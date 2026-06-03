package com.example.footballclubinfo

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

/**
 * QUESTION 6: Native Custom Image Loader Component
 * Decodes standard streams safely using native BitmapFactory channels.
 */
@Composable
fun AsyncImage(
    url: String?,
    modifier: Modifier = Modifier
) {
    var bitmapState by remember(url) { mutableStateOf<Bitmap?>(null) }
    var isLoadingState by remember(url) { mutableStateOf(true) }

    LaunchedEffect(url) {
        isLoadingState = true
        android.util.Log.d("AsyncImage", "Loading image: $url")
        val downloadedBitmap = NetworkUtils.fetchImageAsBitmap(url)
        bitmapState = downloadedBitmap
        isLoadingState = false
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (isLoadingState) {
            CircularProgressIndicator(modifier = Modifier.size(16.dp))
        } else {
            bitmapState?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Club Image Badge",
                    modifier = modifier
                )
            }
        }
    }
}