package edu.bluejack20_1.gogames.itad.api.adapter

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack20_1.gogames.R
import edu.bluejack20_1.gogames.ReminderBroadcast
import edu.bluejack20_1.gogames.itad.api.entity.Deal
import edu.bluejack20_1.gogames.itad.api.entity.ItadResult
import kotlinx.android.synthetic.main.itemview_promos_rv.view.*
import retrofit2.Callback
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DealAdapter(private val list: ArrayList<Deal>, private val activity: AppCompatActivity):RecyclerView.Adapter<DealAdapter.DealViewHolder>(){

    inner class DealViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(deal: Deal){
            with(itemView){
                promo_game_title.text = deal.title
                promo_game_old_price.text = deal.price_old.toString()
                promo_game_new_price.text = deal.price_new.toString()
                price_cut.text = deal.price_cut.toString() + "%"
                if(deal.expiry != null){
                    val date = (deal.expiry as Double *1000)
                    val lon = date.toLong()
                    dayleft_textview.text = convertDate(lon)
                }

                //button function
                notify_button.setOnClickListener{
                    Log.d("notif", "masuk not")

                    createNotificationChannel()

                    val intent = Intent(context, ReminderBroadcast::class.java)
                    val pendingIntent : PendingIntent = PendingIntent.getBroadcast(context, 0 , intent, 0)

                    val alarmManager : AlarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val calendar: Calendar = Calendar.getInstance().apply {
                        timeInMillis = System.currentTimeMillis()
                        set(Calendar.HOUR_OF_DAY, 12)
                    }
                    alarmManager.set(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + 20000, pendingIntent)
                }
                cancelNotify_button.setOnClickListener{
                    Log.d("notif", "masuk can")
                    val intent = Intent(context, ReminderBroadcast::class.java)
                    val alarmManager : AlarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val pendingIntent : PendingIntent = PendingIntent.getBroadcast(context, 1 , intent, 0)

                    alarmManager.cancel(pendingIntent)
                }


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemview_promos_rv, parent, false)
        return DealViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: DealAdapter.DealViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun convertDate(milliSeconds : Long): String{
        val itemDate =  Date(milliSeconds)
        return SimpleDateFormat("dd-MM-yyyy").format(itemDate)
//        val formatter  = SimpleDateFormat(dateFormat)
//
//        val calendar = Calendar.getInstance()
//        calendar.timeInMillis = milliSeconds
//        return formatter.format(calendar.time)
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name : CharSequence  =  "PromoReminderChannel"
            val description : String = "Channel for Promo Reminder"
            val importance : Int = NotificationManager.IMPORTANCE_DEFAULT
            val channel : NotificationChannel = NotificationChannel("notifyPromo", name, importance)
            channel.description = description

            val notificationManager : NotificationManager? = activity.getSystemService(
                NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }
}