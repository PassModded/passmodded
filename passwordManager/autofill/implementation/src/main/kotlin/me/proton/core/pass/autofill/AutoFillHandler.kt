package me.proton.core.pass.autofill

import android.app.PendingIntent
import android.app.assist.AssistStructure
import android.content.Context
import android.os.CancellationSignal
import android.service.autofill.Dataset
import android.service.autofill.FillCallback
import android.service.autofill.FillRequest
import android.service.autofill.FillResponse
import android.service.autofill.SaveInfo
import android.widget.RemoteViews
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import me.proton.android.pass.log.PassLogger
import me.proton.core.pass.autofill.Utils.getWindowNodes
import me.proton.core.pass.autofill.entities.AndroidAutofillFieldId
import me.proton.core.pass.autofill.entities.AssistField
import me.proton.core.pass.autofill.entities.asAndroid
import me.proton.core.pass.autofill.service.R
import me.proton.core.pass.autofill.ui.autofill.AutofillActivity
import kotlin.coroutines.coroutineContext

object AutoFillHandler {
    fun handleAutofill(
        context: Context,
        request: FillRequest,
        cancellationSignal: CancellationSignal,
        callback: FillCallback
    ) {
        val windowNode = getWindowNodes(request.fillContexts.last()).lastOrNull()
        if (windowNode?.rootViewNode == null) {
            callback.onFailure(context.getString(R.string.error_cant_find_matching_fields))
            return
        }

        val handler = CoroutineExceptionHandler { _, exception ->
            PassLogger.e("AutoFillHandler", exception)
        }
        val job = CoroutineScope(Dispatchers.IO).launch(handler) {
            searchAndFill(context, windowNode, request, callback)
        }

        cancellationSignal.setOnCancelListener {
            job.cancel()
        }
    }

    @Suppress("UnusedPrivateMember")
    private suspend fun searchAndFill(
        context: Context,
        windowNode: AssistStructure.WindowNode,
        request: FillRequest,
        callback: FillCallback
    ) {
        val assistFields: List<AssistField> =
            AssistNodeTraversal().traverse(windowNode.rootViewNode)
        if (assistFields.isEmpty()) return
        val listItemId = android.R.layout.simple_list_item_1

        val authenticateView = RemoteViews(context.packageName, listItemId).apply {
            setTextViewText(android.R.id.text1, context.getString(R.string.autofill_authenticate_prompt))
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            AutofillActivity.REQUEST_CODE,
            AutofillActivity.newIntent(context, assistFields),
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        // Single Dataset to force user authentication
        val dataset = Dataset.Builder(authenticateView)
            .apply {
                setAuthentication(pendingIntent.intentSender)
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                    val inlineSuggestionSpecs = request.inlineSuggestionsRequest
//                        ?.inlinePresentationSpecs.orEmpty()
//                    for (spec in inlineSuggestionSpecs) {
//                        addInlineSuggestion(this, spec, pendingIntent)
//                    }
//                }
                for (value in assistFields) {
                    setValue(value.id.asAndroid().autofillId, null)
                }
            }
            .build()

        if (!coroutineContext.isActive) {
            callback.onFailure(context.getString(R.string.error_credentials_not_found))
            return
        }

        val autofillIds = assistFields.map {
            (it.id as AndroidAutofillFieldId).autofillId
        }.toTypedArray()
        val saveInfo = SaveInfo.Builder(SaveInfo.SAVE_DATA_TYPE_GENERIC, autofillIds).build()
        val response = FillResponse.Builder()
            .addDataset(dataset)
            .setSaveInfo(saveInfo)
            .build()

        callback.onSuccess(response)
    }
}
