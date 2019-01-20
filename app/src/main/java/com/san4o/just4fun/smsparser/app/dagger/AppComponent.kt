package com.san4o.just4fun.smsparser.app.dagger

import com.san4o.just4fun.smsparser.app.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    AndroidInjectionModule::class,
    AppBindingsModule::class,
    DatabaseModule::class
])
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun app(app: App): AppComponent.Builder

        fun build(): AppComponent
    }
}
