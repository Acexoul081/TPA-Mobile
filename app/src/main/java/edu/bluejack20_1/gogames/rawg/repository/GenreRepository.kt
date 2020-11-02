package edu.bluejack20_1.gogames.rawg.repository

import android.util.Log
import com.ekn.gruzer.rawg.entity.Genre
import com.ekn.gruzer.rawg.network.RawgData

class GenreRepository (private val remote: RemoteSource): Repository<RawgData<List<Genre>>>(){
    suspend fun getGenres():  DataState<RawgData<List<Genre>>> {
        val result = remote.getGenre()
        return handleResult(result)
    }
}