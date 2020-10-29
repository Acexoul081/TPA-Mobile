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
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()

        register_button.setOnClickListener{
           performRegister()
        }

        alreadyAccount_textView.setOnClickListener{
            Log.d("RegisterActivity", "Try to show login")

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
        //validate pass numeric
//        if(pass.length < 6 && !pass.is){
//
//        }

        Log.d("MainActivity", "username : "+ username)
        Log.d("MainActivity", "email: $email")
        Log.d("MainActivity", "passs: "+pass)

        //Firebase auth
        mAuth.createUserWithEmailAndPassword(email,pass)
            .addOnCompleteListener{
                if (!it.isSuccessful) return@addOnCompleteListener

                Log.d("RegisterActivity","Success created user with uid: ${it.result?.user?.uid}")
//                uploadImageToFirebaseStorage()
                val intent = Intent(this, NewsActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener{
                Log.d("RegisterActivity", "Failed create user: ${it.message}")
                Toast.makeText(this,"Failed create user: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageToFirebaseStorage(){
        if (selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Success upload image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {it ->
                    Log.d("RegisterActivity", "File location: $it")

                    saveUserToDatabase(it.toString())
                }
            }
    }

    private fun saveUserToDatabase(profileImageUrl: String){
        val uid = FirebaseAuth.getInstance().uid?: ""
        val ref =FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, username_editText.text.toString(), profileImageUrl)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "save user to database")
            }

    }

}

class User(val uid: String, val username:String, val profileImageUrl: String)