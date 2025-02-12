package dev.jdtech.jellyfin.models

import dev.jdtech.jellyfin.database.ServerDatabaseDao
import dev.jdtech.jellyfin.repository.JellyfinRepository
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.BaseItemKind
import org.jellyfin.sdk.model.api.ImageType
import java.util.UUID

interface FindroidItem {
    val id: UUID
    val name: String
    val originalTitle: String?
    val overview: String
    val played: Boolean
    val favorite: Boolean
    val canPlay: Boolean
    val canDownload: Boolean
    val sources: List<FindroidSource>
    val runtimeTicks: Long
    val playbackPositionTicks: Long
    val unplayedItemCount: Int?
    val imageTags: Map<ImageType, String>?
    val imageBlurHashes: Map<ImageType, Map<String, String>>?
}

suspend fun BaseItemDto.toFindroidItem(
    jellyfinRepository: JellyfinRepository,
    serverDatabase: ServerDatabaseDao? = null,
): FindroidItem? {
    return when (type) {
        BaseItemKind.MOVIE -> toFindroidMovie(jellyfinRepository, serverDatabase)
        BaseItemKind.EPISODE -> toFindroidEpisode(jellyfinRepository)
        BaseItemKind.SEASON -> toFindroidSeason()
        BaseItemKind.SERIES -> toFindroidShow()
        BaseItemKind.BOX_SET -> toFindroidBoxSet()
        BaseItemKind.FOLDER -> toFindroidFolder()
        else -> null
    }
}

fun FindroidItem.isDownloading(): Boolean {
    return sources.filter { it.type == FindroidSourceType.LOCAL }.any { it.path.endsWith(".download") }
}

fun FindroidItem.isDownloaded(): Boolean {
    return sources.filter { it.type == FindroidSourceType.LOCAL }.any { !it.path.endsWith(".download") }
}
