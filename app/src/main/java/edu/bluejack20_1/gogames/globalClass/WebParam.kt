package edu.bluejack20_1.gogames.globalClass

import edu.bluejack20_1.gogames.profile.User

class WebParam {
    private lateinit var gameID : String

    companion object{
        var instance : WebParam?=null
        @JvmName("WebParam")
        fun getInstance() : WebParam {
            if(instance == null){
                instance = WebParam()
            }
            return instance as WebParam
        }
    }

    private constructor(){
        this.gameID = ""
    }

    fun setGameID(gameID: String){
        this.gameID = gameID
    }

    fun getGameID() : String{
        return this.gameID
    }
}