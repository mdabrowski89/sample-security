package pl.mobite.sample.security.ui.base

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import pl.mobite.sample.security.ui.base.mvi.*

abstract class MviBaseActivity<
        ActionType: MviAction,
        ResultType: MviResult,
        ViewStateType: MviViewState<ResultType>,
        ViewModelType: MviViewModel<ActionType, ResultType, ViewStateType>>(
    private val viewModelClass: Class<ViewModelType>
): BaseActivity() {

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
        mviController.initViewStatesObservable(savedInstanceState, forceInitWithLastViewState())
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
     * Update the last rendered view state before it will be saved to outBundle in onSaveInstanceState() method
     */
    open fun updateViewStateBeforeSave(viewState: ViewStateType?): ViewStateType? = viewState

    /**
     * When activity is recreated use saved view state instead of re emitting the last one
     */
    open fun forceInitWithLastViewState() = false
}