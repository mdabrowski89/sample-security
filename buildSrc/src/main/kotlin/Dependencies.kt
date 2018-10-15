object Versions {

    const val kotlin = "1.2.71"

    const val minSdk = 17
    const val compiledSdk = 28
    const val targetSdk = compiledSdk

    const val androidx = "1.0.0-rc02"
    const val androidxMaterial = "1.0.0-rc01"
    const val androidxLifecycle = "2.0.0-rc01"

    const val rxKotlin = "2.2.0"
    const val rxBinding = "2.1.1"
    const val rxRelay = "2.0.0"

    // test
    const val junit = "4.12"
    const val mockito = "2.21.0"
    const val supportTest = "1.0.2"
    const val espresso = "3.0.2"
}

object Dependencies {

    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

    const val appcompatLib = "androidx.appcompat:appcompat:${Versions.androidx}"
    const val vectorDrawableLib = "androidx.vectordrawable:vectordrawable:${Versions.androidx}"
    const val materialLib = "com.google.android.material:material:${Versions.androidxMaterial}"

    const val lifecycleExtensionsLib = "androidx.lifecycle:lifecycle-extensions:${Versions.androidxLifecycle}"
    const val lifecycleViewModelsLib = "androidx.lifecycle:lifecycle-viewmodel:${Versions.androidxLifecycle}"

    const val rxKotlinLib = "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin}"
    const val rxBindingLib = "com.jakewharton.rxbinding2:rxbinding:${Versions.rxBinding}"
    const val rxRelayLib = "com.jakewharton.rxrelay2:rxrelay:${Versions.rxRelay}"

    // test
    const val junitLib = "junit:junit:${Versions.junit}"
    const val mockitoLib = "org.mockito:mockito-core:${Versions.mockito}"
    const val testRunner = "com.android.support.test:runner:${Versions.supportTest}"
    const val testRules = "com.android.support.test:rules:${Versions.supportTest}"
    const val espressoLib = "com.android.support.test.espresso:espresso-core:${Versions.espresso}"
}