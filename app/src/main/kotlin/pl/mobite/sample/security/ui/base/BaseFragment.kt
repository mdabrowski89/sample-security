package pl.mobite.sample.security.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment: Fragment() {

    protected lateinit var navController: NavController
    protected val onStopDisposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutResId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    override fun onStop() {
        onStopDisposables.clear()
        super.onStop()
    }

    @LayoutRes
    abstract fun getLayoutResId(): Int
}