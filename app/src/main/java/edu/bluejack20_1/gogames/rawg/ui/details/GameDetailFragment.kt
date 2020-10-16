package edu.bluejack20_1.gogames.rawg.ui.details

import android.content.Context
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.ekn.gruzer.rawg.entity.GameSingle
import edu.bluejack20_1.gogames.R
import edu.bluejack20_1.gogames.rawg.RawgApplication
import edu.bluejack20_1.gogames.rawg.di.detailview.DetailedScreenViewModelModule
import edu.bluejack20_1.gogames.rawg.ui.details.viewmodel.DetailViewIntent
import edu.bluejack20_1.gogames.rawg.ui.details.viewmodel.DetailsViewModel
import edu.bluejack20_1.gogames.rawg.utils.LoadImage
import kotlinx.android.synthetic.main.fragment_game_detail.*
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
        details_game_title_txt.text = gameTitle
        LoadImage(details_image, gameImage)

        viewModel.viewState.observe(viewLifecycleOwner, Observer { render(it) })
        viewModel.handleIntent(DetailViewIntent.FetchData(gameID))

    }

    private fun render (viewState: DetailsViewState) = when (viewState) {
        is DetailsViewState.isLoading -> isLoading()
        is DetailsViewState.isDoneLoading -> loadingIsDone()
        is DetailsViewState.ShowData -> show(viewState.game)
        is DetailsViewState.Error -> showError(viewState.error)
    }

    private fun isLoading() {
        //TODO: show loading progress
    }

    private fun loadingIsDone() {
        //TODO: hide loading progress
    }

    private fun showError(error: String) {
        //TODO: show error message
    }

    private fun show(gameSingle: GameSingle?) {
        gameSingle?.let {
            details_description_txt.text = Html.fromHtml(it.description)
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