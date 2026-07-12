package com.example.m3musicapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.m3musicapp.data.VideoData
import com.example.m3musicapp.data.VideoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class DataUiState(
    val isLoading: Boolean = true,
    val filteredItems: List<Map<String, String>> = emptyList(),
    val totalCount: Int = 0,
    val errorMessage: String? = null
)

class DataViewModel(
    private val repository: VideoRepository = VideoRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DataUiState())
    val uiState: StateFlow<DataUiState> = _uiState

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _sortState = MutableStateFlow<Pair<Int?, String?>>(null to null)
    val sortState: StateFlow<Pair<Int?, String?>> = _sortState

    private var allVideos: List<VideoData> = emptyList()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                allVideos = repository.fetchVideos()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    totalCount = allVideos.size
                )
                applyFiltersAndSort()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "無法載入資料: ${e.message}"
                )
            }
        }
    }

    fun updateSearchText(text: String) {
        _searchText.value = text
        applyFiltersAndSort()
    }

    fun toggleSort(columnIndex: Int) {
        val (currentCol, currentOrder) = _sortState.value
        val newOrder = when {
            currentCol != columnIndex -> "asc"
            currentOrder == "asc" -> "desc"
            else -> null
        }
        _sortState.value = if (newOrder == null) null to null else columnIndex to newOrder
        applyFiltersAndSort()
    }

    private fun applyFiltersAndSort() {
        val filtered = allVideos.filter { video ->
            _searchText.value.isEmpty() ||
                    video.artist.contains(_searchText.value, ignoreCase = true) ||
                    video.albumName.contains(_searchText.value, ignoreCase = true) ||
                    (video.booth?.contains(_searchText.value, ignoreCase = true) ?: false) ||
                    (video.credits?.contains(_searchText.value, ignoreCase = true) ?: false)
        }.let { list ->
            val (sortCol, sortOrder) = _sortState.value
            if (sortCol != null && sortOrder != null) {
                when (sortCol) {
                    0 -> {
                        if (sortOrder == "asc") list.sortedBy { it.artist }
                        else list.sortedByDescending { it.artist }
                    }
                    1 -> {
                        if (sortOrder == "asc") list.sortedBy { it.albumName }
                        else list.sortedByDescending { it.albumName }
                    }
                    3 -> {
                        if (sortOrder == "asc") list.sortedBy { it.booth ?: "" }
                        else list.sortedByDescending { it.booth ?: "" }
                    }
                    6 -> {
                        if (sortOrder == "asc") list.sortedBy { it.credits ?: "" }
                        else list.sortedByDescending { it.credits ?: "" }
                    }
                    else -> list
                }
            } else {
                list
            }
        }

        val mapped = filtered.map {
            mapOf(
                "歌手/社團名稱" to it.artist,
                "專輯名稱" to it.albumName,
                "攤位" to (it.booth ?: ""),
                "Credits" to (it.credits ?: "")
            )
        }

        _uiState.value = _uiState.value.copy(filteredItems = mapped)
    }
}
