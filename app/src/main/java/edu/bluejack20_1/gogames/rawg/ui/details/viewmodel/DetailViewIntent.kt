package edu.bluejack20_1.gogames.rawg.ui.details.viewmodel

sealed class DetailViewIntent {
    data class FetchData(val gameID: String): DetailViewIntent()
}