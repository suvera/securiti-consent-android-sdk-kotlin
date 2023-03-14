package ai.securiti.consent.prefcenter

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class ConsentPayload {
    @JsonProperty("timestamp")
    var timestamp: Int = 0

    @JsonProperty("consent_info")
    var consentInfo: List<ConsentPayloadConsentInfo> = ArrayList()
}

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class GeoConsentPayload {
    @JsonProperty("locationCode")
    var locationCode: String = ""

    @JsonProperty("tenantAuthId")
    var tenantAuthId: String = ""

    @JsonProperty("prefCenterId")
    var prefCenterId: String = ""

    @JsonProperty("primaryIdentifier")
    var primaryIdentifier: String = ""

    @JsonProperty("payload")
    var payload: ConsentPayload = ConsentPayload()
}

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class ConsentPayloadConsentInfo {
    @JsonProperty("processing_purpose_id")
    var processingPurposeId: Int = 0

    @JsonProperty("consented_items")
    var consentedItems: ArrayList<ConsentPayloadConsentItem> = ArrayList()
}

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class ConsentPayloadConsentItem {
    @JsonProperty("consent_purpose_id")
    var consentPurposeId: Int = 0

    @JsonProperty("granted")
    var granted: Boolean = false
}