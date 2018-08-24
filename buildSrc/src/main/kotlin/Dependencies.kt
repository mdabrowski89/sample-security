object Versions {

    const val kotlin = "1.2.61"

    const val minSdk = 17
    const val compiledSdk = 28
    const val targetSdk = compiledSdk

    const val androidx = "1.0.0-rc01"
    const val constraintLayout = "1.1.0"

    // test
    const val junit = "4.12"
    const val testRunner = "1.0.2"
    const val espresso = "3.0.2"
}

object Dependencies {

    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

    const val appcompatLib = "androidx.appcompat:appcompat:${Versions.androidx}"
    const val vectorDrawableLib = "androidx.vectordrawable:vectordrawable:${Versions.androidx}"
    const val materialLib = "com.google.android.material:material:${Versions.androidx}"

    const val constraintLayoutLib = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"

    // test
    const val junitLib = "junit:junit:${Versions.junit}"
    const val testRunner = "com.android.support.test:runner:${Versions.testRunner}"
    const val espressoLib = "com.android.support.test.espresso:espresso-core:${Versions.espresso}"
}