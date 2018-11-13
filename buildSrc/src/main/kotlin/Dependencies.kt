object Versions {

    const val kotlin = "1.3.0"
    
    const val minSdk = 17
    const val compiledSdk = 28
    const val targetSdk = compiledSdk
    const val buildTools = "28.0.3"
    
    const val androidx = "1.0.0'"
    const val androidxConstraintLayout = "2.0.0-alpha2"
    const val legacySupport = "1.0.0"
    
    const val archNavigation = "1.0.0-alpha07"
    
    const val rxAndroid = "2.1.0"
    const val rxKotlin = "2.3.0"
    const val rxBinding = "2.1.1"
    const val rxRelay = "2.1.0"
    
    const val retrofit = "2.4.0"
    const val retrofitLoggingInterceptor = "3.9.1"
    
    const val room = "2.1.0-alpha02"
    
    const val coroutines = "1.0.1"
    
    // test
    const val junit = "4.12"
    const val test = "1.1.0"
    const val espresso = "3.1.0"
    
    // not used
    const val androidxMaterial = "1.0.0-rc01"
    const val androidxLifecycle = "2.0.0-rc01"
    const val mockito = "2.8.9"
    const val powerMock = "1.7.0"
    const val supportTest = "1.0.2"
}

object Dependencies {
    
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    
    const val appcompatLib = "androidx.appcompat:appcompat:${Versions.androidx}"
    const val vectorDrawableLib = "androidx.vectordrawable:vectordrawable:${Versions.androidx}"
    const val constraintLayoutLib = "androidx.constraintlayout:constraintlayout:${Versions.androidxConstraintLayout}"
    const val legacySupportLib = "androidx.legacy:legacy-support-v4:${Versions.legacySupport}"
    
    const val navigationFragmentLib = "android.arch.navigation:navigation-fragment:${Versions.archNavigation}"
    const val navigationUiLib = "android.arch.navigation:navigation-ui:${Versions.archNavigation}"
    
    const val rxAndroidLib = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
    const val rxKotlinLib = "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin}"
    const val rxBindingLib = "com.jakewharton.rxbinding2:rxbinding:${Versions.rxBinding}"
    const val rxRelayLib = "com.jakewharton.rxrelay2:rxrelay:${Versions.rxRelay}"
    
    const val retrofitLib = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitGsonConverterLib = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val retrofitRxJava2AdapterLib = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
    const val retrofitLoggingInterceptorLib = "com.squareup.okhttp3:logging-interceptor:${Versions.retrofitLoggingInterceptor}"
    
    const val roomLib = "androidx.room:room-runtime:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    
    const val coroutinesCoreLib = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutinesAndroidLib = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    
    // test
    const val junitLib = "junit:junit:${Versions.junit}"
    const val testRunnerLib = "androidx.test:runner:${Versions.test}"
    const val espressoLib = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val mockitoLib = "org.mockito:mockito-core:${Versions.mockito}"
    const val powerMockMockitoLib = "org.powermock:powermock-api-mockito2:${Versions.powerMock}"
    const val powerMockJunitLib = "org.powermock:powermock-module-junit4:${Versions.powerMock}"
    
    // not used
    const val materialLib = "com.google.android.material:material:${Versions.androidxMaterial}"
    const val lifecycleExtensionsLib = "androidx.lifecycle:lifecycle-extensions:${Versions.androidxLifecycle}"
    const val lifecycleViewModelsLib = "androidx.lifecycle:lifecycle-viewmodel:${Versions.androidxLifecycle}"
    const val testRules = "com.android.support.test:rules:${Versions.supportTest}"
}