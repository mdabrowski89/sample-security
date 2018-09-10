package pl.mobite.sample.security.utils

import org.mockito.Mockito


/**
 * Lazy delegate for creating mocks
 */
inline fun <reified T : Any> lazyMock(): Lazy<T> = lazy { Mockito.mock(T::class.java) }


typealias StateTransformer<T> = (state: T) -> T

fun <T> createExpectedStates(initialState: T, stateTransformers: List<StateTransformer<T>>): List<T> {
    val expectedStates = mutableListOf(initialState)
    stateTransformers.forEach { stateTransformer ->
        expectedStates.add(stateTransformer(expectedStates.last()))
    }
    return expectedStates.distinctUntilChanged()
}

fun <T> List<T>.distinctUntilChanged(): List<T> {
    val distinctUtilChangedList = mutableListOf<T>()
    forEachIndexed { i, item ->
        val prevItem = getOrNull(i -1)
        if (prevItem == null || prevItem != item) {
            distinctUtilChangedList.add(item)
        }
    }
    return distinctUtilChangedList
}