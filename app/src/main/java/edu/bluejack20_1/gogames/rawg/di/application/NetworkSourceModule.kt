package edu.bluejack20_1.gogames.rawg.di.application

import dagger.Module
import dagger.Provides
import edu.bluejack20_1.gogames.rawg.di.ApplicationScope
import edu.bluejack20_1.gogames.rawg.repository.RawgApi

@Module
class NetworkSourceModule{
    @ApplicationScope
    @Provides
    fun provideNetworkApi(): RawgApi {
        return RawgApi.create()
    }
}