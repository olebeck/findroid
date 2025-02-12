package dev.jdtech.jellyfin.models

import dev.jdtech.jellyfin.database.ServerDatabaseDao
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.ImageType
import org.jellyfin.sdk.model.api.PlayAccess
import java.util.UUID

data class FindroidFolder(
    override val id: UUID,
    override val name: String,
    override val originalTitle: String?,
    override val overview: String,
    override val sources: List<FindroidSource>,
    override val played: Boolean,
    override val favorite: Boolean,
    override val canPlay: Boolean,
    override val canDownload: Boolean,
    override val runtimeTicks: Long = 0L,
    override val playbackPositionTicks: Long = 0L,
    override val unplayedItemCount: Int?,
    override val imageTags: Map<ImageType, String>?,
    override val imageBlurHashes: Map<ImageType, Map<String, String>>?,
) : FindroidItem

fun BaseItemDto.toFindroidFolder(): FindroidFolder {
    return FindroidFolder(
        id = id,
        name = name.orEmpty(),
        originalTitle = originalTitle,
        overview = overview.orEmpty(),
        played = userData?.played ?: false,
        favorite = userData?.isFavorite ?: false,
        canPlay = playAccess != PlayAccess.NONE,
        canDownload = canDownload ?: false,
        unplayedItemCount = userData?.unplayedItemCount,
        sources = emptyList(),
        imageTags = imageTags,
        imageBlurHashes = imageBlurHashes,
    )
}
