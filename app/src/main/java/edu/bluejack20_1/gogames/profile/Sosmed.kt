package edu.bluejack20_1.gogames.profile

class Sosmed {
    private lateinit var type : String
    private lateinit var link: String

    constructor(){
        this.type = ""
        this.link = ""
    }

    fun setType(type: String){
        this.type = type
    }

    fun getType() : String{
        return this.type
    }

    fun setLink(link : String){
        this.link = link
    }

    fun getLink() : String{
        return this.link
    }
}