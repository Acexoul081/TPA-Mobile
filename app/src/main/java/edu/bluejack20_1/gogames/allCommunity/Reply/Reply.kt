package edu.bluejack20_1.gogames.allCommunity.Reply

data class Reply(
    val id: String,
    val user_id: String,
    val userName: String,
    val description: String,
    val like: Int,
    val dislike: Int,
){
    constructor() : this ("", "", "", "", 0, 0)
}