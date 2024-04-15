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

package proton.android.pass.features.security.center.excludeditems.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import proton.android.pass.features.security.center.excludeditems.navigation.SecurityCenterExcludeItemsDestination
import proton.android.pass.features.security.center.excludeditems.presentation.SecurityCenterExcludedItemsViewModel

@Composable
fun SecurityCenterExcludedItemsScreen(
    onNavigated: (SecurityCenterExcludeItemsDestination) -> Unit,
    viewModel: SecurityCenterExcludedItemsViewModel = hiltViewModel()
) = with(viewModel) {
    val state by state.collectAsStateWithLifecycle()

    SecurityCenterExcludedItemsContent(
        state = state,
        onUiEvent = { uiEvent ->
            when (uiEvent) {
                SecurityCenterExcludedItemsUiEvent.Back -> onNavigated(
                    SecurityCenterExcludeItemsDestination.Back
                )

                is SecurityCenterExcludedItemsUiEvent.OnShowItemDetails -> onNavigated(
                    SecurityCenterExcludeItemsDestination.ItemDetails(
                        shareId = uiEvent.shareId,
                        itemId = uiEvent.itemId
                    )
                )
            }
        }
    )
}
