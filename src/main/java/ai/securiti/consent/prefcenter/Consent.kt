package ai.securiti.consent.prefcenter

class Consent(
    var processingPurposeId: Int = 0,
    var processingPurposeName: String = "",
    var consentPurposeId: Int = 0,
    var consentPurposeName: String = "",
    var granted: Boolean = false
) {

    fun getId(): String {
        return "${processingPurposeId}-${consentPurposeId}"
    }
}
