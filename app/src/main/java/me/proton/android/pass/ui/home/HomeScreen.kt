package me.proton.android.pass.ui.home

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import me.proton.android.pass.ui.home.bottomsheet.SortingBottomSheetContents
import me.proton.core.compose.component.ProtonModalBottomSheetLayout

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeScreenNavigation: HomeScreenNavigation,
    onDrawerIconClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val (currentBottomSheet, setBottomSheet) = remember { mutableStateOf(HomeBottomSheetType.CreateItem) }

    val bottomSheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val scope = rememberCoroutineScope()

    ProtonModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            when (currentBottomSheet) {
                HomeBottomSheetType.CreateItem -> FABBottomSheetContents(
                    state = bottomSheetState,
                    navigation = homeScreenNavigation,
                    shareId = uiState.homeListUiState.selectedShare.value()
                )
                HomeBottomSheetType.Sorting -> SortingBottomSheetContents {}
                HomeBottomSheetType.LoginOptions -> {}
                HomeBottomSheetType.AliasOptions -> {}
                HomeBottomSheetType.NoteOptions -> {}
            }
        }
    ) {
        HomeContent(
            modifier = modifier,
            uiState = uiState,
            homeScreenNavigation = homeScreenNavigation,
            onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
            onEnterSearch = { viewModel.onEnterSearch() },
            onStopSearching = { viewModel.onStopSearching() },
            sendItemToTrash = { viewModel.sendItemToTrash(it) },
            onDrawerIconClick = onDrawerIconClick,
            onMoreOptionsClick = {
                setBottomSheet(HomeBottomSheetType.Sorting)
                scope.launch { bottomSheetState.show() }
            },
            onAddItemClick = {
                setBottomSheet(HomeBottomSheetType.CreateItem)
                scope.launch { bottomSheetState.show() }
            },
            onRefresh = { viewModel.onRefresh() }
        )
    }
}
