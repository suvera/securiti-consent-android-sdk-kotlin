package ai.securiti.consent.prefcenter

interface ConsentActivityListener {

    fun onPreferenceCenterLoaded(purposes: List<Consent>)

    fun onPreferenceCenterLoadFailed(ex: Exception)

    fun onConsentsSaved(consents: List<Consent>)

    fun onConsentsSaveFailed(consents: List<Consent>, ex: Exception)

    fun onConsentsCancelled()
}