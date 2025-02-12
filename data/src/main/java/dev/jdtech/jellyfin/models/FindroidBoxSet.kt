package dev.jdtech.jellyfin.models

import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.ImageType
import java.util.UUID

data class FindroidBoxSet(
    override val id: UUID,
    override val name: String,
    override val originalTitle: String? = null,
    override val overview: String = "",
    override val played: Boolean = false,
    override val favorite: Boolean = false,
    override val canPlay: Boolean = false,
    override val canDownload: Boolean = false,
    override val sources: List<FindroidSource> = emptyList(),
    override val runtimeTicks: Long = 0L,
    override val playbackPositionTicks: Long = 0L,
    override val unplayedItemCount: Int? = null,
    override val imageTags: Map<ImageType, String>?,
    override val imageBlurHashes: Map<ImageType, Map<String, String>>?,
) : FindroidItem

fun BaseItemDto.toFindroidBoxSet(): FindroidBoxSet {
    return FindroidBoxSet(
        id = id,
        name = name.orEmpty(),
        imageTags = imageTags,
        imageBlurHashes = imageBlurHashes,
    )
}
