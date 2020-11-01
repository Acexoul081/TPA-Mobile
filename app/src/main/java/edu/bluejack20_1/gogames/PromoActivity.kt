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
import android.view.Gravity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack20_1.gogames.itad.api.ItadServiceApi
import edu.bluejack20_1.gogames.itad.api.RetrofitClient
import edu.bluejack20_1.gogames.itad.api.adapter.DealAdapter
import edu.bluejack20_1.gogames.itad.api.entity.Deal
import edu.bluejack20_1.gogames.itad.api.entity.ItadResult
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_news.*
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
                val adapter = DealAdapter(list)
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
            }
            true
        }

        createNotificationChannel()

        val intent = Intent(this, ReminderBroadcast::class.java)
        val pendingIntent : PendingIntent = PendingIntent.getBroadcast(this, 0 , intent, 0)

        val alarmManager : AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 12)
        }
        alarmManager?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
//        alarmManager.set(AlarmManager.RTC_WAKEUP,
//        System.currentTimeMillis() + 60000, pendingIntent)
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

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name : CharSequence  =  "PromoReminderChannel"
            val description : String = "Channel for Promo Reminder"
            val importance : Int = NotificationManager.IMPORTANCE_DEFAULT
            val channel : NotificationChannel = NotificationChannel("notifyPromo", name, importance)
            channel.description = description

            val notificationManager : NotificationManager? = getSystemService(
                NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }
}