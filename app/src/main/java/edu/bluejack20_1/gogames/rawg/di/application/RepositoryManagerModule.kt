package edu.bluejack20_1.gogames.rawg.di.application

import dagger.Module
import dagger.Provides
import edu.bluejack20_1.gogames.rawg.di.ApplicationScope
import edu.bluejack20_1.gogames.rawg.repository.GameSingleRepository
import edu.bluejack20_1.gogames.rawg.repository.GamesRepository
import edu.bluejack20_1.gogames.rawg.repository.RemoteSource

@Module
class RepositoryManagerModule {
    @ApplicationScope
    @Provides
    fun provideGameRepository(remoteSource: RemoteSource): GamesRepository {
        return GamesRepository(remoteSource)
    }

    @ApplicationScope
    @Provides
    fun provideGameSingleRepository(remoteSource: RemoteSource): GameSingleRepository {
        return GameSingleRepository(remoteSource)
    }
}