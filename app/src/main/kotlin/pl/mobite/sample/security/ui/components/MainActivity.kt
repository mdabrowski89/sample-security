package pl.mobite.sample.security.ui.components

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_content.*
import pl.mobite.sample.security.R
import pl.mobite.sample.security.ui.components.fingerprint.FingerprintFragment
import pl.mobite.sample.security.ui.components.pin.PinFragment
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyFragment
import pl.mobite.sample.security.ui.custom.NavItem

class MainActivity : AppCompatActivity() {

    private var currentNavItem: NavItem? = null

    private val navItems = listOf(
            NavItem(R.id.navItemSecretKey, "SECRET_KEY_FRAGMENT_TAG", R.string.secret_key_screen_title),
            NavItem(R.id.navItemFingerprint, "FINGERPRINT_FRAGMENT_TAG", R.string.fingerprint_screen_title),
            NavItem(R.id.navItemPin, "PIN_FRAGMENT_TAG", R.string.pin_screen_title)
    )

    private fun NavItem.getNewFragment()
            = when(this.navItemId) {
                R.id.navItemSecretKey -> SecretKeyFragment.getInstance()
                R.id.navItemFingerprint -> FingerprintFragment.getInstance()
                R.id.navItemPin -> PinFragment.getInstance()
                else -> throw IllegalStateException("Missing create fragment function for tag ${this.fragmentTag}")
            }

    private val navItemSelectedListener = NavigationView.OnNavigationItemSelectedListener { item ->
        navItems.find { it.navItemId == item.itemId }?.let { navItem -> showFragment(navItem) }
        drawerLayout.closeDrawer(GravityCompat.START)
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.main_nav_drawer_open_desc, R.string.main_nav_drawer_close_desc)
                .apply {
                    drawerLayout.addDrawerListener(this)
                    syncState()
                }

        navigationView.setNavigationItemSelectedListener(navItemSelectedListener)
        showFragment(savedInstanceState?.getParcelable(NavItem.PARCEL_KEY) ?: navItems.first())
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putParcelable(NavItem.PARCEL_KEY, currentNavItem)
        super.onSaveInstanceState(outState)
    }

    private fun showFragment(navItem: NavItem) {
        currentNavItem = navItem
        setTitle(navItem.screenTitleResId)
        navigationView.setCheckedItem(navItem.navItemId)

        with(supportFragmentManager) {
            findFragmentByTag(navItem.fragmentTag).let { fragment ->
                beginTransaction()
                    .replace(R.id.container, fragment ?: navItem.getNewFragment(), navItem.fragmentTag)
                    .apply { if (fragment == null) addToBackStack(null) }
                    .commitAllowingStateLoss()
            }
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            finish()
        }
    }
}
