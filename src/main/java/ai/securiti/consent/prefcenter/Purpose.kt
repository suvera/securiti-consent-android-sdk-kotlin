package ai.securiti.consent.prefcenter

import ai.securiti.consent.prefcenter.ConsentPurpose
import ai.securiti.consent.prefcenter.ProcessingPurpose

@Suppress("MemberVisibilityCanBePrivate")
class Purpose(val processing: ProcessingPurpose, val consent: ConsentPurpose, var granted: Boolean = false) {

    fun getId(): String {
        return "${processing.id}-${consent.id}"
    }
}