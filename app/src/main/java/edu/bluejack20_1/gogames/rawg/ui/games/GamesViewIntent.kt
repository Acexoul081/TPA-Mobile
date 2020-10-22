package edu.bluejack20_1.gogames.rawg.ui.games

sealed class GamesViewIntent {
    object FetchFutureRelease:GamesViewIntent()
    object SearchGames:GamesViewIntent()
}