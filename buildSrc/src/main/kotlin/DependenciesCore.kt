object VersionsCore {

    const val gradle = "3.5.0"

    const val kotlin = "1.3.50"
    const val kotlinCoroutines = "1.0.1"

    const val rxAndroid = "2.1.0"
    const val rxKotlin = "2.3.0"
    const val rxBinding = "2.1.1"
    const val rxRelay = "2.1.0"

    const val retrofit = "2.4.0"
    const val retrofitOkHttp = "3.12.0"
    const val retrofitSynchronousAdapter = "0.4.0"

    const val gson = "2.8.2"

    const val koin = "2.0.0"
    const val dagger = "2.16"
}

object DependenciesCore {

    const val gradlePlugin = "com.android.tools.build:gradle:${VersionsCore.gradle}"

    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${VersionsCore.kotlin}"
    const val kotlinReflectLib = "org.jetbrains.kotlin:kotlin-reflect:${VersionsCore.kotlin}"

    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${VersionsCore.kotlin}"

    const val kotlinCoroutinesCoreLib = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${VersionsCore.kotlinCoroutines}"
    const val kotlinCoroutinesAndroidLib = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${VersionsCore.kotlinCoroutines}"

    const val koinLib = "org.koin:koin-android:${VersionsCore.koin}"
    const val koinScopeLib = "org.koin:koin-androidx-scope:${VersionsCore.koin}"
    const val koinViewModelLib = "org.koin:koin-androidx-viewmodel:${VersionsCore.koin}"
    const val koinTestLib = "org.koin:koin-test:${VersionsCore.koin}"

    const val rxAndroidLib = "io.reactivex.rxjava2:rxandroid:${VersionsCore.rxAndroid}"
    const val rxKotlinLib = "io.reactivex.rxjava2:rxkotlin:${VersionsCore.rxKotlin}"
    const val rxBindingLib = "com.jakewharton.rxbinding2:rxbinding:${VersionsCore.rxBinding}"
    const val rxRelayLib = "com.jakewharton.rxrelay2:rxrelay:${VersionsCore.rxRelay}"

    const val retrofitLib = "com.squareup.retrofit2:retrofit:${VersionsCore.retrofit}"
    const val retrofitGsonConverterLib = "com.squareup.retrofit2:converter-gson:${VersionsCore.retrofit}"
    const val retrofitRxJava2AdapterLib = "com.squareup.retrofit2:adapter-rxjava2:${VersionsCore.retrofit}"
    const val retrofitOkHttpLib = "com.squareup.okhttp3:okhttp:${VersionsCore.retrofitOkHttp}"
    const val retrofitOkHttpLoggingInterceptorLib = "com.squareup.okhttp3:logging-interceptor:${VersionsCore.retrofitOkHttp}"
    const val retrofitSynchronousAdapterLib = "com.jaredsburrows.retrofit:retrofit2-synchronous-adapter:${VersionsCore.retrofitSynchronousAdapter}"

    const val gsonLib = "com.google.code.gson:gson:${VersionsCore.gson}"

    const val daggerLib = "com.google.dagger:dagger:${VersionsCore.dagger}"
    const val daggerAndroidLib = "com.google.dagger:dagger-android-support:${VersionsCore.dagger}"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:${VersionsCore.dagger}"
    const val daggerAndroidProcessor = "com.google.dagger:dagger-android-processor:${VersionsCore.dagger}"
}