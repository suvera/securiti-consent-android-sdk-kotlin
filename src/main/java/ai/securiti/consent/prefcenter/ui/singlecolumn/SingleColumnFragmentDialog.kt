package ai.securiti.consent.prefcenter.ui.singlecolumn

import ai.securiti.android.prefcenter.R
import ai.securiti.consent.prefcenter.PreferenceCenter
import ai.securiti.consent.prefcenter.PreferenceCenterSDK
import ai.securiti.consent.prefcenter.ProcessingPurpose
import ai.securiti.consent.prefcenter.Purpose
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager.LayoutParams
import android.widget.*
import androidx.fragment.app.DialogFragment


class SingleColumnFragmentDialog(private val sdk: PreferenceCenterSDK, private val prefCenter: PreferenceCenter) :
    DialogFragment() {
    private val consentStore = HashMap<String, Purpose>()

    override fun onCreateDialog(bundle: Bundle?): Dialog {
        Log.i("SingleColumnFragmentDialog", "Single column preference center started")
        val builder = AlertDialog.Builder(activity)

        val metadata = prefCenter.getMetaData()
        builder.setTitle(Html.fromHtml(metadata.prefCenterHeader, Html.FROM_HTML_MODE_LEGACY))

        val saveListener = SingleColumnFragmentDialogSaveListener(sdk, prefCenter, consentStore)
        builder.setPositiveButton(metadata.buttonText, saveListener)

        val cancelListener = SingleColumnFragmentDialogCancelListener(sdk, prefCenter)
        builder.setNegativeButton(R.string.securiti_lbl_cancel, cancelListener)

        builder.setView(buildView())

        val dialog = builder.create()
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setDecorFitsSystemWindows(true)
        dialog.window?.setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_PAN or LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        dialog.setOnShowListener {
            val btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            btn.setBackgroundColor(Color.parseColor(metadata.bgColor))
            btn.setTextColor(Color.parseColor("#FFFFFF"))
        }

        return dialog
    }

    private fun buildView(): View {
        val metadata = prefCenter.getMetaData()

        val layout = ScrollView(sdk.appCtx)

        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        //layoutParams.setMargins(5, 0, 2, 5)
        layout.layoutParams = layoutParams

        val linearLayout = LinearLayout(sdk.appCtx)
        val linearParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        linearParams.getRule(RelativeLayout.ALIGN_PARENT_TOP)
        linearParams.setMargins(8, 0, 2, 5)
        linearLayout.orientation = LinearLayout.VERTICAL;
        linearLayout.layoutParams = linearParams

        val desc = buildTextView(metadata.prefCenterBody)
        linearLayout.addView(desc, -1)

        val byCategory = HashMap<String, ArrayList<ProcessingPurpose>>()
        val purposeTree = prefCenter.getPurposeTree()
        for (item in purposeTree) {
            val category = item.categoryName ?: ""

            if (!byCategory.containsKey(category)) {
                byCategory[category] = ArrayList()
            }
            byCategory[category]?.add(item)
        }

        for ((category, items) in byCategory.toSortedMap()) {
            if (category.isNotEmpty()) {
                val tmpTxt = buildTextView("<h3>$category</h3>")
                linearLayout.addView(tmpTxt, -1)
            }

            for (item in items) {
                val tmpTxt = buildTextView("<b>${item.name}</b>")
                linearLayout.addView(tmpTxt, -1)
                linearLayout.addView(buildLine(), -1)
                if (item.description?.isNotEmpty() == true) {
                    val tmpDescTxt = buildTextView("${item.description}")
                    linearLayout.addView(tmpDescTxt, -1)
                }
                if (item.consentPurposes != null) {
                    for (consent in item.consentPurposes!!) {
                        val record = Purpose(item, consent, false)
                        consentStore[record.getId()] = record
                        val chkBox = buildConsentItemView(record)
                        linearLayout.addView(chkBox)
                    }
                }
            }
        }

        layout.addView(linearLayout)
        return layout
    }

    private fun buildTextView(text: String): TextView {
        val tmpTxt = TextView(sdk.appCtx)
        val tmpTxtParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        tmpTxt.text = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        tmpTxt.layoutParams = tmpTxtParams
        return tmpTxt
    }

    private fun buildConsentItemView(consent: Purpose): CheckBox {
        val tmpCheck = CheckBox(sdk.appCtx)
        val tmpTxtParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        tmpCheck.setPadding(20, 0, 5, 5)
        tmpCheck.isChecked = consent.granted
        tmpCheck.text = Html.fromHtml(consent.consent.name, Html.FROM_HTML_MODE_LEGACY)
        tmpCheck.layoutParams = tmpTxtParams

        tmpCheck.setOnCheckedChangeListener { _, isChecked ->
            val msg = "You have ${consent.consent.name} is " + (if (isChecked) "checked" else "unchecked") + "."
            Log.i("SingleColumnFragmentDialog", msg)
            consent.granted = isChecked
        }

        return tmpCheck
    }

    private fun buildLine(): View {
        val v = View(sdk.appCtx)
        v.layoutParams = LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT,
            5
        )
        v.setBackgroundColor(Color.parseColor("#B3B3B3"))
        return v
    }
}

private class SingleColumnFragmentDialogSaveListener(
    val sdk: PreferenceCenterSDK,
    val prefCenter: PreferenceCenter,
    val consentStore: HashMap<String, Purpose>
) :
    DialogInterface.OnClickListener {
    override fun onClick(dialog: DialogInterface?, id: Int) {
        //Toast.makeText(sdk.appCtx, "Save Clicked", Toast.LENGTH_SHORT).show()
        sdk.saveConsent(ArrayList(consentStore.values))
        dialog?.dismiss()
    }
}

private class SingleColumnFragmentDialogCancelListener(val sdk: PreferenceCenterSDK, val prefCenter: PreferenceCenter) :
    DialogInterface.OnClickListener {
    override fun onClick(dialog: DialogInterface?, id: Int) {
        Toast.makeText(sdk.appCtx, "Cancel Clicked", Toast.LENGTH_SHORT).show()
        dialog?.dismiss()
    }
}