package edu.bluejack20_1.gogames.rawg.di.application

import com.ekn.gruzer.rawg.network.RawgServiceApi
import dagger.Module
import dagger.Provides
import edu.bluejack20_1.gogames.rawg.di.ApplicationScope

@Module
class NetworkSourceModule{
    @ApplicationScope
    @Provides
    fun provideNetworkApi(): RawgServiceApi{
        return RawgServiceApi.create()
    }
}