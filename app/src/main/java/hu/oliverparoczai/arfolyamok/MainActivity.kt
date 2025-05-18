package hu.oliverparoczai.arfolyamok

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import hu.oliverparoczai.arfolyamok.databinding.ActivityMainBinding
import hu.oliverparoczai.arfolyamok.model.CurrencyRate
import hu.oliverparoczai.arfolyamok.ui.loggedInAccount.LoggedInAccountFragment
import hu.oliverparoczai.arfolyamok.ui.login.LoginFragment
import java.util.prefs.Preferences
import hu.oliverparoczai.arfolyamok.model.FirebaseHelper

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private fun updateBottomNavigationView(isLoggedIn: Boolean) {
        // Clear the current menu items
        binding.navView.menu.clear()

        // Inflate the appropriate menu based on login state
        if (isLoggedIn) {
            binding.navView.inflateMenu(R.menu.main_bottom_nav_menu_loggedin)
            binding.navView.selectedItemId = R.id.navigation_loggedin

        } else {
            binding.navView.inflateMenu(R.menu.main_bottom_nav_menu_loggedout)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_login, R.id.navigation_loggedin
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        updateBottomNavigationView(false);

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No network connection", Toast.LENGTH_SHORT).show()

        }

        refreshData()

        //val firebaseHelper = FirebaseHelper()
        //firebaseHelper.addCurrencyRate(CurrencyRate(currencyCode = "USD", rate = 1.0, timestamp = System.currentTimeMillis()))
    }

    // Olyan erőforrás amihez kell android permission és értelmesen beágyazott az alkalmazás funkcionalitásába
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork?.let {
                connectivityManager.getNetworkCapabilities(it)
            }
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }

    private fun refreshData(){
        val firebaseHelper = FirebaseHelper()
        firebaseHelper.getLastNCurrencyRates("USD", 6, object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val rates = mutableListOf<CurrencyRate>()
                for (snapshot in dataSnapshot.children) {
                    val rate = snapshot.getValue(CurrencyRate::class.java)
                    rate?.let { rates.add(it) }
                }
                rates.sortBy { it.timestamp }
                rates.forEach {
                    println("Currency Code: ${it.currencyCode}, Rate: ${it.rate}, Timestamp: ${it.timestamp}")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Error: ${databaseError.message}")
            }
        })
    }

    //Lifecycle Hook
    override fun onResume() {
        super.onResume()
        refreshData()
    }



    fun onLoginSuccess() {
        //val navController = findNavController(R.id.nav_host_fragment_activity_main)
        //navController.navigate(R.id.navigation_loggedin)

        binding.navView.menu.clear()
        updateBottomNavigationView(true);

    }

}