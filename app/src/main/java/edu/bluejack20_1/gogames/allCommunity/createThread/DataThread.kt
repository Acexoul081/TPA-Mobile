package edu.bluejack20_1.gogames.allCommunity.createThread

data class DataThread(
    val threadID: String,
    var userID: String,
    val username: String,
    val title: String,
    val description: String,
    val category: String,
    val like: Int,
    val dislike: Int,
    val view: Int,
    val date: String,
    val totalReplied: Int
){
    constructor() : this ("","","","","","",0,0, 0, "", 0)
}