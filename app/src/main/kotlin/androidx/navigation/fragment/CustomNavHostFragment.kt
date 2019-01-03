package androidx.navigation.fragment

import androidx.navigation.Navigator


class CustomNavHostFragment: NavHostFragment() {

    /**
     * CustomFragmentNavigator - during navigation it first check if the target fragment already exist
     * and if so it will use it instead of creating new instance on every navigation action
     */
    override fun createFragmentNavigator(): Navigator<out FragmentNavigator.Destination> {
        return CustomFragmentNavigator(requireContext(), childFragmentManager, id)
    }
}