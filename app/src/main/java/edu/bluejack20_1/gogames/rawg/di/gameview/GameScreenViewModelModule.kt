package edu.bluejack20_1.gogames.rawg.di.gameview

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import dagger.Module
import dagger.Provides
import edu.bluejack20_1.gogames.rawg.repository.GamesRepository
import edu.bluejack20_1.gogames.rawg.ui.games.GamesViewModel
import edu.bluejack20_1.gogames.rawg.utils.MyViewModelFactory

@Module
class GameScreenViewModelModule (private val fragment: Fragment) {
    @Provides
    fun provideViewModel(fragment: Fragment, gamesRepository: GamesRepository): GamesViewModel{
        return ViewModelProvider(fragment,
            MyViewModelFactory{
                GamesViewModel(gamesRepository)
            }).get(GamesViewModel::class.java)
    }

    @Provides
    fun provideFragment() = fragment
}