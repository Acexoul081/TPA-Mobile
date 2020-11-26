package edu.bluejack20_1.gogames.rawg.ui.games

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack20_1.gogames.rawg.entity.Game
import edu.bluejack20_1.gogames.rawg.entity.Genre
import com.google.firebase.database.FirebaseDatabase
import edu.bluejack20_1.gogames.R
import edu.bluejack20_1.gogames.globalClass.PreferencesConfig
import edu.bluejack20_1.gogames.globalClass.WebParam
import edu.bluejack20_1.gogames.profile.User
import edu.bluejack20_1.gogames.rawg.RawgApplication
import edu.bluejack20_1.gogames.rawg.di.gameview.GameScreenViewModelModule
import kotlinx.android.synthetic.main.fragment_games.*
import javax.inject.Inject
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**-
 * A simple [Fragment] subclass.
 * Use the [GamesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class GamesFragment : Fragment() , GamesAdapter.RecyclerViewItemClickLister, AdapterView.OnItemSelectedListener {

    private var searchView: SearchView? = null
    private var queryTextListener: SearchView.OnQueryTextListener? = null
    private lateinit var spinner: Spinner
    private lateinit var user : User
    @Inject
    lateinit var viewModel: GamesViewModel
    private val gamesAdapter: GamesAdapter by lazy {
        GamesAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        user = User.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_games, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context?.let {
            (it.applicationContext as RawgApplication).appComponent.provideGameScreenComponent(
                GameScreenViewModelModule(this)
            ).inject(this)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        main_games_rv.apply {
            layoutManager = LinearLayoutManager(this@GamesFragment.context)
            adapter = gamesAdapter
    }

        viewModel.viewState.observe(viewLifecycleOwner, Observer { render(it) })
        Log.d("Userrrr", User.getInstance().toString())
        if(user != null && user.getGenre()  != ""){
            viewModel.perform(GamesViewIntent.FetchGamesByGenre, genre = user.getGenre())
        }else{
            viewModel.perform(GamesViewIntent.FetchFutureRelease)
        }
        viewModel.performGenre()
    }

    private fun render(viewState: GamesViewState) = when (viewState) {
        is GamesViewState.IsLoading -> loadingInProgress()
        is GamesViewState.IsDoneLoading -> loadingIsDone()
        is GamesViewState.ShowData -> displayGames(viewState.games)
        is GamesViewState.Error -> showError(viewState.error)
        is GamesViewState.ShowGenre -> displayGenre(viewState.genre)
    }

    private fun showError(error: String){
        Log.d("masalah", error)
    }

    private fun loadingInProgress() {
        shimmer_frame_layout.startShimmerAnimation()
        main_games_pb.visibility = View.VISIBLE
    }

    private fun loadingIsDone(){
        shimmer_frame_layout.stopShimmerAnimation()
        shimmer_frame_layout.visibility = View.GONE
        main_games_pb.visibility = View.GONE
    }

    private fun displayGames(games: List<Game>?) {
        val param = WebParam.getInstance()
        if(param.getGameID() != ""){
            val direction: NavDirections =
                GamesFragmentDirections.actionGamesFragmentToGameDetailFragment(
                    param.getGameID(),
                    "",
                    ""
                )
            param.setGameID("")
            findNavController().navigate(direction)
        }else{
            games?.let {
                gamesAdapter.updateList(it)
            }
        }

    }

    private fun displayGenre(genres: List<Genre>?){
        val arrayList = ArrayList<String>(genres?.map { it.name })
        arrayList.add(0, "Category")
        genre_spinner.adapter = ArrayAdapter<String>(activity as Context, R.layout.style_spinner, arrayList)
        genre_spinner.onItemSelectedListener = this
        arrayList.forEachIndexed{ index, element ->
            if(element == user.getGenre()){
                genre_spinner.setSelection(index)
            }
        }
    }

    override fun onRecycleViewItemSelected(item: Game) {
        val direction: NavDirections =
            GamesFragmentDirections.actionGamesFragmentToGameDetailFragment(
                item.id.toString(),
                item.name,
                item.background_image
            )
        findNavController().navigate(direction)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.top_app_bar, menu)
        val menuItem = menu.findItem(R.id.action_search)
        Log.d("inDebug", menuItem.toString())
        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        if (menuItem != null) {
            searchView = menuItem.actionView as SearchView
        }
        if (searchView != null) {
            searchView!!.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))
            queryTextListener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.perform(GamesViewIntent.SearchGames, keyword = query)
                    return true
                }
            }
            searchView!!.setOnQueryTextListener(queryTextListener)
        }
        super.onCreateOptionsMenu(menu, inflater)


    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        if (parent != null) {
            if(pos > 0){
                viewModel.perform(GamesViewIntent.FetchGamesByGenre, genre = parent.getItemAtPosition(pos).toString())
                if(user != null){
                    user.setGenre(parent.getItemAtPosition(pos).toString())
                }

            }
            else{
                viewModel.perform(GamesViewIntent.FetchFutureRelease)
                if(user != null){
                    user.setGenre("")
                }
            }
            FirebaseDatabase.getInstance().reference.child("Users").child(user.getUid()).child("genre").setValue(user.getGenre())
            val pref = PreferencesConfig(activity as Context)
            pref.putUser(user.getUid(), User.getInstance().getUsername()
                , User.getInstance().getImagePath()
                , User.getInstance().getDescription()
                , User.getInstance().getGenre()
                , User.getInstance().getSocmed()
                , User.getInstance().getEmail())

        }else{
            Log.d("debug", "gagal ngga ketemu")
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}