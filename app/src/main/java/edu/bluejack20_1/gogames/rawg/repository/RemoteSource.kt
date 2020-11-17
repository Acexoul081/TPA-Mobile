package edu.bluejack20_1.gogames.rawg.repository

import android.util.Log
import com.ekn.gruzer.rawg.entity.Game
import com.ekn.gruzer.rawg.entity.GameSingle
import com.ekn.gruzer.rawg.entity.Genre
import com.ekn.gruzer.rawg.network.RawgApiResult
import com.ekn.gruzer.rawg.network.RawgData
import com.ekn.gruzer.rawg.network.RawgServiceApi

class RemoteSource (private val service: RawgApi){
    suspend fun getGames(dates: String? = null, keyword: String? = null, genre: String?= null): RawgApiResult<RawgData<List<Game>>>{
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

    suspend fun fetchGameDetails(id: String): RawgApiResult<GameSingle>{
        Log.d("inDebug", service.getDetailsOfGame(id = id).toString())
        return service.getDetailsOfGame(id = id)
    }

    suspend fun getGenre() : RawgApiResult<RawgData<List<Genre>>>{
        return service.getListOfGamesGenres()
    }


}