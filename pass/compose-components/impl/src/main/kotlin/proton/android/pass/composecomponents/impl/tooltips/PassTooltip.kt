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

package proton.android.pass.composecomponents.impl.tooltips

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import me.proton.core.compose.theme.ProtonTheme
import proton.android.pass.commonui.api.PassTheme
import proton.android.pass.commonui.api.Spacing
import proton.android.pass.commonui.api.ThemePreviewProvider
import proton.android.pass.commonui.api.body3Weak
import me.proton.core.presentation.R as CoreR

@Composable
fun PassTooltip(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    onDismiss: () -> Unit,
) {
    Column(
        modifier = modifier
            .background(color = PassTheme.colors.searchBarBackground)
            .padding(
                horizontal = Spacing.medium,
                vertical = Spacing.small
            ),
        verticalArrangement = Arrangement.spacedBy(space = Spacing.extraSmall)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = ProtonTheme.typography.body1Bold,
                color = PassTheme.colors.textNorm
            )

            Icon(
                modifier = Modifier
                    .clickable { onDismiss() },
                painter = painterResource(id = CoreR.drawable.ic_proton_cross_small),
                contentDescription = null,
                tint = PassTheme.colors.textWeak
            )
        }

        Text(
            text = description,
            style = PassTheme.typography.body3Weak(),
            color = PassTheme.colors.textWeak
        )
    }
}

@[Preview Composable]
internal fun PassTooltipPreview(@PreviewParameter(ThemePreviewProvider::class) isDark: Boolean) {
    PassTheme(isDark = isDark) {
        Surface {
            PassTooltip(
                title = "Tooltip title",
                description = "This is the tooltip description",
                onDismiss = {}
            )
        }
    }
}
