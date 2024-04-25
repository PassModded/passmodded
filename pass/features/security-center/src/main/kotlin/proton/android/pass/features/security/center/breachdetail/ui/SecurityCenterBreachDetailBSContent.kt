/*
 * Copyright (c) 2024 Proton AG
 * This file is part of Proton AG and Proton Pass.
 *
 * Proton Pass is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Proton Pass is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Proton Pass.  If not, see <https://www.gnu.org/licenses/>.
 */

package proton.android.pass.features.security.center.breachdetail.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import me.proton.core.compose.theme.ProtonTheme
import proton.android.pass.commonui.api.PassTheme
import proton.android.pass.commonui.api.Spacing
import proton.android.pass.commonui.api.ThemedBooleanPreviewProvider
import proton.android.pass.commonui.api.applyIf
import proton.android.pass.commonui.api.body3Norm
import proton.android.pass.commonui.api.body3Weak
import proton.android.pass.commonui.api.bottomSheet
import proton.android.pass.composecomponents.impl.container.roundedContainer
import proton.android.pass.composecomponents.impl.extension.toResource
import proton.android.pass.domain.breach.BreachEmail
import proton.android.pass.features.security.center.R
import proton.android.pass.features.security.center.breachdetail.presentation.SecurityCenterBreachDetailState
import proton.android.pass.features.security.center.shared.ui.DateUtils
import proton.android.pass.features.security.center.shared.ui.image.BreachImage
import me.proton.core.presentation.R as CoreR

@Composable
internal fun SecurityCenterBreachDetailBSContent(
    modifier: Modifier = Modifier,
    state: SecurityCenterBreachDetailState,
    onOpenUrl: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = PassTheme.colors.backgroundNorm)
            .bottomSheet(Spacing.medium)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Spacing.medium)
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } else {
            state.breachEmail?.let { breachEmail ->
                BreachDetailHeader(breachEmail = breachEmail)
                ExposedData(breachEmail = breachEmail)
                Details(breachEmail = breachEmail)
                RecommendedActions(
                    breachEmail = breachEmail,
                    onOpenUrl = onOpenUrl
                )
                Text(
                    text = stringResource(
                        id = R.string.security_center_report_detail_note,
                        breachEmail.name
                    ),
                    style = ProtonTheme.typography.body2Regular
                )
            }
        }
    }
}

@Composable
internal fun RecommendedActions(
    modifier: Modifier = Modifier,
    breachEmail: BreachEmail,
    onOpenUrl: (String) -> Unit
) {
    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(Spacing.medium)
    ) {
        if (breachEmail.actions.isNotEmpty()) {
            Text(
                text = stringResource(R.string.security_center_report_detail_recommended_actions),
                style = ProtonTheme.typography.body1Medium
            )
        }


        Column(verticalArrangement = Arrangement.spacedBy(Spacing.small)) {
            breachEmail.actions.forEach { action ->
                RecommendedAction(
                    text = action.name,
                    icon = action.code.toResource(),
                    url = action.url,
                    onClick = {
                        action.url?.let { onOpenUrl(it) }
                    }
                )
            }
        }
    }
}

@Composable
private fun RecommendedAction(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes icon: Int,
    url: String?,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .roundedContainer(
                backgroundColor = Color.Transparent,
                borderColor = PassTheme.colors.inputBorderNorm
            )
            .applyIf(
                condition = url != null,
                ifTrue = { clickable(onClick = onClick) }
            )
            .fillMaxWidth()
            .padding(Spacing.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.small)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = PassTheme.colors.loginInteractionNormMajor2
        )
        Text(
            modifier = Modifier.weight(1f),
            text = text
        )
        if (url != null) {
            Icon(
                painter = painterResource(id = CoreR.drawable.ic_proton_arrow_out_square),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun Details(modifier: Modifier = Modifier, breachEmail: BreachEmail) {
    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(Spacing.medium)
    ) {
        if (breachEmail.email.isNotBlank() || !breachEmail.passwordLastChars.isNullOrBlank()) {
            Text(
                text = stringResource(R.string.security_center_report_detail_details),
                style = ProtonTheme.typography.body1Medium
            )
            breachEmail.email.takeIf { it.isNotBlank() }?.let { email ->
                Column {
                    Text(
                        text = stringResource(R.string.security_center_report_detail_email_address),
                        style = PassTheme.typography.body3Norm()
                    )
                    Text(text = email, style = ProtonTheme.typography.body1Regular)
                }
            }
            breachEmail.passwordLastChars?.let { password ->
                Column {
                    Text(
                        text = stringResource(R.string.security_center_report_detail_password),
                        style = PassTheme.typography.body3Norm()
                    )
                    Text(
                        text = password,
                        style = ProtonTheme.typography.body1Regular
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun ExposedData(modifier: Modifier = Modifier, breachEmail: BreachEmail) {
    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(Spacing.medium)
    ) {
        breachEmail.exposedData.takeIf { it.isNotEmpty() }?.let { dataList ->
            Text(
                text = stringResource(R.string.security_center_report_detail_your_exposed_information),
                style = ProtonTheme.typography.body1Medium
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(Spacing.small),
                verticalArrangement = Arrangement.spacedBy(Spacing.small)
            ) {
                dataList.forEach {
                    Text(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(PassTheme.colors.signalDanger)
                            .padding(Spacing.small),
                        text = it,
                        style = PassTheme.typography.body3Norm(),
                        color = PassTheme.colors.textInvert
                    )
                }
            }
        }
    }
}

@Composable
private fun BreachDetailHeader(modifier: Modifier = Modifier, breachEmail: BreachEmail) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Spacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BreachImage(isResolved = breachEmail.isResolved)

        Column(verticalArrangement = Arrangement.spacedBy(Spacing.small)) {
            Text(text = breachEmail.name, style = ProtonTheme.typography.headline)

            DateUtils.formatDate(breachEmail.publishedAt)
                .onSuccess { date ->
                    val bodyTextResource =
                        stringResource(R.string.security_center_report_detail_subtitle)
                    val bodyText = buildAnnotatedString {
                        val textParts = bodyTextResource.split("__DATE__")
                        if (textParts.size == 2) {
                            append(textParts[0])
                            append(
                                AnnotatedString(
                                    date,
                                    SpanStyle(fontWeight = FontWeight.Bold)
                                )
                            )
                            append(textParts[1])
                        } else {
                            append(bodyTextResource)
                        }
                    }

                    Text(
                        text = bodyText,
                        style = PassTheme.typography.body3Weak()
                    )
                }
        }
    }
}

@Preview
@Composable
fun RecommendedActionPreview(@PreviewParameter(ThemedBooleanPreviewProvider::class) input: Pair<Boolean, Boolean>) {
    val url = if (input.second) "test.url" else null
    PassTheme(isDark = input.first) {
        Surface {
            RecommendedAction(
                text = "A recommended action",
                icon = CoreR.drawable.ic_proton_key,
                url = url,
                onClick = {}
            )
        }
    }
}
