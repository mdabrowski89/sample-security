package pl.mobite.sample.security.ui.base

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import pl.mobite.sample.security.ui.base.mvi.*

abstract class MviBaseFragment<
        ActionType: MviAction,
        ResultType: MviResult,
        ViewStateType: MviViewState<ResultType>,
        ViewModelType: MviViewModel<ActionType, ResultType, ViewStateType>>(
    private val viewModelClass: Class<ViewModelType>
): BaseFragment(), MviControllerCallback<ActionType, ResultType, ViewStateType> {

    protected val mviController by lazy {
        MviController(
            ViewModelProviders.of(this),
            javaClass.name,
            lifecycle,
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mviController.initViewModel(viewModelClass)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mviController.initViewStatesObservable(savedInstanceState)
    }

    override fun onDestroyView() {
        /**
         * Fragment view can be destroyed an recreated without destroying the whole fragment
         * so we need to additionally update state here
         */
        with(mviController) {
            viewState = updateViewStateBeforeSave(viewState)
        }
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mviController.saveLastViewState(outState)
        super.onSaveInstanceState(outState)
    }
}