package ai.securiti.consent.prefcenter

import android.content.res.Resources
import android.util.Log
import androidx.core.os.ConfigurationCompat
import kotlin.math.log

/**
 * Example:
 *  prefCenterId: "8f45c2d0-b7db-449d-b897-b13889cd33fa"
 *  prefCenterToken: "app~ce603cd7-3890-490d-9493-9fee1a9a75f8"
 */
class Configuration(
    val prefCenterId: String,
    val prefCenterToken: String,
    var lang: String = "",
    var location: String = ""
) {
    private var _region: String = ""
    val apiHost: String get() = regions[_region]?.apiHost ?: ""
    val cdnHost: String get() = regions[_region]?.cdnHost ?: ""

    init {
        print("invoke method called")
        if (prefCenterId.isBlank() || prefCenterToken.isBlank()) {
            throw RuntimeException("prefCenterId/prefCenterToken cannot be blank")
        }

        val region = prefCenterToken.split("~")[0]
        _region = region
        Log.i(TAG, "region: $region")

        if (!regions.containsKey(region)) {
            Log.e(TAG, "region $region does not exist")
            throw RuntimeException("region $region does not exist")
        }

        //var locale = Resources.getSystem().getConfiguration().getLocales().get(0)
        val localeList = ConfigurationCompat.getLocales(Resources.getSystem().configuration)
        val locale = localeList.get(0)

        if (lang.isEmpty() && locale != null && locale.language.isNotEmpty()) {
            lang = locale.language
        }
        if (location.isEmpty() && locale != null && locale.country.isNotEmpty()) {
            location = locale.country
        }
    }

    companion object {
        private const val TAG = "Configuration"
        val regions = mutableMapOf(
            "app" to Region("app", "https://app.securiti.ai", "https://cdn-prod.securiti.ai"),
            "app-eu" to Region("app-eu", "https://app.eu.securiti.ai", "https://cdn-prod.eu.securiti.ai"),
            "app2" to Region("app2", "https://app2.securiti.ai", "https://cdn-app2.securiti.ai"),
            "qa" to Region("qa", "https://qa.securiti.xyz", "https://cdn-qa.securiti.xyz"),
            "dev" to Region("dev", "https://dev.securiti.xyz", "https://cdn-dev.securiti.xyz"),
        )
    }
}

