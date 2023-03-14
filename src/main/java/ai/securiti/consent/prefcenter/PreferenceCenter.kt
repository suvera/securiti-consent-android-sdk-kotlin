package ai.securiti.consent.prefcenter

import android.util.Log
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.readValue

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
class PreferenceCenter {
    @JsonProperty("status")
    var status: Int = 0

    @JsonProperty("message")
    var message: String = ""

    @JsonProperty("data")
    var data: PreferenceCenterData? = null

    @JsonIgnore
    private var metaData: PreferenceCenterMetadata? = null

    @JsonIgnore
    private var purposeTree: List<ProcessingPurpose>? = null

    fun getMetaData(): PreferenceCenterMetadata {
        if (data == null || data?.metaData!!.isEmpty()) {
            return PreferenceCenterMetadata()
        } else if (metaData != null) {
            return metaData as PreferenceCenterMetadata
        }

        val mapper = Utils.mapper

        try {
            metaData = mapper.readValue(data?.metaData!!)

            return metaData as PreferenceCenterMetadata
        } catch (e: Exception) {
            // print this error ?
        }

        return PreferenceCenterMetadata()
    }

    fun getPurposeTree(): List<ProcessingPurpose> {
        if (data == null || data?.preferenceTree == null || data?.preferenceTree!!.isEmpty()) {
            Log.i("PreferenceCenter", "data.preferenceTree is empty")
            return listOf()
        } else if (purposeTree != null) {
            Log.i("PreferenceCenter", "purposeTree variable is empty")
            return purposeTree as List<ProcessingPurpose>
        }

        val mapper = Utils.mapper

        try {
            purposeTree = mapper.readValue(data?.preferenceTree!!)

            return purposeTree as List<ProcessingPurpose>
        } catch (e: Exception) {
            Log.e("PreferenceCenter", e.stackTraceToString())
        }

        return listOf()
    }

}

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
class PreferenceCenterData {
    @JsonProperty("base64_logo")
    var base64Logo: String = ""

    @JsonProperty("button_text")
    var buttonText: String = ""

    @JsonProperty("metadata")
    var metaData: String = ""

    @JsonProperty("narrow_path_enabled")
    var narrowPathEnabled: Boolean = false

    @JsonProperty("org_unit_id")
    var orgUnitId: Int = 0

    @JsonProperty("preference_center_ux_v2")
    var uxV2: Boolean = false

    @JsonProperty("purpose_category_enabled")
    var purposeCategoryEnabled: Boolean = false

    @JsonProperty("preference_tree")
    var preferenceTree: String? = ""

    /*
        "processing_purpose_mappings": {},
        "processing_purpose_translations": {},
        "purpose_category_mappings": {},
        "purpose_category_translations": {}
        "consent_purpose_mappings": {},
        "consent_purpose_translations": {},
        "default_translations": {},
        "preference_center_mappings": {},
        "preference_center_translations": {},
     */
}

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
class PreferenceCenterMetadata {
    @JsonProperty("buttonText")
    var buttonText: String = ""

    @JsonProperty("textColor")
    var textColor: String = ""

    @JsonProperty("bgColor")
    var bgColor: String = ""

    @JsonProperty("formTextColor")
    var formTextColor: String = ""

    @JsonProperty("prefCenterHeader")
    var prefCenterHeader: String = "Header Text"

    @JsonProperty("prefCenterBody")
    var prefCenterBody: String = "Body Text"
}

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
class ProcessingPurpose {
    @JsonProperty("id")
    var id: Int = 0

    @JsonProperty("name")
    var name: String = ""

    @JsonProperty("description")
    var description: String? = ""

    @JsonProperty("category_id")
    var categoryId: Int? = 0

    @JsonProperty("category_name")
    var categoryName: String? = ""

    @JsonProperty("custom")
    var custom: Boolean = false

    @JsonProperty("consent_purposes")
    var consentPurposes: List<ConsentPurpose>? = listOf()
}

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
class ConsentPurpose {
    @JsonProperty("id")
    var id: Int = 0

    @JsonProperty("name")
    var name: String = ""

    @JsonProperty("custom")
    var custom: Boolean = false

    @JsonProperty("required")
    var required: Boolean = false
    // "translations_mapping":{}
    // "translations_generated":null
}

