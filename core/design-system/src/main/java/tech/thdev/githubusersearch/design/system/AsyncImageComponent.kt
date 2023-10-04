package tech.thdev.githubusersearch.design.system

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size

@Composable
internal fun GitAsyncImage(
    modifier: Modifier = Modifier,
    model: Any?,
    onSuccess: ((AsyncImagePainter.State.Success) -> Unit)? = null,
    onError: (() -> Unit)? = null,
    contentDescription: String? = null,
    imageLoader: ImageLoader = LocalContext.current.imageLoader,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    filterQuality: FilterQuality = FilterQuality.None,
) {
    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        alignment = alignment,
        contentScale = contentScale,
        filterQuality = filterQuality,
        imageLoader = imageLoader,
        onSuccess = {
            onSuccess?.invoke(it)
        },
        onError = {
            onError?.invoke()
        },
        modifier = modifier
    )
}

/**
 * coil을 이용한 AsyncImage 매핑
 */
@Composable
fun GitAsyncImage(
    modifier: Modifier = Modifier,
    imageUrl: Any?,
    size: Size = Size.ORIGINAL,
    placeholder: Int = 0,
    imageLoader: ImageLoader = LocalContext.current.imageLoader,
    onSuccess: ((AsyncImagePainter.State.Success) -> Unit)? = null,
    onError: (() -> Unit)? = null,
    contentDescription: String? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    filterQuality: FilterQuality = FilterQuality.Medium,
) {
    GitAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(data = imageUrl)
            .placeholder(placeholder)
            .crossfade(true)
            .size(size)
            .error(placeholder)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .networkCachePolicy(CachePolicy.ENABLED)
            .build(),
        imageLoader = imageLoader,
        contentDescription = contentDescription,
        alignment = alignment,
        contentScale = contentScale,
        filterQuality = filterQuality,
        onSuccess = {
            onSuccess?.invoke(it)
        },
        onError = {
            onError?.invoke()
        },
        modifier = modifier
    )
}

@Preview
@Composable
internal fun PreviewGitAsyncImage() {
    GitAsyncImage(
        imageUrl = "",
        placeholder = R.drawable.ic_collections_bookmark_black_24dp,
    )
}