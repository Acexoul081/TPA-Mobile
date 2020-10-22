package edu.bluejack20_1.gogames.rawg.repository

sealed class DataState<T> {
    data class Error<T>(val error:String): DataState<T>()
    data class NetworkError<T>(val message:String): DataState<T>()
    data class Success<T>(val data: T?): DataState<T>()
}