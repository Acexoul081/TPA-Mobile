package edu.bluejack20_1.gogames.rawg.di.application

import com.ekn.gruzer.rawg.network.RawgServiceApi
import dagger.Module
import dagger.Provides
import edu.bluejack20_1.gogames.rawg.di.ApplicationScope
import edu.bluejack20_1.gogames.rawg.repository.RawgApi
import edu.bluejack20_1.gogames.rawg.repository.RemoteSource

@Module
class RemoteSourceModule{
    @ApplicationScope
    @Provides
    fun provideRemoteSource(service: RawgApi): RemoteSource{
        return RemoteSource(service)
    }
}