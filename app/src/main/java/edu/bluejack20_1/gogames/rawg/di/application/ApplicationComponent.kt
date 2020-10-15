package edu.bluejack20_1.gogames.rawg.di.application

import dagger.Component
import edu.bluejack20_1.gogames.rawg.RawgApplication
import edu.bluejack20_1.gogames.rawg.di.ApplicationScope
import edu.bluejack20_1.gogames.rawg.di.gameview.GameScreenComponent
import edu.bluejack20_1.gogames.rawg.di.gameview.GameScreenViewModelModule

@ApplicationScope
@Component(modules = [NetworkSourceModule::class, RemoteSourceModule::class, RepositoryManagerModule::class])
interface ApplicationComponent{
    fun inject(rawgApplication: RawgApplication)

    fun provideGameScreenComponent(
        gameScreenViewModelModule: GameScreenViewModelModule
    ): GameScreenComponent
}