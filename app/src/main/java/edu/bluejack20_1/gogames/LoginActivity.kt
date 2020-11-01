package edu.bluejack20_1.gogames

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.bluejack20_1.gogames.globalClass.PreferencesConfig
import kotlinx.android.synthetic.main.activiy_login.*

class LoginActivity : AppCompatActivity(){

    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var gso: GoogleSignInOptions
    val RC_SIGN_IN: Int = 1
    lateinit var signOut: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activiy_login)
        auth = Firebase.auth
        login_button.setOnClickListener{
            val email = email_editText.text.toString()
            val password = password_editText.text.toString()

            Log.d("loginActivity", "login with $email $password")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){
                    if(it.isSuccessful){
                        val pref = PreferencesConfig(this)
                        val uid = FirebaseAuth.getInstance().currentUser?.uid
                        val email = FirebaseAuth.getInstance().currentUser?.email

                        if (uid != null) {
                            if (email != null) {
                                pref.putUser(uid, email)
                            }
                        }
                        val intent = Intent(this, NewsActivity::class.java)
                        startActivity(intent)
                    }else{
                        it.exception?.message?.let{
                            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }

        backRegister_textView.setOnClickListener{
            finish()
        }

        val signIn = findViewById<View>(R.id.google_signIn) as SignInButton
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        signOut = findViewById<View>(R.id.signOut_btn) as Button
        signOut.visibility = View.INVISIBLE
        signIn.setOnClickListener{
            view: View? -> signInGoogle()
        }
    }

    private fun signInGoogle(){
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)!!
            if (account != null) {
                firebaseAuthWithGoogle(account.idToken!!)
            }
        }catch (e: ApiException){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }
    private fun updateUI(account: FirebaseUser?){
        val dispTxt = findViewById<View>(R.id.testText) as TextView
        dispTxt.text = account?.displayName
        signOut.visibility = View.VISIBLE
        signOut.setOnClickListener{
            view: View? -> mGoogleSignInClient.signOut().addOnCompleteListener{
            task: Task<Void> -> dispTxt.text = " "
            signOut.visibility = View.INVISIBLE
        }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("success", "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                    val intent = Intent(this, NewsActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.d("error", "signInWithCredential:failure", task.exception)
                    // ...
                }
            }
    }

}