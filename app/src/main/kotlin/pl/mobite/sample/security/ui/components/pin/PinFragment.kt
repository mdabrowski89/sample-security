package pl.mobite.sample.security.ui.components.pin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pl.mobite.sample.security.R


class PinFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pin, container, false)
    }

    companion object {

        fun getInstance(): Fragment {
            return PinFragment()
        }

        private const val KEY_ALIAS = "BUBBLES"
    }
}