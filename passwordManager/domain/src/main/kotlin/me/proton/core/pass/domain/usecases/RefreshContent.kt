package me.proton.core.pass.domain.usecases

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import me.proton.core.domain.entity.UserId
import me.proton.core.pass.common.api.Result
import me.proton.core.pass.common.api.map
import me.proton.core.pass.common.api.onSuccess
import me.proton.core.pass.domain.repositories.ItemRepository
import me.proton.core.pass.domain.repositories.ShareRepository
import javax.inject.Inject

interface RefreshContent {
    suspend operator fun invoke(userId: UserId): Result<Unit>
}

class RefreshContentImpl @Inject constructor(
    private val shareRepository: ShareRepository,
    private val itemRepository: ItemRepository
) : RefreshContent {

    override suspend fun invoke(userId: UserId): Result<Unit> =
        shareRepository.refreshShares(userId)
            .onSuccess { shares ->
                coroutineScope {
                    shares.map { share ->
                        async { itemRepository.refreshItems(userId, share) }
                    }.awaitAll()
                }
            }
            .map { }

}
