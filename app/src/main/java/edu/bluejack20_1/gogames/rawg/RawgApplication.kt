package edu.bluejack20_1.gogames.rawg

import android.app.Application
import edu.bluejack20_1.gogames.rawg.di.application.ApplicationComponent
import edu.bluejack20_1.gogames.rawg.di.application.DaggerApplicationComponent
import javax.inject.Inject

class RawgApplication : Application(){

    @Inject
    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        DaggerApplicationComponent
            .builder()
            .build()
            .inject(this)
    }
}