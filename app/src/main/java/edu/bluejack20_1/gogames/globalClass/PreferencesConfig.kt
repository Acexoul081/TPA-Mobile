package edu.bluejack20_1.gogames.globalClass

import android.content.Context

class PreferencesConfig(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)

    fun putUser(userID: String, username: String){
        sharedPreferences.edit().putString("userID", userID).apply()
        sharedPreferences.edit().putString("username", username).apply()
    }

    fun putGenre(genre : String){
        sharedPreferences.edit().putString("genre", genre).apply()
    }

    fun getUserID() : String? {
        return sharedPreferences.getString("userID", null)
    }

    fun getUsername(): String? {
        return sharedPreferences.getString("username", null)
    }

    fun getGenre(): String?{
        return sharedPreferences.getString("genre", null)
    }

    fun clearSharedPreference() : Boolean{
        return sharedPreferences.edit().clear().commit()
    }

}