package edu.bluejack20_1.gogames.rawg.repository

import edu.bluejack20_1.gogames.rawg.entity.GameSingle

class GameSingleRepository(private val remoteSource: RemoteSource) : Repository<GameSingle>(){

    suspend fun fetchGameDetails(gameID: String): DataState<GameSingle> {
        return handleResult(remoteSource.fetchGameDetails(gameID))
    }

}