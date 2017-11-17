# DevTemp
Cordova plugin to measure device temperature  (actually, Battery temperature)

Currently only implemented for Android.

Sample usage:


// create an event to recover device temperature every 2s . this call should probably be done only after onDeviceReady  event received.

window.setInterval(function(){DevTemp.getTemperature(onSuccess);}, 2000);

// log tempeature in java console. can usually be seen from command line by doing something like  :   adb logcat|grep CONSOLE

function onSuccess(values) {
 console.log (values[0]);
  };
