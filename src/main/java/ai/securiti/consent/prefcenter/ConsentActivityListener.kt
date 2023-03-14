package ai.securiti.consent.prefcenter

interface ConsentActivityListener {

    fun onPurposeLoaded(purposes: List<Consent>)

    fun onConsentsSubmitted(consents: List<Consent>)

    fun onConsentsCancelled()
}