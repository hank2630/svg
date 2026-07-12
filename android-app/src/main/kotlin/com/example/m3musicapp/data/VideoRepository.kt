package com.example.m3musicapp.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

class VideoRepository {
    private val jsonUrl = "https://raw.githubusercontent.com/hank2630/svg/main/db/m3-2026-spring.json"
    private val client = OkHttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun fetchVideos(): List<VideoData> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(jsonUrl)
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                throw Exception("HTTP error: ${response.code}")
            }

            val body = response.body?.string() ?: throw Exception("Empty response")
            val data = json.decodeFromString<List<VideoData>>(body)
            return@withContext data
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Failed to fetch videos: ${e.message}")
        }
    }
}
