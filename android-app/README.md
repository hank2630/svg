# M3 Music App - Android

基於 home.html 和 2026-spring.html 的 Android 應用

## 功能

### 首頁 (Home)
- 🎵 隨機播放 YouTube 影片
- ⏱️ 倒計時顯示
- 📝 影片資訊展示
- 💬 可展開的 Credits

### 資料頁 (Data)
- 🔍 模糊搜尋
- 📊 欄位篩選
- ↕️ 排序功能
- 📱 響應式布局

## 技術棧

- **語言**: Kotlin
- **UI框架**: Jetpack Compose
- **網絡**: OkHttp + Kotlinx Serialization
- **異步**: Coroutines
- **影片播放**: android-youtube-player

## 專案結構

```
android-app/
├── src/main/kotlin/com/example/m3musicapp/
│   ├── MainActivity.kt                 # 主Activity
│   ├── screens/
│   │   ├── HomeScreen.kt              # 首頁屏幕
│   │   └── DataScreen.kt              # 資料屏幕
│   ├── viewmodels/
│   │   ├── HomeViewModel.kt           # 首頁ViewModel
│   │   └── DataViewModel.kt           # 資料ViewModel
│   ├── data/
│   │   ├── VideoData.kt               # 數據模型
│   │   └── VideoRepository.kt         # 數據倉庫
│   └── ui/theme/
│       └── Theme.kt                   # 主題配置
└── build.gradle.kts                   # 項目配置
```

## 快速開始

### 環境要求
- Android Studio Jellyfish 或更新版本
- Android SDK 34
- Kotlin 1.9+
- Java 11+

### 安裝步驟

1. 克隆倉庫
```bash
git clone https://github.com/hank2630/svg.git
cd android-app
```

2. 在 Android Studio 打開項目

3. 同步 Gradle

4. 運行應用
```bash
./gradlew run
```

## API 數據源

應用從以下 URL 加載數據：
```
https://raw.githubusercontent.com/hank2630/svg/main/db/m3-2026-spring.json
```

## 屏幕截圖

### 首頁
- YouTube 播放器
- 倒計時條
- 影片資訊面板

### 資料頁
- 搜尋欄
- 數據表格
- 排序和篩選

## 開發說明

### 添加新依賴

編輯 `build.gradle.kts`：

```kotlin
dependencies {
    implementation("com.example:library:1.0.0")
}
```

### 主題定制

在 `ui/theme/Theme.kt` 中修改顏色和排版

### 屏幕導航

屏幕之間的導航在 `MainActivity.kt` 中配置

## 許可證

MIT

## 貢獻

歡迎提交 Issue 和 Pull Request！
