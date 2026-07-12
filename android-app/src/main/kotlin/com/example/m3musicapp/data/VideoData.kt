package com.example.m3musicapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoData(
    @SerialName("歌手/社團名稱")
    val artist: String = "",
    @SerialName("專輯名稱")
    val albumName: String = "",
    @SerialName("XFD")
    val xfd: String? = null,
    @SerialName("攤位")
    val booth: String? = null,
    @SerialName("攤位資訊(品書)")
    val boothInfo: String? = null,
    @SerialName("特設網站")
    val specialWebsite: String? = null,
    @SerialName("Credits")
    val credits: String? = null,
    @SerialName("社群網站")
    val socialUrl: String? = null,
    @SerialName("youtube_id")
    val youtubeIdField: String? = null,
    @SerialName("youtube_url")
    val youtubeUrl: String? = null,
    @SerialName("連結")
    val url: String? = null
) {
    val videoId: String
        get() {
            if (!youtubeIdField.isNullOrEmpty()) return youtubeIdField!!
            val extracted = extractYoutubeId(youtubeUrl ?: url ?: "")
            return if (extracted.length == 11) extracted else ""
        }

    private fun extractYoutubeId(input: String): String {
        if (input.isEmpty()) return ""
        val regex = Regex("[a-zA-Z0-9_-]{11}")
        return regex.find(input)?.value ?: ""
    }
}
