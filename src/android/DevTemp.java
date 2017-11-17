
package cordova_plugin_DevTemp;

import org.apache.cordova.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
//import android.util.Log;
import java.util.ArrayList;

/**
 * This class echoes a string called from JavaScript.
 */
public class DevTemp extends CordovaPlugin {

	private CallbackContext callbackContext;
  /**
     * Sets the context of the Command. This can then be used to do things like
     * get file paths associated with the Activity.
     *
     * @param cordova The context of the main Activity.
     * @param webView The CordovaWebView Cordova is running in.
     */
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        this.getTemperatureFiles();
    }
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("getTemperature")) 
		{	
            //String message = args.getString(0);
            //this.getTemperature(message, callbackContext);
			File toto = this.getTempFile("AUTO");
			float temperature = getCpuTemp(toto);
			JSONArray value = new JSONArray();
			value.put(Float.parseFloat(temperature+""));
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, value));
			return true;
        }
        return false;
    }

    public float getCpuTemp(File tempfile)
    {
        try {
            //p = Runtime.getRuntime().exec("cat /sys/class/thermal/thermal_zone0/temp");
            //p.waitFor();
            FileReader fis = new FileReader(tempfile.getPath());
            BufferedReader reader = new BufferedReader(fis);

            String line = reader.readLine();
            float temp = Float.parseFloat(line) / 1000.0f;

            return temp;

        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }
	
   private void getTemperature(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

	/**
	* find the appropriate temperature file for this device.
	**/
	
    private static String[] getTemperatureFiles() 
	{
        ArrayList<String> result = new ArrayList<String>();
        result.add("AUTO");

        try {
            InputStream in = Runtime.getRuntime()
                    .exec("find /sys -type f -name *temp*")
                    .getInputStream();
            BufferedReader inBuffered = new BufferedReader(
                    new InputStreamReader(in));

            String line = null;
            while ((line = inBuffered.readLine()) != null) {
                if(line.indexOf("trip_point")>=0)
                    continue;
                if(line.indexOf("_crit")>=0)
                    continue;

                result.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toArray(new String[]{});
    }

    private static String[] tempFiles = {
            "/sys/devices/virtual/thermal/thermal_zone0/temp",
			"/sys/devices/virtual/thermal/thermal_zone1/temp",
           "/sys/class/thermal/thermal_zone0/temp",
            "/sys/class/thermal/thermal_zone1/temp",
			"/sys/devices/platform/omap/omap_temp_sensor.0/temperature",
            "/sys/kernel/debug/tegra_thermal/temp_tj",
            "/sys/devices/system/cpu/cpu0/cpufreq/cpu_temp",
              "/sys/devices/platform/s5p-tmu/curr_temp",
             "/sys/devices/system/cpu/cpufreq/cput_attributes/cur_temp",
            "/sys/devices/platform/s5p-tmu/temperature",
    };
    public static File getTempFile(String fileName) {
        File ret = null;

        if(fileName!=null) {
            ret = new File(fileName);
            if(!ret.exists() || !ret.canRead())
                ret = null;
        }

        if(ret==null || fileName.equals("AUTO")) {
            for(String tempFileName : tempFiles) {
                ret = new File(tempFileName);
                if(!ret.exists() || !ret.canRead()) {
                    ret = null;
                    continue;
                }
                else break;
            }
        }

        if(ret==null) {
			return null;
            //Log.w(TAG, "Couldn't find any temp files!");
         }

        return ret;
    }
	
}
