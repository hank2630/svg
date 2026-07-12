package com.example.m3musicapp.screens

import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.m3musicapp.viewmodels.HomeViewModel
import com.example.m3musicapp.viewmodels.HomeUiState
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val countdownTime by viewModel.countdownTime.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF343434))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 倒計時條
            CountdownBar(countdownTime, modifier = Modifier.fillMaxWidth())

            // 主容器
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // 左邊：資訊
                InfoPanel(
                    uiState = uiState,
                    modifier = Modifier.weight(0.35f)
                )

                // 右邊：影片播放器
                VideoPlayerPanel(
                    uiState = uiState,
                    viewModel = viewModel,
                    modifier = Modifier.weight(0.65f)
                )
            }

            // 隨機播放按鈕
            Button(
                onClick = { viewModel.playRandomVideo() },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF667eea))
            ) {
                Text("隨機播放", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun InfoPanel(
    uiState: HomeUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(Color(0xFF2a2a2a), shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        when {
            uiState.isLoading -> {
                Text(
                    "隨機抽選影片中...",
                    color = Color(0xFF666666),
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp
                )
            }
            uiState.currentVideo != null -> {
                val video = uiState.currentVideo!!

                // 社群連結
                if (!video.socialUrl.isNullOrEmpty()) {
                    Text(
                        "📱 社群連結",
                        modifier = Modifier.clickable { },
                        color = Color(0xFF95c7fc),
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // 歌手名稱
                Text(
                    video.artist,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 專輯名稱
                Text(
                    "【專輯名稱】",
                    fontSize = 12.sp,
                    color = Color(0xFF95c7fc),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    video.albumName,
                    fontSize = 14.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Credits (可展開)
                ExpandableCredits(video.credits ?: "")
            }
            else -> {
                Text(
                    uiState.errorMessage ?: "無法載入影片",
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun VideoPlayerPanel(
    uiState: HomeUiState,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(Color.Black, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
            .clickable { viewModel.playPauseVideo() }
    ) {
        if (uiState.currentVideo?.videoId?.isNotEmpty() == true) {
            YouTubePlayerComposable(
                videoId = uiState.currentVideo!!.videoId,
                onPlayerReady = { viewModel.onPlayerReady(it) },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text(
                "正在加載影片播放器...",
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun YouTubePlayerComposable(
    videoId: String,
    onPlayerReady: (YouTubePlayer) -> Unit,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            YouTubePlayerView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        onPlayerReady(youTubePlayer)
                        if (videoId.isNotEmpty()) {
                            youTubePlayer.cueVideo(videoId, 0f)
                        }
                    }
                })
            }
        },
        update = { view ->
            if (videoId.isNotEmpty()) {
                view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.cueVideo(videoId, 0f)
                    }
                })
            }
        }
    )
}

@Composable
fun ExpandableCredits(credits: String) {
    var isExpanded by remember { mutableStateOf(false) }

    Column {
        Text(
            if (isExpanded) "▲Credits" else "▼Credits",
            modifier = Modifier.clickable { isExpanded = !isExpanded },
            color = Color(0xFF95c7fc),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        if (isExpanded && credits.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                credits,
                fontSize = 11.sp,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        } else if (isExpanded) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "—",
                fontSize = 11.sp,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun CountdownBar(countdownText: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(Color(0xFF555555))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            countdownText,
            color = Color(0xFF7FFF7F),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
        )
    }
}
