// Kanade用のアプリケーションログ
// ChocoGeneratorのテスト用Markdown
sealed class KanadeLog: ChocoItem() {

// アプリケーションが開始された
class OnApplicationStart: KanadeLog() {
override val title: String = "application_start"
override val properties: Bundle = Bundle()
}

// 初めてKanadeを起動した
class OnFirstOpen: KanadeLog() {
override val title: String = "first_open"
override val properties: Bundle = Bundle()
}

// MainControllerFragmentが全画面表示になった
class OnMainControllerShow(
title: String,
artist: String,
isPlaying: Boolean,
): KanadeLog() {
override val title: String = "main_controller_show"
override val properties: Bundle = Bundle.apply {
putString("title", title)
putString("artist", artist)
putBoolean("is_playing", isPlaying)
}
}

// 楽曲が再生された
class OnPlay(
title: String,
artist: String,
): KanadeLog() {
override val title: String = "play"
override val properties: Bundle = Bundle.apply {
putString("title", title)
putString("artist", artist)
}
}

companion object {
fun onApplicationStart() = OnApplicationStart()
fun onFirstOpen() = OnFirstOpen()
fun onMainControllerShow(title: String,artist: String,isPlaying: Boolean) = OnMainControllerShow(title,artist,isPlaying)
fun onPlay(title: String,artist: String) = OnPlay(title,artist)
}
}
