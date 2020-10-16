package edu.bluejack20_1.gogames.rawg.ui.details.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ekn.gruzer.rawg.entity.GameSingle
import edu.bluejack20_1.gogames.rawg.repository.DataState
import edu.bluejack20_1.gogames.rawg.repository.GameSingleRepository
import edu.bluejack20_1.gogames.rawg.ui.details.DetailsViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsViewModel(private val repository: GameSingleRepository ) : ViewModel() {
    private val _viewState: MutableLiveData<DetailsViewState> = MutableLiveData()

    val viewState: LiveData<DetailsViewState>
        get() = _viewState

    fun handleIntent(intent: DetailViewIntent) = when (intent){
        is DetailViewIntent.FetchData -> getDetails(intent.gameID)
    }

    private fun getDetails(gameID: String) {
        if(_viewState.value == null) {
            viewModelScope.launch(Dispatchers.Main) {
                _viewState.value = DetailsViewState.isLoading
                performRequest(gameID)
            }
        }
    }

    private suspend fun performRequest(gameID: String) = withContext(Dispatchers.IO)
    {
        val result = repository.fetchGameDetails(gameID)
        withContext(Dispatchers.Main) { _viewState.value = DetailsViewState.isDoneLoading}
        handleResult(result)
    }

    private fun handleResult(result: DataState<GameSingle>) = when (result) {
        is DataState.Success -> _viewState.postValue(DetailsViewState.ShowData(result.data))
        is DataState.NetworkError -> _viewState.postValue(DetailsViewState.Error(result.message))
        is DataState.Error -> _viewState.postValue(DetailsViewState.Error(result.error))
    }
}