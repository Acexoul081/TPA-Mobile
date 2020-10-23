package edu.bluejack20_1.gogames.rawg.repository

import com.ekn.gruzer.rawg.entity.Game
import com.ekn.gruzer.rawg.network.RawgData

class GamesRepository (private val remote: RemoteSource): Repository<RawgData<List<Game>>>(){
    suspend fun getGames(dates: String? = null, keyword: String?=null): DataState<RawgData<List<Game>>>{
        val result = remote.getGames(dates, keyword)
        return handleResult(result)
    }
}