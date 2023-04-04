package ai.securiti.consent.prefcenter

import android.content.Context
import android.content.SharedPreferences

class PreferenceStore(private val conf: Configuration, val appCtx: Context) {

    companion object {
        const val STORE_PREFIX = "securiti.consent."
        const val GRANTED = 1
    }

    private val sharedPref: SharedPreferences =
        appCtx.getSharedPreferences(STORE_PREFIX + conf.prefCenterId, Context.MODE_PRIVATE)

    public fun purposeGranted(consent: Consent): Boolean {
        return (sharedPref.getInt(consent.getId(), 0) == GRANTED)
    }

    public fun putConsents(records: List<Consent>) {
        with(sharedPref.edit()) {
            for (consent in records) {
                putInt(consent.getId(), if (consent.granted) 1 else 0)
            }
            apply()
        }
    }
}