package com.example.m3musicapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.m3musicapp.data.VideoData
import com.example.m3musicapp.data.VideoRepository
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class HomeUiState(
    val isLoading: Boolean = true,
    val currentVideo: VideoData? = null,
    val errorMessage: String? = null
)

class HomeViewModel(
    private val repository: VideoRepository = VideoRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    private val _countdownTime = MutableStateFlow("加載中...")
    val countdownTime: StateFlow<String> = _countdownTime

    private var youtubePlayer: YouTubePlayer? = null
    private var allVideos: List<VideoData> = emptyList()

    init {
        loadVideos()
        startCountdownTimer()
    }

    private fun loadVideos() {
        viewModelScope.launch {
            try {
                allVideos = repository.fetchVideos()
                playRandomVideo()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "無法載入影片資料: ${e.message}"
                )
            }
        }
    }

    fun playRandomVideo() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val MAX_TRIES = 20
            var picked: VideoData? = null

            repeat(MAX_TRIES) {
                val candidate = allVideos.randomOrNull()
                if (candidate != null && candidate.videoId.isNotEmpty()) {
                    picked = candidate
                    return@repeat
                }
            }

            if (picked != null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    currentVideo = picked
                )
                youtubePlayer?.cueVideo(picked!!.videoId, 0f)
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "找不到 YouTube 影片"
                )
            }
        }
    }

    fun onPlayerReady(player: YouTubePlayer) {
        youtubePlayer = player
        val currentVideo = _uiState.value.currentVideo
        if (currentVideo != null && currentVideo.videoId.isNotEmpty()) {
            player.cueVideo(currentVideo.videoId, 0f)
        }
    }

    fun playPauseVideo() {
        youtubePlayer?.let {
            // YouTube Player 的播放暫停控制
        }
    }

    private fun startCountdownTimer() {
        viewModelScope.launch {
            while (true) {
                updateCountdown()
                delay(1000)
            }
        }
    }

    private fun updateCountdown() {
        val now = LocalDateTime.now(ZoneId.of("Asia/Tokyo"))
        val endDate = LocalDateTime.of(2026, 4, 26, 16, 30, 0)
        val currentTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        val duration = java.time.Duration.between(now, endDate)
        val days = duration.toDays()
        val hours = duration.toHours() % 24
        val minutes = duration.toMinutes() % 60
        val seconds = duration.seconds % 60

        val text = if (duration.isNegative || duration.isZero) {
            "M3-2026春已結束"
        } else {
            "🕒 $currentTime | ⏱️ 還要 ${days}天 ${hours}時 ${minutes}分 ${seconds}秒"
        }

        _countdownTime.value = text
    }
}
