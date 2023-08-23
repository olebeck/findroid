package dev.jdtech.jellyfin

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toDrawable
import coil.load
import dev.jdtech.jellyfin.api.JellyfinApi
import dev.jdtech.jellyfin.models.FindroidEpisode
import dev.jdtech.jellyfin.models.FindroidItem
import dev.jdtech.jellyfin.models.FindroidMovie
import dev.jdtech.jellyfin.models.User
import dev.jdtech.jellyfin.utils.BlurHashDecoder
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.BaseItemKind
import org.jellyfin.sdk.model.api.BaseItemPerson
import org.jellyfin.sdk.model.api.ImageType
import java.util.UUID
import dev.jdtech.jellyfin.core.R as CoreR

fun bindItemImage(imageView: ImageView, item: BaseItemDto) {
    val itemId =
        if (item.type == BaseItemKind.EPISODE || item.type == BaseItemKind.SEASON && item.imageTags.isNullOrEmpty()) item.seriesId else item.id

    val imageId = item.imageTags?.get(ImageType.PRIMARY)
    val blurHash = item.imageBlurHashes?.get(ImageType.PRIMARY)?.get(imageId)

    imageView
        .loadImage("/items/$itemId/Images/${ImageType.PRIMARY}", placeholderHash = blurHash)
        .posterDescription(item.name)
}

fun bindItemImage(imageView: ImageView, item: FindroidItem) {
    val itemId = when (item) {
        is FindroidEpisode -> item.seriesId
        else -> item.id
    }

    val imageId = item.imageTags?.get(ImageType.PRIMARY)
    val blurHash = item.imageBlurHashes?.get(ImageType.PRIMARY)?.get(imageId)

    imageView
        .loadImage("/items/$itemId/Images/${ImageType.PRIMARY}", placeholderHash = blurHash)
        .posterDescription(item.name)
}

fun bindItemBackdropImage(imageView: ImageView, item: FindroidItem?) {
    if (item == null) return

    val imageId = item.imageTags?.get(ImageType.BACKDROP)
    val blurHash = item.imageBlurHashes?.get(ImageType.BACKDROP)?.get(imageId)

    imageView
        .loadImage("/items/${item.id}/Images/${ImageType.BACKDROP}", placeholderHash = blurHash)
        .backdropDescription(item.name)
}

fun bindItemBackdropById(imageView: ImageView, itemId: UUID) {
    imageView.loadImage("/items/$itemId/Images/${ImageType.BACKDROP}")
}

fun bindPersonImage(imageView: ImageView, person: BaseItemPerson) {
    val blurHash = person.imageBlurHashes?.get(ImageType.PRIMARY)?.get(person.primaryImageTag)

    imageView
        .loadImage("/items/${person.id}/Images/${ImageType.PRIMARY}", placeholderId = CoreR.drawable.person_placeholder, placeholderHash = blurHash)
        .posterDescription(person.name)
}

fun bindCardItemImage(imageView: ImageView, item: FindroidItem) {
    val imageType = when (item) {
        is FindroidMovie -> ImageType.BACKDROP
        else -> ImageType.PRIMARY
    }

    val imageId = item.imageTags?.get(imageType)
    val blurHash = item.imageBlurHashes?.get(imageType)?.get(imageId)

    imageView
        .loadImage("/items/${item.id}/Images/$imageType", placeholderHash = blurHash)
        .posterDescription(item.name)
}

fun bindSeasonPoster(imageView: ImageView, seasonId: UUID) {
    imageView.loadImage("/items/$seasonId/Images/${ImageType.PRIMARY}")
}

fun bindUserImage(imageView: ImageView, user: User) {
    imageView
        .loadImage("/users/${user.id}/Images/${ImageType.PRIMARY}", placeholderId = CoreR.drawable.user_placeholder)
        .posterDescription(user.name)
}

private fun ImageView.loadImage(
    url: String,
    @DrawableRes placeholderId: Int = CoreR.color.neutral_800,
    placeholderHash: String? = null,
): View {
    val api = JellyfinApi.getInstance(context.applicationContext)

    val blurPlaceholder = BlurHashDecoder.decode(placeholderHash, 100, 150)?.toDrawable(resources)

    this.load("${api.api.baseUrl}$url") {
        crossfade(true)
        if(blurPlaceholder != null) {
            placeholder(blurPlaceholder)
            error(blurPlaceholder)
        } else {
            placeholder(placeholderId)
            error(placeholderId)
        }
    }

    return this
}

private fun View.posterDescription(name: String?) {
    contentDescription = context.resources.getString(CoreR.string.image_description_poster, name)
}

private fun View.backdropDescription(name: String?) {
    contentDescription = context.resources.getString(CoreR.string.image_description_backdrop, name)
}
