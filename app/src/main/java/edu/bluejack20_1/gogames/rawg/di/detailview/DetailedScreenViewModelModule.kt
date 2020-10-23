package edu.bluejack20_1.gogames.rawg.di.detailview

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import edu.bluejack20_1.gogames.rawg.repository.GameSingleRepository
import edu.bluejack20_1.gogames.rawg.ui.details.viewmodel.DetailsViewModel
import edu.bluejack20_1.gogames.rawg.utils.MyViewModelFactory

@Module
class DetailedScreenViewModelModule(private val fragment: Fragment) {
    @Provides
    fun provideFragment() = fragment

    @Provides
    fun provideViewModel(fragment: Fragment, repository: GameSingleRepository): DetailsViewModel {
        return ViewModelProvider(fragment,
            MyViewModelFactory{
                DetailsViewModel(repository)
            }).get(DetailsViewModel::class.java)
    }
}