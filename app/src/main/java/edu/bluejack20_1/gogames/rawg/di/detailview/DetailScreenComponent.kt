package edu.bluejack20_1.gogames.rawg.di.detailview

import dagger.Subcomponent
import edu.bluejack20_1.gogames.rawg.di.DetailScreenScope
import edu.bluejack20_1.gogames.rawg.ui.details.GameDetailFragment

@DetailScreenScope
@Subcomponent(modules = [DetailedScreenViewModelModule::class])
interface DetailScreenComponent {
    fun inject(detailFragment: GameDetailFragment)
}