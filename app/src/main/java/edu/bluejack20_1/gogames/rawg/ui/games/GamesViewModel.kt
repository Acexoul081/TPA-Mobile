package edu.bluejack20_1.gogames.rawg.ui.games

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ekn.gruzer.rawg.entity.Game
import com.ekn.gruzer.rawg.network.RawgData
import edu.bluejack20_1.gogames.rawg.repository.DataState
import edu.bluejack20_1.gogames.rawg.repository.GamesRepository
import edu.bluejack20_1.gogames.rawg.utils.currentFormated
import edu.bluejack20_1.gogames.rawg.utils.formatedDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class GamesViewModel (private val repository: GamesRepository) : ViewModel() {
    private val _viewState: MutableLiveData<GamesViewState> = MutableLiveData()

    val viewState: LiveData<GamesViewState>
        get() = _viewState

    fun perform(intent: GamesViewIntent, keyword: String? = null) = when (intent){
        is GamesViewIntent.FetchFutureRelease -> getGames(dates = getDates())
        is GamesViewIntent.SearchGames -> getGames(keyword = keyword)
    }

    private fun getGames(dates: String? = null, keyword: String? = null) {
        viewModelScope.launch (Dispatchers.Main )  {
            _viewState.value = GamesViewState.IsLoading
            performRequest(dates = dates, keyword = keyword)
        }
    }

    private fun handleResult(result: DataState<RawgData<List<Game>>>) = when (result) {
        is DataState.NetworkError -> _viewState.postValue(GamesViewState.Error(result.message))
        is DataState.Error -> _viewState.postValue(GamesViewState.Error(result.error))
        is DataState.Success -> _viewState.postValue(GamesViewState.ShowData(result.data?.result))
    }

    private suspend fun performRequest(dates: String? = null, keyword: String? = null) = withContext(Dispatchers.IO) {
        delay(5000)
        val result = repository.getGames(dates, keyword)
        Log.d("keyword", result.toString())
        withContext(Dispatchers.Main) {_viewState.value = GamesViewState.IsDoneLoading}
        handleResult(result)
    }

    private fun getDates(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, 1)
        val currentDate = Date().currentFormated()
        val nextYearDate = Date().formatedDate(calendar.time)
        return "$currentDate,$nextYearDate"
    }
}