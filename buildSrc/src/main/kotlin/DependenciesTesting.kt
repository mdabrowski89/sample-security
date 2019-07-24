object VersionsTesting {

    const val junit = "4.12"
    const val androidxTest = "1.1.1"
    const val androidxEspressoTest = "3.1.1"
    const val mockito = "2.21.0"
    const val mockitoAndroid = "2.19.0"
    const val powerMock = "2.0.0"
}

object DependenciesTesting {

    // unit test
    const val junitLib = "junit:junit:${VersionsTesting.junit}"
    const val powerMockMockitoLib = "org.powermock:powermock-api-mockito2:${VersionsTesting.powerMock}"
    const val powerMockJunitLib = "org.powermock:powermock-module-junit4:${VersionsTesting.powerMock}"

    // ui test
    const val mockitoLib = "org.mockito:mockito-core:${VersionsTesting.mockito}"
    const val mockitoAndroidLib = "org.mockito:mockito-android:${VersionsTesting.mockitoAndroid}"

    const val androidxTestRunnerLib = "androidx.test:runner:${VersionsTesting.androidxTest}"
    const val androidxTestRulesLib = "androidx.test:rules:${VersionsTesting.androidxTest}"
    const val androidxRoomTest = "androidx.room:room-testing:${VersionsJetpack.androidxRoom}"

    const val androidxNavigationTest = "androidx.navigation:navigation-testing-ktx:${VersionsJetpack.androidxNavigation}"

    const val espressoLib = "androidx.test.espresso:espresso-core:${VersionsTesting.androidxEspressoTest}"
}