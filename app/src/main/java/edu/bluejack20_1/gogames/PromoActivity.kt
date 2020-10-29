package edu.bluejack20_1.gogames

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.appcompat.app.ActionBarDrawerToggle
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
                tvDealsCode.text = responseCode
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
}