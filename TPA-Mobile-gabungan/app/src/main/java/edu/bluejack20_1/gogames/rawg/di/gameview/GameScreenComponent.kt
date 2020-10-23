package edu.bluejack20_1.gogames.rawg.di.gameview

import dagger.Subcomponent
import edu.bluejack20_1.gogames.rawg.di.GameScreenScope
import edu.bluejack20_1.gogames.rawg.ui.games.GamesFragment


@GameScreenScope
@Subcomponent(modules = [GameScreenViewModelModule::class])
interface GameScreenComponent {
    fun inject(fragment: GamesFragment)
}