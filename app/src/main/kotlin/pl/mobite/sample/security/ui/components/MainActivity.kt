package pl.mobite.sample.security.ui.components

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import pl.mobite.sample.security.R
import pl.mobite.sample.security.ui.components.fingerprint.FingerprintFragment
import pl.mobite.sample.security.ui.components.pin.PinFragment
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyFragment

class MainActivity : AppCompatActivity() {

    private var lastFragmentTag: String? = null

    private val mOnNavigationItemSelectedListener = NavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navSecretKey -> {
                showFragment(SECRET_KEY_FRAGMENT_TAG)
            }
            R.id.navFingerprint -> {
                showFragment(FINGERPRINT_FRAGMENT_TAG)
            }
            R.id.navPin -> {
                showFragment(PIN_FRAGMENT_TAG)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val lastFragmentTag = savedInstanceState?.getString(LAST_FRAGMENT_TAG_KEY) ?: SECRET_KEY_FRAGMENT_TAG
        showFragment(lastFragmentTag)
        navigationView.setCheckedItem(getItemIdForTag(lastFragmentTag))
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString(LAST_FRAGMENT_TAG_KEY, lastFragmentTag)
        super.onSaveInstanceState(outState)
    }

    private fun showFragment(tag: String) {
        lastFragmentTag = tag
        setTitle(getScreenTitleForTag(tag))
        val fragment = supportFragmentManager.findFragmentByTag(tag)
        val addToBackStack = fragment == null
        val transaction = supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment ?: createFragmentInstanceForTag(tag), tag)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commitAllowingStateLoss()
    }

    private fun createFragmentInstanceForTag(tag: String): Fragment {
        return when(tag) {
            SECRET_KEY_FRAGMENT_TAG -> SecretKeyFragment.getInstance()
            FINGERPRINT_FRAGMENT_TAG -> FingerprintFragment.getInstance()
            PIN_FRAGMENT_TAG -> PinFragment.getInstance()
            else -> throw Exception("Invalid fragment tag: $tag")
        }
    }

    private fun getScreenTitleForTag(tag: String): Int {
        return when(tag) {
            SECRET_KEY_FRAGMENT_TAG -> R.string.secret_key_title
            FINGERPRINT_FRAGMENT_TAG -> R.string.fingerprint_title
            PIN_FRAGMENT_TAG -> R.string.pin_title
            else -> throw Exception("Invalid fragment tag: $tag")
        }
    }

    private fun getItemIdForTag(tag: String): Int {
        return when(tag) {
            SECRET_KEY_FRAGMENT_TAG -> R.id.navSecretKey
            FINGERPRINT_FRAGMENT_TAG -> R.id.navFingerprint
            PIN_FRAGMENT_TAG -> R.id.navPin
            else -> throw Exception("Invalid fragment tag: $tag")
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    companion object {

        private const val SECRET_KEY_FRAGMENT_TAG = "SECRET_KEY_FRAGMENT_TAG"
        private const val FINGERPRINT_FRAGMENT_TAG = "FINGERPRINT_FRAGMENT_TAG"
        private const val PIN_FRAGMENT_TAG = "PIN_FRAGMENT_TAG"
        private const val LAST_FRAGMENT_TAG_KEY = "LAST_FRAGMENT_TAG_KEY"
    }
}
