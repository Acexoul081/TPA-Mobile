package edu.bluejack20_1.gogames.rawg.repository

import android.util.Log
import edu.bluejack20_1.gogames.rawg.entity.Game
import edu.bluejack20_1.gogames.rawg.entity.GameSingle
import edu.bluejack20_1.gogames.rawg.entity.Genre
import edu.bluejack20_1.gogames.rawg.network.RawgApiResult
import edu.bluejack20_1.gogames.rawg.network.RawgData

class RemoteSource (private val service: RawgApi){
    suspend fun getGames(dates: String? = null, keyword: String? = null, genre: String?= null): RawgApiResult<RawgData<List<Game>>> {
        if (dates != null){
            return service.getListOfGames(dates = dates, ordering = "released")
        }
        else if(keyword != null){
            return service.getListOfGames(search = keyword)
        }
        else if(genre != null){
            return service.getListOfGames(genres = genre.decapitalize())
        }
        return service.getListOfGames()
    }

    suspend fun fetchGameDetails(id: String): RawgApiResult<GameSingle> {
        Log.d("inDebug", service.getDetailsOfGame(id = id).toString())
        return service.getDetailsOfGame(id = id)
    }

    suspend fun getGenre() : RawgApiResult<RawgData<List<Genre>>> {
        return service.getListOfGamesGenres()
    }


}