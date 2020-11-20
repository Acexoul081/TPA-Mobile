package edu.bluejack20_1.gogames

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import edu.bluejack20_1.gogames.globalClass.WebParam
import edu.bluejack20_1.gogames.profile.User
import edu.bluejack20_1.gogames.rawg.RawgApplication
import edu.bluejack20_1.gogames.rawg.di.detailview.DetailedScreenViewModelModule
import edu.bluejack20_1.gogames.rawg.ui.details.GameDetailFragment
import edu.bluejack20_1.gogames.rawg.ui.games.GamesFragment
import edu.bluejack20_1.gogames.rawg.ui.games.GamesFragmentDirections
import io.branch.referral.Branch
import io.branch.referral.BranchError
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.activity_news.Navigation
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.header_hamburger.*
import org.json.JSONObject


class NewsActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private var profile = false

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
                R.id.logout -> logOut()
                R.id.profile -> moveToProfile()
            }
            true
        }

        val header: View = (findViewById<NavigationView>(R.id.Navigation)).getHeaderView(0)
        (header.findViewById(R.id.userName) as TextView).text = User.getInstance().getUsername()
        Glide.with(this)
            .load(User.getInstance().getImagePath())
            .circleCrop()
            .into((header.findViewById(R.id.menu_user_pict) as ImageView))

//        Branch.sessionBuilder(this).withCallback(branchListener).withData(this.intent?.data).init()
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
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun moveToProfile() {
        profile = true
        invalidateOptionsMenu()
        val fragment = ProfileFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.games_fragment, fragment)
            addToBackStack(null)
            commit()
        }
    }
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if(profile == true){
            menu?.findItem(R.id.action_search)?.setVisible(false)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onStart() {
        super.onStart()
        // Branch init
        Branch.sessionBuilder(this).withCallback(branchListener).withData(this.intent?.data).init()
    }

//    override fun onResume() {
//        profile = false
//        invalidateOptionsMenu()
//        super.onResume()
//    }

    override fun onBackPressed() {
        profile = profile == false
        invalidateOptionsMenu()
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
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
                    val gameID : String = referringParams.optString("gameID", "")
                    val userID : String = referringParams.optString("userID", "")
                    if(gameID != "") {
                        Log.d("Deeplink", gameID)
                        val param = WebParam.getInstance()
                        param.setGameID(gameID)
                    }
                    else if (userID != ""){
                        val bundle = Bundle()
                        bundle.putString("userID", userID)
                        val intent = Intent(Branch.getInstance().applicationContext, MainActivity::class.java)
                        intent.putExtras(bundle)
                        Branch.getInstance().applicationContext.startActivity(intent)
                    }
                    else{
                        Log.d("Deeplink", "salah")
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

