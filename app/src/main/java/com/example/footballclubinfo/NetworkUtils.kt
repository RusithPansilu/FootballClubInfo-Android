package com.example.footballclubinfo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.BufferedInputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object NetworkUtils {

    /**
     * Requirement: Standard API Network Fetcher
     * Connects to TheSportsDB API web service and downloads the raw JSON string response.
     * Runs entirely on Dispatchers.IO to ensure the UI thread never locks up or crashes.
     */
    suspend fun fetchJsonFromServer(urlString: String): String? = withContext(Dispatchers.IO) {
        var urlConnection: HttpURLConnection? = null
        var reader: BufferedReader? = null
        try {
            val url = URL(urlString)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.connectTimeout = 15000
            urlConnection.readTimeout = 15000
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")

            val responseCode = urlConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = urlConnection.inputStream
                reader = BufferedReader(InputStreamReader(inputStream))
                val jsonResult = StringBuilder()
                var currentLine: String?

                while (reader.readLine().also { currentLine = it } != null) {
                    jsonResult.append(currentLine)
                }
                return@withContext jsonResult.toString()
            } else {
                return@withContext null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        } finally {
            urlConnection?.disconnect()
            try {
                reader?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Requirement: Manual Image Downloader (No Glide or Coil allowed)
     * Downloads an image stream directly from a URL string and transforms it into a Bitmap.
     * This is vital for rendering small logo thumbnails and t-shirt (jersey) images.
     */
    suspend fun fetchImageAsBitmap(urlString: String?): Bitmap {
        return withContext(Dispatchers.IO) {
            try {
                if (urlString.isNullOrBlank() || urlString == "null") {
                    return@withContext createMockBitmap()
                }

                val standardizedUrl = urlString.replace("\\", "/")
                val url = URL(standardizedUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connectTimeout = 15000
                connection.readTimeout = 15000
                // Add a standard User-Agent to avoid being blocked by some CDNs/Servers
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val bufferedInputStream = BufferedInputStream(inputStream)
                    val bitmap = BitmapFactory.decodeStream(bufferedInputStream)
                    bufferedInputStream.close()
                    if (bitmap != null) return@withContext bitmap
                }

                // If network connection blocks or fails, return our guaranteed local bitmap
                return@withContext createMockBitmap()
            } catch (e: Exception) {
                android.util.Log.e("NetworkUtils", "Image download failed for $urlString", e)
                return@withContext createMockBitmap()
            }
        }
    }

    // Helper to generate a solid, native placeholder icon programmatically without external files
    private fun createMockBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()

        // Draw a sports-themed green background badge
        paint.color = Color.parseColor("#4CAF50")
        canvas.drawCircle(50f, 50f, 48f, paint)

        // Draw an internal white core accent
        paint.color = Color.WHITE
        canvas.drawCircle(50f, 50f, 20f, paint)

        return bitmap
    }
}