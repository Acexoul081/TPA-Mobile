package edu.bluejack20_1.gogames

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import edu.bluejack20_1.gogames.globalClass.PreferencesConfig
import edu.bluejack20_1.gogames.itad.api.RetrofitClient
import edu.bluejack20_1.gogames.itad.api.adapter.DealAdapter
import edu.bluejack20_1.gogames.itad.api.entity.Deal
import edu.bluejack20_1.gogames.itad.api.entity.ItadResult
import edu.bluejack20_1.gogames.profile.User
import kotlinx.android.synthetic.main.activity_promo.*
import kotlinx.android.synthetic.main.activity_promo.Navigation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class PromoActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    private val list = ArrayList<Deal>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promo)

        rvDeal.setHasFixedSize(true)
        rvDeal.layoutManager = LinearLayoutManager(this)

        RetrofitClient.instance.getDeals().enqueue(object: Callback<ItadResult>{
            override fun onResponse(
                call: Call<ItadResult>,
                response: Response<ItadResult>
            ) {
                val responseCode = response.code().toString()
                response.body()?.let { list.addAll(it.data.list) }
                val adapter = DealAdapter(list, getAct())
                rvDeal.adapter = adapter
            }

            override fun onFailure(call: Call<ItadResult>, t: Throwable) {
                Log.d("inDebug", t.toString())
                Log.d("inDebug", call.toString())
            }
        })

        toggle = ActionBarDrawerToggle(this, drawer_layoutp, R.string.open_navigation, R.string.close_navigation)
        drawer_layoutp.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        topAppBar.setNavigationOnClickListener {
//            drawer_layoutp.openDrawer(Gravity.START)
//        }

        Navigation.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_community -> moveToCommunity()
                R.id.nav_news -> moveToNews()
                R.id.nav_promo -> moveToPromo()
                R.id.profile -> moveToProfile()
                R.id.logout -> logOut()
            }
            true
        }

        val header: View = (findViewById<NavigationView>(R.id.Navigation)).getHeaderView(0)
        (header.findViewById(R.id.userName) as TextView).text = User.getInstance().getUsername()
        Glide.with(this)
            .load(User.getInstance().getImagePath())
            .circleCrop()
            .into((header.findViewById(R.id.menu_user_pict) as ImageView))


//        val intent = Intent(this, ReminderBroadcast::class.java)
//        val pendingIntent : PendingIntent = PendingIntent.getBroadcast(this, 0 , intent, 0)
//
//        val alarmManager : AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//        val calendar: Calendar = Calendar.getInstance().apply {
//            timeInMillis = System.currentTimeMillis()
//            set(Calendar.HOUR_OF_DAY, 12)
//        }
//        alarmManager.set(AlarmManager.RTC_WAKEUP,
//        System.currentTimeMillis() + 60000, pendingIntent)
//        alarmManager?.setInexactRepeating(
//            AlarmManager.RTC_WAKEUP,
//            calendar.timeInMillis,
//            AlarmManager.INTERVAL_DAY,
//            pendingIntent
//        )

    }

    private fun getAct(): AppCompatActivity{
        return this
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

    fun logOut(){
        FirebaseAuth.getInstance().signOut()
        User.instance = null
        val sharePref = PreferencesConfig(this)
        sharePref.clearSharedPreference()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun moveToProfile() {
        val fragment = ProfileFragment()
        supportFragmentManager.beginTransaction().apply {
            add(android.R.id.content, fragment)
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
}