package edu.bluejack20_1.gogames.rawg.ui.games

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekn.gruzer.rawg.entity.Game
import edu.bluejack20_1.gogames.R
import edu.bluejack20_1.gogames.rawg.RawgApplication
import edu.bluejack20_1.gogames.rawg.di.gameview.GameScreenViewModelModule
import kotlinx.android.synthetic.main.fragment_games.*
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GamesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class GamesFragment : Fragment() , GamesAdapter.RecyclerViewItemClickLister {

    private var searchView: SearchView? = null
    private var queryTextListener: SearchView.OnQueryTextListener? = null
    @Inject
    lateinit var viewModel: GamesViewModel

    private val gamesAdapter: GamesAdapter by lazy {
        GamesAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        viewModel.perform(GamesViewIntent.FetchFutureRelease)

    }

    private fun  render(viewState: GamesViewState) = when (viewState) {
        is GamesViewState.IsLoading -> loadingInProgress()
        is GamesViewState.IsDoneLoading -> loadingIsDone()
        is GamesViewState.ShowData -> displayGames(viewState.games)
        is GamesViewState.Error -> showError(viewState.error)

    }

    private fun showError(error: String){
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun loadingInProgress() {
        main_games_pb.visibility = View.VISIBLE
    }

    private fun loadingIsDone(){
        main_games_pb.visibility = View.GONE
    }

    private fun displayGames(games: List<Game>?) {
        games?.let {
            gamesAdapter.updateList(it)
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
        inflater.inflate(R.menu.main_menu, menu)
        val menuItem = menu.findItem(R.id.action_search)
        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        if (menuItem != null) {
            searchView = menuItem.actionView as SearchView
        }
        if (searchView != null) {
            searchView!!.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))
            queryTextListener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    Log.d("inDebug", newText)
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.perform(GamesViewIntent.SearchGames, query)
                    return true
                }
            }
            searchView!!.setOnQueryTextListener(queryTextListener)
        }
        super.onCreateOptionsMenu(menu, inflater)


    }
}