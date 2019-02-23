package pl.mobite.sample.security.ui.base

import android.os.Bundle
import pl.mobite.sample.security.ui.base.mvi.*

abstract class MviBaseFragment<
        ActionType: MviAction,
        ResultType: MviResult,
        ViewStateType: MviViewState<ResultType>,
        ViewModelType: MviViewModel<ActionType, ResultType, ViewStateType>>(
    private val modelClass: Class<ViewModelType>
): BaseFragment() {

    protected val mviController = MviFragmentController(this, ::render, ::initialAction)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mviController.onCreate(savedInstanceState, modelClass)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mviController.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    abstract fun render(state: ViewStateType)

    open fun initialAction(state: ViewStateType?): ActionType? = null
}