package edu.bluejack20_1.gogames.rawg.ui.details

import edu.bluejack20_1.gogames.rawg.entity.GameSingle

sealed class DetailsViewState {
    object isLoading: DetailsViewState()
    object isDoneLoading: DetailsViewState()
    data class ShowData(val game: GameSingle?): DetailsViewState()
    data class Error(val error:String): DetailsViewState()
}