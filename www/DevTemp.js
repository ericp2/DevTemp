/*global cordova, module*/

module.exports = {
    getTemperature: function(successCallback) {
        cordova.exec(successCallback, null, "DevTemp", "getTemperature", []);
    }
};

