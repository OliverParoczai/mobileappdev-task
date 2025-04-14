package hu.oliverparoczai.arfolyamok

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

class MainApplication : Application() {
    // Create a DataStore instance
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate() {
        super.onCreate()
        // Initialize any other components here if needed
    }
}
