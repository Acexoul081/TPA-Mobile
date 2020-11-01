package edu.bluejack20_1.gogames.rawg.ui.games

import com.ekn.gruzer.rawg.entity.Game
import com.ekn.gruzer.rawg.entity.Genre

sealed class GamesViewState {
    object IsLoading : GamesViewState()
    object IsDoneLoading: GamesViewState()
    data class ShowData(val games: List<Game>?) : GamesViewState()
    data class Error(val error: String) : GamesViewState()
    data class ShowGenre(val genre: List<Genre>?) : GamesViewState()
}