package edu.bluejack20_1.gogames

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack20_1.gogames.itad.api.ItadServiceApi
import edu.bluejack20_1.gogames.itad.api.RetrofitClient
import edu.bluejack20_1.gogames.itad.api.adapter.DealAdapter
import edu.bluejack20_1.gogames.itad.api.entity.Deal
import edu.bluejack20_1.gogames.itad.api.entity.ItadResult
import kotlinx.android.synthetic.main.activity_promo.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PromoActivity : AppCompatActivity() {

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
    }
}