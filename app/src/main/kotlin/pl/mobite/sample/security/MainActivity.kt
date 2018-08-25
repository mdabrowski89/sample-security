package pl.mobite.sample.security

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import pl.mobite.sample.security.fragments.FingerprintFragment
import pl.mobite.sample.security.fragments.PinFragment
import pl.mobite.sample.security.fragments.SecretKeyFragment

class MainActivity : AppCompatActivity() {

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

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        navigation.selectedItemId = 0
        showFragment(SECRET_KEY_FRAGMENT_TAG)
    }

    private fun showFragment(tag: String) {
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
    }
}
