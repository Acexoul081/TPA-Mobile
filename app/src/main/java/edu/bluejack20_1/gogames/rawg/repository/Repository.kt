package edu.bluejack20_1.gogames.rawg.repository

import edu.bluejack20_1.gogames.rawg.network.RawgApiResult

abstract class Repository <R> {
    fun handleResult(response: RawgApiResult<R>) = when (response){
        is RawgApiResult.Success ->handleSuccess(response.data)
        is RawgApiResult.Failure -> handleErrorState(response.toString())
        is RawgApiResult.NetworkError -> handleNetworkError(response.toString())
    }

    fun handleNetworkError(message: String): DataState<R>{
        return DataState.NetworkError(message)
    }

    fun handleErrorState(error:String):DataState<R>{
        return DataState.Error(error)
    }

    fun handleSuccess(data: R?): DataState<R>{
        return DataState.Success(data)
    }
}