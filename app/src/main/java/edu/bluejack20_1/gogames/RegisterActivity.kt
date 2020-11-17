package edu.bluejack20_1.gogames

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import edu.bluejack20_1.gogames.profile.User
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        database = Firebase.database.reference

        mAuth = FirebaseAuth.getInstance()
        val btnRegister = findViewById<Button>(R.id.button_register)
        btnRegister.setOnClickListener{
           performRegister()
        }

        alreadyAccount_textView.setOnClickListener{

            //launch the login activity somehow
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        skip_textView.setOnClickListener{
            val intent = Intent(this, NewsActivity::class.java)
            startActivity(intent)
        }
        photo_textView.setOnClickListener{
            Log.d("RegisterActivity", "show photo selector")

            val intent= Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

    }
    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            Log.d("RegisterActivity", "Photo Selected")

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            val bitmapDrawable = BitmapDrawable(bitmap);
            photo_textView.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun performRegister(){
        val username = username_editText.text.toString()
        val email = email_editText.text.toString()
        val pass = password_editText.text.toString()

        if(email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this,"Please enter text in email/pw", Toast.LENGTH_SHORT).show()
            return
        }
        //validate email unique
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"valid email required", Toast.LENGTH_SHORT).show()
            email_editText.requestFocus()
            return
        }
//        validate pass alphanumeric
        if(pass.length < 6){
            Toast.makeText(this, "Password to short", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isAlphaNumeric(pass)){
            Toast.makeText(this, "Pass must be alphanumeric", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!username.matches("^[a-zA-Z0-9]*$".toRegex())){
            Toast.makeText(this, "Username cannot contain special characters", Toast.LENGTH_SHORT).show();
            return;
        }
        progressbar_register.visibility = View.VISIBLE
        username_editText.isEnabled = false
        email_editText.isEnabled = false
        password_editText.isEnabled = false
        //Firebase auth
        mAuth.createUserWithEmailAndPassword(email,pass)
            .addOnCompleteListener{
                progressbar_register.visibility = View.INVISIBLE
                Log.d("RegisterActivity","Success created user with uid: ${it.result?.user?.uid}")
                uploadImageToFirebaseStorage()
                val intent = Intent(this, NewsActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener{
                progressbar_register.visibility = View.INVISIBLE
                username_editText.isEnabled = true
                email_editText.isEnabled = true
                password_editText.isEnabled = true
                Log.d("RegisterActivity", "Failed create user: ${it.message}")
                Toast.makeText(this,"Failed create user: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return
        val filename = FirebaseAuth.getInstance().uid?.toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Success upload image: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {it ->
                    saveUserToDatabase(it.toString())
                }
            }
    }

    private fun saveUserToDatabase(profileImageUrl: String){
        val uid = FirebaseAuth.getInstance().uid?: ""
        val email = FirebaseAuth.getInstance().currentUser?.email

        val user = User.getInstance()
        user.setUid(uid)
        if (email != null) {
            user.setEmail(email)
        }
        user.setImagePath(profileImageUrl)
        user.setPassword(password_editText.text.toString())
        user.setUsername(username_editText.text.toString())
        database.child("Users").child(uid).setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "save user to database")
            }

    }

    private fun isAlphaNumeric(text : String) : Boolean{
        var alphaBet : Boolean = false
        var isNum : Boolean = false
        for(c in text){
            if(c in 'A'..'Z' || c in 'a'..'z'){
                alphaBet = true
            }
            else if (c in '0'..'9'){
                isNum = true
            }
            else{
                return false
            }
        }
        return (alphaBet && isNum)
    }

}
