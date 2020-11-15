package edu.bluejack20_1.gogames

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import io.branch.referral.Branch
import io.branch.referral.BranchError
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.header_hamburger.*
import org.json.JSONObject


class NewsActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        toggle = ActionBarDrawerToggle(
            this,
            drawer_layoutn,
            R.string.open_navigation,
            R.string.close_navigation
        )
        drawer_layoutn.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        topAppBar.setNavigationOnClickListener {
//            drawer_layoutn.openDrawer(Gravity.START)
//        }

        Navigation.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_community -> moveToCommunity()
                R.id.nav_news -> moveToNews()
                R.id.nav_promo -> moveToPromo()
            }
            true
        }

        val header: View = (findViewById<NavigationView>(R.id.Navigation)).getHeaderView(0)
        (header.findViewById(R.id.userName) as TextView).text = FirebaseAuth.getInstance().currentUser?.email

        Branch.sessionBuilder(this).withCallback(branchListener).withData(this.intent?.data).init()
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

    override fun onStart() {
        super.onStart()
        // Branch init
        Branch.sessionBuilder(this).withCallback(branchListener).withData(this.intent?.data).init()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        this.intent = intent
        // Branch reinit (in case Activity is already in foreground when Branch link is clicked)
        Branch.sessionBuilder(this).withCallback(branchListener).reInit()
    }

    object branchListener : Branch.BranchReferralInitListener {

        override fun onInitFinished(referringParams: JSONObject?, error: BranchError?) {
            if (error == null) {
                if (referringParams != null) {
//                    Log.d("BRANCH SDK", referringParams.optString("game", ""))
                    val gameID : String = referringParams.optString("gameID", "");
                    if (gameID == "") {
                        Log.d("Deeplink", "param ngga dapat")
                    }
                    else{
                        Log.d("Deeplink", gameID)
                    }
                }
                // Retrieve deeplink keys from 'referringParams' and evaluate the values to determine where to route the user
                // Check '+clicked_branch_link' before deciding whether to use your Branch routing logic
            } else {
                Log.d("BRANCH SDK", error.message)
            }
        }
    }


}

