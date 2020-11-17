package edu.bluejack20_1.gogames

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import edu.bluejack20_1.gogames.R.string.close_navigation
import edu.bluejack20_1.gogames.R.string.open_navigation
import edu.bluejack20_1.gogames.allCommunity.DeveloperThread.DeveloperThread
import edu.bluejack20_1.gogames.globalClass.PreferencesConfig
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header_hamburger.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        if(intent.extras != null){
            val bundle = intent.extras
            val fragment = ProfileFragment()
            fragment.arguments = bundle
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.mainFragment, fragment)
                addToBackStack(null)
                commit()
            }
        }else{
            val homeFragment = HomeFragment()
            val homeCommunity = CommunityHome()

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.mainFragment, homeFragment)
                addToBackStack(null)
                commit()
            }
        }
//        tembakUser()
        toggle = ActionBarDrawerToggle(this, drawer_layout, open_navigation, close_navigation)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        topAppBar.setNavigationOnClickListener {
            drawer_layout.openDrawer(Gravity.START)
        }

        Navigation.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_community -> moveToCommunity()
                R.id.nav_news -> moveToNews()
                R.id.nav_promo -> moveToPromo()
                R.id.profile -> moveToProfile()
            }
            true
        }
    }

    fun moveToPromo() {
        val intent = Intent(this, PromoActivity::class.java)
        startActivity(intent)
    }

    fun moveToCommunity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun moveToNews() {
        val intent = Intent(this, NewsActivity::class.java)
        startActivity(intent)
    }

    fun moveToProfile() {
        val fragment = ProfileFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainFragment, fragment)
            addToBackStack(null)
            commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    fun redirectToMainThread(id: String, title: String, desc: String, category: String){
        val fragment = DeveloperThread(id, title, desc, category)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainFragment, fragment)
            addToBackStack(null)
            commit()
        }
    }

//    private fun checkUser(){
//        if(auth.currentUser != null){
//            Toast.makeText(this, "User Log in", Toast.LENGTH_LONG).show()
//        }
//    }


//    //login
//    private fun tembakUser(){
//        val email = "test@gmail.com"
//        val password = "pass123"
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                auth.signInWithEmailAndPassword(email, password).await()
//                withContext(Dispatchers.Main) {
//                    checkUser()
//                    val uid = auth.currentUser?.uid
//                    val username = "dummy"
//                    userName.text = username
//                    if (uid != null) {
//                        sharedPreferences(uid, username)
//                    }
//                }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main){}
//                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
//            }
//        }
//    }

    private fun sharedPreferences(uid: String, username: String){
        val sharePref = PreferencesConfig(this)
        sharePref.putUser(uid, username)
        Log.d("test", sharePref.getUserID() as String)
    }
}