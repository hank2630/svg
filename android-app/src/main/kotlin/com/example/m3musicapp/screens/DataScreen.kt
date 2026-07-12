package com.example.m3musicapp.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.m3musicapp.viewmodels.DataViewModel

@Composable
fun DataScreen(viewModel: DataViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val sortState by viewModel.sortState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF343434))
            .padding(16.dp)
    ) {
        // 標題
        Text(
            "M3-2026春資訊",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 搜尋欄位
        SearchBarComponent(
            value = searchText,
            onValueChange = { viewModel.updateSearchText(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // 資料表格
        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
            uiState.errorMessage != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(uiState.errorMessage!!, color = Color.Red, fontSize = 14.sp)
                }
            }
            else -> {
                DataTable(
                    items = uiState.filteredItems,
                    sortState = sortState,
                    onColumnSort = { columnIndex -> viewModel.toggleSort(columnIndex) },
                    modifier = Modifier.weight(1f)
                )

                // 資料統計
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "共載入 ${uiState.totalCount} 筆資料，當前顯示 ${uiState.filteredItems.size} 筆",
                    color = Color(0xFF95c7fc),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun SearchBarComponent(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        placeholder = { Text("輸入關鍵字搜尋...", color = Color.Gray) },
        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = "搜尋", tint = Color.Gray)
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "清除",
                    modifier = Modifier.clickable { onValueChange("") },
                    tint = Color.Gray
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        singleLine = true
    )
}

@Composable
fun DataTable(
    items: List<Map<String, String>>,
    sortState: Pair<Int?, String?>,
    onColumnSort: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val columns = listOf("歌手/社團名稱", "專輯名稱", "攤位", "Credits")
    val columnIndices = listOf(0, 1, 3, 6)

    Column(modifier = modifier.fillMaxWidth()) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFCFCFCF))
                .padding(8.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            columns.forEachIndexed { index, title ->
                Text(
                    title,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onColumnSort(columnIndices[index]) }
                        .padding(4.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Data rows
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(items) { item ->
                DataTableRow(item, columns)
            }

            if (items.isEmpty()) {
                item {
                    Text(
                        "無符合資料",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun DataTableRow(item: Map<String, String>, columns: List<String>) {
    val uriHandler = LocalUriHandler.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE0E0E0))
            .border(1.dp, Color.White)
            .padding(8.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        columns.forEach { column ->
            val value = item[column] ?: ""
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable(enabled = value.startsWith("http")) {
                        if (value.startsWith("http")) {
                            uriHandler.openUri(value)
                        }
                    }
            ) {
                Text(
                    value,
                    modifier = Modifier
                        .padding(4.dp),
                    fontSize = 11.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = if (value.startsWith("http")) Color.Blue else Color.Black
                )
            }
        }
    }
}
