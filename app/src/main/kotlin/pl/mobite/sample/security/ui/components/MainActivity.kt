package pl.mobite.sample.security.ui.components

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_content.*
import pl.mobite.sample.security.R

class MainActivity: AppCompatActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        navController = findNavController(mainNavHostFragment)

        ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.main_nav_drawer_open_desc,
            R.string.main_nav_drawer_close_desc
        )
            .apply {
                drawerLayout.addDrawerListener(this)
                syncState()
            }
        NavigationUI.setupWithNavController(navigationView, navController)
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            finish()
        }
    }
}
