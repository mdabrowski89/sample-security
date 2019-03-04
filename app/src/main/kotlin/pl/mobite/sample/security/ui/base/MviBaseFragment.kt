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
): BaseFragment() {

    protected val mviController by lazy {
        MviController(
            ViewModelProviders.of(this),
            javaClass.name,
            lifecycle,
            ::render,
            ::initialAction,
            ::updateViewStateBeforeSave
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mviController.initViewModel(viewModelClass)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mviController.initViewStatesObservable(savedInstanceState, alwaysInitWithSavedViewState())
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

    /**
     * Sends this action right after subscription to viewStates observer
     */
    open fun initialAction(lastViewState: ViewStateType?): ActionType? = null

    /**
     * Update UI based on ViewState
     */
    abstract fun render(viewState: ViewStateType)

    /**
     * Update the last rendered view state before:
     * 1. it will be saved to outBundle in onSaveInstanceState() method
     * 2. the view will be destroyed in onDestroyView() method
     */
    open fun updateViewStateBeforeSave(viewState: ViewStateType?): ViewStateType? = viewState

    /**
     * When fragment view is recreated use saved view state instead of re emitting the last one
     */
    open fun alwaysInitWithSavedViewState() = false

}