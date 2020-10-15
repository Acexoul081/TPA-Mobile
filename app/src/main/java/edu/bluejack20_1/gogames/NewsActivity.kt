package edu.bluejack20_1.gogames

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ekn.gruzer.rawg.network.RawgServiceApi
import edu.bluejack20_1.gogames.rawg.ui.games.GamesViewModel

class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

//        checkForAPI()

    }
//    private fun checkForAPI(){
//        val service = RawgServiceApi.create()
//        //dates=2019-09-01,2019-09-30 date format is yyyy-MM-dd
//        //need provide range start and end date to gate list of games between this dates
//        //For More info check officale documentation
//        //https://api.rawg.io/docs/#operation/games_list
//
//        val result = service.getListOfGames(dates = "2019-09-01") //RawgApiResult<RawgData<List<Game>>>
//        val data = result.data //RawgData<List<Games>> class
//        val list = data.result //  List<Game> list of games Number of results to return per page.(default 20)
//        // Page size can be specified more about all option please read official documentation
//        val next = data.next // contaned next data url page number "x"
//        Log.d("News", next)
//    }

}

//sealed class RawgApiResult<out T> {
//    data class Success<T>(val data: T?) : RawgApiResult<T>()
//    data class Failure(val statusCode: Int?) : RawgApiResult<Nothing>()
//    object NetworkError : RawgApiResult<Nothing>()
//
//    fun handleResult(response: RawgApiResult<R>) = when (response) {
//        is RawgApiResult.Success ->handleSuccess(response.data)
//        is RawgApiResult.Failure -> handleErrorState(response.statusCode)
//        is RawgApiResult.NetworkError -> handleNetworkError()
//    }
//}
