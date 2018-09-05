package pl.mobite.sample.security.ui.components

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import pl.mobite.sample.security.R
import pl.mobite.sample.security.ui.components.fingerprint.FingerprintFragment
import pl.mobite.sample.security.ui.components.pin.PinFragment
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyFragment

class MainActivity : AppCompatActivity() {

    private var lastFragmentTag: String? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_secret_key -> {
                showFragment(SECRET_KEY_FRAGMENT_TAG)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_fingerprint -> {
                showFragment(FINGERPRINT_FRAGMENT_TAG)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_pin -> {
                showFragment(PIN_FRAGMENT_TAG)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO: change navigation bar to navigation drawer, because in the future there will be more than 3 tabs
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        navigation.selectedItemId = 0
        showFragment(savedInstanceState?.getString(LAST_FRAGMENT_TAG_KEY) ?: SECRET_KEY_FRAGMENT_TAG)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString(LAST_FRAGMENT_TAG_KEY, lastFragmentTag)
        super.onSaveInstanceState(outState)
    }

    private fun showFragment(tag: String) {
        lastFragmentTag = tag
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

    override fun onBackPressed() {
        finish()
    }

    companion object {

        private const val SECRET_KEY_FRAGMENT_TAG = "SECRET_KEY_FRAGMENT_TAG"
        private const val FINGERPRINT_FRAGMENT_TAG = "FINGERPRINT_FRAGMENT_TAG"
        private const val PIN_FRAGMENT_TAG = "PIN_FRAGMENT_TAG"
        private const val LAST_FRAGMENT_TAG_KEY = "LAST_FRAGMENT_TAG_KEY"
    }
}
