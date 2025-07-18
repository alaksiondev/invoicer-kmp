package io.github.monolithic.invoicer.sharedApp.init.modules.platform

import io.github.monolithic.invoicer.foundation.analytics.AnalyticsTracker
import io.github.monolithic.invoicer.foundation.auth.firebase.FirebaseHelper
import io.github.monolithic.invoicer.foundation.auth.firebase.IosGoogleFirebaseHelper
import io.github.monolithic.invoicer.foundation.storage.LocalStorage
import io.github.monolithic.invoicer.sharedApp.init.di.appModule
import io.github.monolithic.invoicer.sharedApp.init.modules.ModuleInitializer
import org.koin.core.context.startKoin
import org.koin.dsl.module

actual class KoinModule(
    private val storage: LocalStorage,
    private val analyticsTracker: AnalyticsTracker,
    private val firebaseHelper: FirebaseHelper,
    private val googleFirebaseHelper: IosGoogleFirebaseHelper
) : ModuleInitializer {
    override fun onStart() {
        startKoin {
            modules(appModule)
            modules(
                module {
                    factory<LocalStorage> { storage }
                    factory<FirebaseHelper> { firebaseHelper }
                    factory<AnalyticsTracker> { analyticsTracker }
                    factory<IosGoogleFirebaseHelper> { googleFirebaseHelper }
                }
            )
        }
    }
}
