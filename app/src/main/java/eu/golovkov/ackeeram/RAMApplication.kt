package eu.golovkov.ackeeram

import android.app.Application

class RAMApplication : Application() {

    companion object {
        lateinit var instance: RAMApplication
    }

    private var _apiService: ApiService? = null
    val apiService: ApiService by lazy {
        _apiService ?: ApiService.create("https://rickandmortyapi.com/api/")
            .also { _apiService = it }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}

// dagger is over-rated lol
val app: RAMApplication get() = RAMApplication.instance
