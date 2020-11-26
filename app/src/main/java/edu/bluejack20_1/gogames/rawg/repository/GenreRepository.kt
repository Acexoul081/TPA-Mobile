package edu.bluejack20_1.gogames.rawg.repository

import edu.bluejack20_1.gogames.rawg.entity.Genre
import edu.bluejack20_1.gogames.rawg.network.RawgData

class GenreRepository (private val remote: RemoteSource): Repository<RawgData<List<Genre>>>(){
    suspend fun getGenres():  DataState<RawgData<List<Genre>>> {
        val result = remote.getGenre()
        return handleResult(result)
    }
}