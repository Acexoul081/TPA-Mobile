package edu.bluejack20_1.gogames.profile

class User{
    private lateinit var username : String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var imagePath: String
    private lateinit var description: String
    private lateinit var uid: String
    private lateinit var socmeds: List<Sosmed>
    private lateinit var genre: String

    companion object{
        var instance : User?=null
        @JvmName("Users")
        fun getInstance() : User{
            if(instance == null){
                instance = User()
            }
            return instance as User
        }
    }

    private constructor(){
        this.username = ""
        this.password = ""
        this.email = ""
        this.imagePath = ""
        this.description = ""
        this.uid = ""
        this.socmeds = emptyList()
        this.genre = ""
    }

    fun setUsername(username: String){
        this.username = username
    }

    fun getUsername() : String{
        return this.username
    }

    fun setPassword(password : String){
        this.password = password
    }

    fun getPassword() : String{
        return this.password
    }

    fun setEmail(email : String){
        this.email = email
    }

    fun getEmail() : String{
        return this.email
    }

    fun setUid(uid : String){
        this.uid = uid
    }

    fun getUid() : String{
        return this.uid
    }

    fun setImagePath(imagePath : String){
        this.imagePath = imagePath
    }

    fun getImagePath(): String{
        return this.imagePath
    }

    fun setDescription (description : String){
        this.description = description
    }

    fun getDescription(): String{
        return  this.description
    }

    fun setSocmed(socmeds : List<Sosmed>){
        this.socmeds = socmeds
    }

    fun getSocmed():List<Sosmed>{
        return this.socmeds
    }

    fun setGenre (genre : String){
        this.genre = genre
    }

    fun getGenre(): String{
        return this.genre
    }
}