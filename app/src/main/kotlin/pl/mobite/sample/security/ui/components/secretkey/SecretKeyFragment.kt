package pl.mobite.sample.security.ui.components.secretkey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pl.mobite.sample.security.R


class SecretKeyFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_secret_key, container, false)
    }

    companion object {

        fun getInstance(): Fragment {
            return SecretKeyFragment()
        }
    }
}