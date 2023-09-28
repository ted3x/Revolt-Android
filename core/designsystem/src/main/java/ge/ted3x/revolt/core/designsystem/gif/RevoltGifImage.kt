package ge.ted3x.revolt.core.designsystem.gif

import android.os.Build.VERSION.SDK_INT
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.ComponentRegistry
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder

@Composable
fun RevoltGifImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    transform: (AsyncImagePainter.State) -> AsyncImagePainter.State = AsyncImagePainter.DefaultTransform,
    onState: ((AsyncImagePainter.State) -> Unit)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality
) {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components(fun ComponentRegistry.Builder.() {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        })
        .build()
    AsyncImage(
        model,
        contentDescription,
        imageLoader,
        modifier,
        transform,
        onState,
        alignment,
        contentScale,
        alpha,
        colorFilter,
        filterQuality
    )
}