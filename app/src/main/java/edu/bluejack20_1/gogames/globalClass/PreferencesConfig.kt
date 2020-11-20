package edu.bluejack20_1.gogames.globalClass

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.bluejack20_1.gogames.profile.Sosmed

class PreferencesConfig(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)

    fun putUser(userID: String, username: String, imagePath: String, description : String, genre: String, socmeds : List<Sosmed>, email: String){
        sharedPreferences.edit().putString("userID", userID).apply()
        sharedPreferences.edit().putString("username", username).apply()
        sharedPreferences.edit().putString("imagePath", imagePath).apply()
        sharedPreferences.edit().putString("description", description).apply()
        sharedPreferences.edit().putString("genre", genre).apply()
        sharedPreferences.edit().putString("email", email).apply()
        val gson = Gson()
        sharedPreferences.edit().putString("socmeds", gson.toJson(socmeds)).apply()
    }

    fun getUserID() : String? {
        return sharedPreferences.getString("userID", null)
    }

    fun getUsername(): String? {
        return sharedPreferences.getString("username", null)
    }

    fun getImagePath(): String? {
        return sharedPreferences.getString("imagePath", null)
    }

    fun getDescription(): String?{
        return sharedPreferences.getString("description", null)
    }

    fun getGenre(): String?{
        return sharedPreferences.getString("genre", null)
    }

    fun getEmail(): String?{
        return sharedPreferences.getString("email", null)
    }

    fun getSocmeds() : List<Sosmed>?{
        val type = object : TypeToken<List<Sosmed>>() { }.type
        return Gson().fromJson<List<Sosmed>>(sharedPreferences.getString("socmeds", null), type)
    }

    fun clearSharedPreference() : Boolean{
        return sharedPreferences.edit().clear().commit()
    }

}