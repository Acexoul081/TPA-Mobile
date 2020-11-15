package edu.bluejack20_1.gogames

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import edu.bluejack20_1.gogames.profile.ExampleDialog
import edu.bluejack20_1.gogames.profile.UpdateProfileActivity
import edu.bluejack20_1.gogames.profile.User
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.ByteArrayOutputStream


class ProfileFragment : Fragment() {

    private lateinit var imageUri : Uri
    private val REQUEST_IMAGE_CAPTURE = 100
    private val DEFAULT_IMAGE_URL = "https://picsum.photos/200"

    private val currentUser = FirebaseAuth.getInstance().currentUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = User.getInstance()
        text_view_name.text = user.getUsername()
        text_email.text = user.getEmail()
        text_view_description.text = user.getDescription()
        Glide.with(this)
            .load(user.getImagePath())
            .into(image_view)

//            Glide.with(this)
//                .load(currentUser.photoUrl)
//                .into(image_view)

//            if(user.isEmailVerified){
//                text_not_verified.visibility = View.VISIBLE
//            }else{
//                text_not_verified.visibility = View.INVISIBLE
//            }



        image_view.setOnClickListener{
            takePictureIntent()
        }

//        button_save.setOnClickListener{
//            val photo = when{
//                ::imageUri.isInitialized -> imageUri
//                currentUser?.photoUrl == null -> Uri.parse(DEFAULT_IMAGE_URL)
//                else -> currentUser.photoUrl
//            }
//            val name = edit_text_name.text.toString().trim()
//
//            if(name.isEmpty()){
//                edit_text_name.error = "name required"
//                edit_text_name.requestFocus()
//                return@setOnClickListener
//            }
//
//            val updates = UserProfileChangeRequest.Builder()
//                .setDisplayName(name)
//                .setPhotoUri(photo)
//                .build()
//            progressbar.visibility = View.VISIBLE
//
//            currentUser?.updateProfile(updates)
//                ?.addOnCompleteListener{task ->
//                    progressbar.visibility = View.INVISIBLE
//                    if(task.isSuccessful){
////                        Toast.makeText(context?, "Profile Updated")
//                    }else{
//                        Log.d("error update", task.exception?.message!!)
//                    }
//                }
//        }

        text_not_verified.setOnClickListener{
            currentUser?.sendEmailVerification()
                ?.addOnCompleteListener{
                    if (it.isSuccessful){
                        //toast email sent
                    }else{
                        //toast exception
                    }
                }
        }

        updateProfile_btn.setOnClickListener{
            startActivity(Intent(this.context, UpdateProfileActivity::class.java))
        }
    }

    private fun takePictureIntent(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {pictureIntent ->
            pictureIntent.resolveActivity(activity?.packageManager!!)?.also {
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            val imageBitmap = data?.extras?.get("data") as Bitmap

            uploadImageAndSaveUri(imageBitmap)
        }
    }

    private fun uploadImageAndSaveUri(bitmap: Bitmap){
        val baos = ByteArrayOutputStream()
        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("pics/${FirebaseAuth.getInstance().currentUser?.uid}")
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()

        val upload = storageRef.putBytes(image)
        progressbar_pic.visibility = View.VISIBLE
        upload.addOnCompleteListener{uploadTask ->
            progressbar_pic.visibility = View.INVISIBLE
            if(uploadTask.isSuccessful){
                storageRef.downloadUrl.addOnCompleteListener{urlTask ->
                    urlTask.result?.let{
                        imageUri= it

                        image_view.setImageBitmap(bitmap)
                    }
                }
            }else{
                uploadTask.exception?.let{
                    Log.d("error upload", it.message!!)
                }
            }
        }
    }
}