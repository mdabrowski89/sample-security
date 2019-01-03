package androidx.navigation.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

@Navigator.Name("custom_fragment")
class CustomFragmentNavigator(
    context: Context,
    fragmentManager: FragmentManager,
    private val containerId: Int
): FragmentNavigator(context, fragmentManager, containerId) {

    private val TAG = "CustomFragmentNavigator"

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ) {
        if (mFragmentManager.isStateSaved) {
            Log.i(TAG, "Ignoring navigate() call: FragmentManager has already" + " saved its state")
            return
        }

        val fragmentClass = destination.fragmentClass
        val tag = fragmentClass.name
        val frag = createFragment(tag, fragmentClass, args)
        val ft = mFragmentManager.beginTransaction()

        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }

        ft.replace(containerId, frag, tag)
        ft.setPrimaryNavigationFragment(frag)

        @IdRes val destId = destination.id
        val initialNavigation = mBackStack.isEmpty()
        val isClearTask = navOptions != null && navOptions.shouldClearTask()
        // TODO Build first class singleTop behavior for fragments
        val isSingleTopReplacement = (navOptions != null && !initialNavigation
                && navOptions.shouldLaunchSingleTop()
                && mBackStack.peekLast() == destId)

        val backStackEffect: Int
        if (initialNavigation || isClearTask) {
            backStackEffect = Navigator.BACK_STACK_DESTINATION_ADDED
        } else if (isSingleTopReplacement) {
            // Single Top means we only want one instance on the back stack
            if (mBackStack.size > 1) {
                // If the Fragment to be replaced is on the FragmentManager's
                // back stack, a simple replace() isn't enough so we
                // remove it from the back stack and put our replacement
                // on the back stack in its place
                mFragmentManager.popBackStack()
                ft.addToBackStack(Integer.toString(destId))
                mIsPendingBackStackOperation = true
            }
            backStackEffect = Navigator.BACK_STACK_UNCHANGED
        } else {
            ft.addToBackStack(Integer.toString(destId))
            mIsPendingBackStackOperation = true
            backStackEffect = Navigator.BACK_STACK_DESTINATION_ADDED
        }
        if (navigatorExtras is Extras) {
            for ((key, value) in navigatorExtras.sharedElements) {
                ft.addSharedElement(key, value)
            }
        }
        ft.setReorderingAllowed(true)
        ft.commit()
        // The commit succeeded, update our view of the world
        if (backStackEffect == Navigator.BACK_STACK_DESTINATION_ADDED) {
            mBackStack.add(destId)
        }
        dispatchOnNavigatorNavigated(destId, backStackEffect)
    }

    private fun createFragment(tag: String, fragmentClass: Class<out Fragment>, args: Bundle?): Fragment {
        val f: Fragment
        try {
            f = mFragmentManager.findFragmentByTag(tag) ?: fragmentClass.newInstance()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

        if (args != null) {
            f.arguments = args
        }
        return f
    }
}