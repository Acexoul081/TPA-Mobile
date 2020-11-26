package edu.bluejack20_1.gogames.rawg.repository

import edu.bluejack20_1.gogames.rawg.entity.Game
import edu.bluejack20_1.gogames.rawg.network.RawgData

class GamesRepository (private val remote: RemoteSource): Repository<RawgData<List<Game>>>(){
    suspend fun getGames(dates: String? = null, keyword: String?=null, genre: String? = null): DataState<RawgData<List<Game>>>{
        val result = remote.getGames(dates, keyword , genre)
        return handleResult(result)
    }

}

