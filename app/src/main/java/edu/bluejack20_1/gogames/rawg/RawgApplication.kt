package edu.bluejack20_1.gogames.rawg

import android.app.Application
import edu.bluejack20_1.gogames.rawg.di.application.ApplicationComponent
import edu.bluejack20_1.gogames.rawg.di.application.DaggerApplicationComponent
import io.branch.referral.Branch
import javax.inject.Inject

class RawgApplication : Application(){

    @Inject
    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        // Branch logging for debugging
        Branch.enableLogging();

        // Branch object initialization
        Branch.getAutoInstance(this);
        DaggerApplicationComponent
            .builder()
            .build()
            .inject(this)
    }
}