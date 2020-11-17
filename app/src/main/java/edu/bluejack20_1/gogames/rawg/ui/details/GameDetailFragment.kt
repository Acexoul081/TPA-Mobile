package edu.bluejack20_1.gogames.rawg.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.ekn.gruzer.rawg.entity.GameSingle
import com.google.firebase.database.*
import edu.bluejack20_1.gogames.R
import edu.bluejack20_1.gogames.profile.Sosmed
import edu.bluejack20_1.gogames.profile.User
import edu.bluejack20_1.gogames.rawg.RawgApplication
import edu.bluejack20_1.gogames.rawg.di.detailview.DetailedScreenViewModelModule
import edu.bluejack20_1.gogames.rawg.ui.details.viewmodel.DetailViewIntent
import edu.bluejack20_1.gogames.rawg.ui.details.viewmodel.DetailsViewModel
import edu.bluejack20_1.gogames.rawg.utils.LoadImage
import kotlinx.android.synthetic.main.fragment_game_detail.*
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GameDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    @Inject
    lateinit var viewModel: DetailsViewModel

    val args by navArgs<GameDetailFragmentArgs>()
    private lateinit var gameID: String
    private lateinit var gameTitle: String
    private lateinit var gameImage: String
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        gameID = args.gameID
        gameTitle = args.gameTitle
        gameImage = args.gameImage

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context?.let {
            (it.applicationContext as RawgApplication).appComponent.provideDetailScreenComponent(
                DetailedScreenViewModelModule(this)
            ).inject(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_detail, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = FirebaseDatabase.getInstance().reference.child("games").child(gameID)
        share_btn.setOnClickListener{
            val message = "70960.app.link/?gameID=$gameID"
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, message)
            intent.type = "text/plain"

            startActivity(Intent.createChooser(intent, "Share to : "))
        }
        viewModel.viewState.observe(viewLifecycleOwner, Observer { render(it) })
        viewModel.handleIntent(DetailViewIntent.FetchData(gameID))

        var listLike : MutableList<String> = mutableListOf<String>()
        var listDislike : MutableList<String> = mutableListOf<String>()
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listLike = mutableListOf<String>()
                listDislike = mutableListOf<String>()
                snapshot.child("like").children.forEach{
                    listLike.add(it.getValue() as String)
                }
                snapshot.child("dislike").children.forEach{
                    listDislike.add(it.getValue() as String)
                }
                btn_dislike_news.text = listDislike.size.toString()
                btn_like_news.text = listLike.size.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        val user = User.getInstance()
        btn_like_news.setOnClickListener{
            if(user != null){
                listLike.forEach{
                    if(it == user.getUid()){
                        return@setOnClickListener
                    }
                }
                listLike.add(user.getUid())
                listDislike.remove(user.getUid())
                database.child("like").setValue(listLike)
                database.child("dislike").setValue(listDislike)
            }
        }
        btn_dislike_news.setOnClickListener{
            if(user != null){
                listDislike.forEach{
                    if(it == user.getUid()){
                        return@setOnClickListener
                    }
                }
                listDislike.add(user.getUid())
                listLike.remove(user.getUid())
                database.child("like").setValue(listLike)
                database.child("dislike").setValue(listDislike)
            }

        }
    }

    private fun render (viewState: DetailsViewState) = when (viewState) {
        is DetailsViewState.isLoading -> isLoading()
        is DetailsViewState.isDoneLoading -> loadingIsDone()
        is DetailsViewState.ShowData -> show(viewState.game)
        is DetailsViewState.Error -> showError(viewState.error)
    }

    private fun isLoading() {
        progressbar_detail.visibility = View.VISIBLE
    }

    private fun loadingIsDone() {
        progressbar_detail.visibility = View.INVISIBLE
    }

    private fun showError(error: String) {
        Log.d("errorDetail", error)
    }

    private fun show(gameSingle: GameSingle?) {
        gameSingle?.let {
            Log.d("deepLink", it.description)
            details_description_txt.text = Html.fromHtml(it.description)
            details_game_title_txt.text = it.name
            metacritic_textview.text = "Metacritic : "+ it.metacritic.toString()
            added_textview.text = "Released : "+ it.released

            if(it.backgroundImageURL!=null){
                LoadImage(details_image, it.backgroundImageURL)
            }
            else if (it.backgroundImageAdditionalURL!=null){
                LoadImage(details_image, it.backgroundImageAdditionalURL)
            }

        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GameDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GameDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}