/*
 * Copyright (c) 2023 Proton AG
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

package proton.android.pass.composecomponents.impl.item

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.datetime.Clock
import proton.android.pass.commonuimodels.api.ItemUiModel
import proton.android.pass.domain.AddressDetails
import proton.android.pass.domain.ContactDetails
import proton.android.pass.domain.ItemContents
import proton.android.pass.domain.ItemId
import proton.android.pass.domain.PersonalDetails
import proton.android.pass.domain.ShareId
import proton.android.pass.domain.WorkDetails

class IdentityRowPreviewProvider : PreviewParameterProvider<IdentityRowParameter> {
    override val values: Sequence<IdentityRowParameter>
        get() = sequenceOf(
            with(title = "Empty identity")
        )

    companion object {
        private fun with(title: String, highlight: String = ""): IdentityRowParameter = IdentityRowParameter(
            model = ItemUiModel(
                id = ItemId("123"),
                shareId = ShareId("345"),
                contents = ItemContents.Identity(
                    title = title,
                    note = "",
                    personalDetails = PersonalDetails.EMPTY,
                    addressDetails = AddressDetails.EMPTY,
                    contactDetails = ContactDetails.EMPTY,
                    workDetails = WorkDetails.EMPTY
                ),
                state = 0,
                createTime = Clock.System.now(),
                modificationTime = Clock.System.now(),
                lastAutofillTime = Clock.System.now(),
                isPinned = false
            ),
            highlight = highlight
        )
    }
}

data class IdentityRowParameter(
    val model: ItemUiModel,
    val highlight: String
)

