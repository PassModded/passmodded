package proton.android.pass.featureaccount.impl

import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import proton.android.pass.commonui.api.PassTheme
import proton.android.pass.commonui.api.ThemePreviewProvider
import proton.android.pass.composecomponents.impl.container.roundedContainerNorm
import proton.android.pass.composecomponents.impl.setting.ColorSettingOption
import me.proton.core.presentation.compose.R as CoreR

@Composable
fun SignOut(modifier: Modifier = Modifier, onSignOutClick: () -> Unit) {
    ColorSettingOption(
        modifier = modifier.roundedContainerNorm(),
        text = stringResource(R.string.account_sign_out),
        textColor = PassTheme.colors.interactionNorm,
        iconBgColor = PassTheme.colors.interactionNormMinor2,
        icon = {
            Icon(
                painter = painterResource(CoreR.drawable.ic_proton_arrow_out_from_rectangle),
                contentDescription = stringResource(R.string.account_sign_out_icon_content_description),
                tint = PassTheme.colors.interactionNorm
            )
        },
        onClick = onSignOutClick
    )
}

@Preview
@Composable
fun SignOutPreview(
    @PreviewParameter(ThemePreviewProvider::class) isDark: Boolean
) {
    PassTheme(isDark = isDark) {
        Surface {
            SignOut {}
        }
    }
}
