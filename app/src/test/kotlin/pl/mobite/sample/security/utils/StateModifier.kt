package pl.mobite.sample.security.utils

import pl.mobite.sample.security.ui.components.secretkey.SecretKeyViewState


typealias StateModifier<T> = (state: T) -> T
typealias SecretKeyViewStateModifier = StateModifier<SecretKeyViewState>

fun <T> createExpectedStates(initialState: T, stateTransformers: List<StateModifier<T>>): List<T> {
    val expectedStates = mutableListOf(initialState)
    stateTransformers.forEach { stateTransformer ->
        expectedStates.add(stateTransformer(expectedStates.last()))
    }
    return expectedStates.distinctUntilChanged()
}

private fun <T> List<T>.distinctUntilChanged(): List<T> {
    val distinctUtilChangedList = mutableListOf<T>()
    forEachIndexed { i, item ->
        val prevItem = getOrNull(i -1)
        if (prevItem == null || prevItem != item) {
            distinctUtilChangedList.add(item)
        }
    }
    return distinctUtilChangedList
}