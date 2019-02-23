object Versions {

    const val minSdk = 17
    const val compiledSdk = 28
    const val targetSdk = compiledSdk
    const val buildTools = "28.0.3"

    const val kotlin = "1.3.10"
    const val kotlinCoroutines = "1.0.1"

    const val androidx = "1.0.0"
    const val androidxCore = "1.1.0-alpha02"
    const val androidxAppcompat = "1.1.0-alpha01"
    const val androidxConstraintLayout = "2.0.0-alpha2"
    const val androidxLegacySupport = "1.0.0"
    const val androidxRoom = "2.1.0-alpha03"
    const val androidxLifecycle = "2.0.0"
    const val androidxMaterial = "1.0.0-rc01"

    const val playServiceMaps = "15.0.1"
    const val playServices = "16.0.0"

    const val archNavigation = "1.0.0-alpha07"
    const val archWorkManager = "1.0.0-alpha10"

    const val rxAndroid = "2.1.0"
    const val rxKotlin = "2.3.0"
    const val rxBinding = "2.1.1"
    const val rxRelay = "2.1.0"

    const val retrofit = "2.5.0"
    const val retrofitOkHttp = "3.12.0"
    const val retrofitSynchronousAdapter = "0.4.0"

    const val gson = "2.8.2"

    const val koin = "1.0.2"
    const val dagger = "2.16"

    const val mapsUtils = "0.5+"

    const val picasso = "2.71828"
    const val glide = "4.8.0"

    const val flexBox = "1.1.0"
    const val discreteScrollView = "1.4.9"

    const val permissionDispatcher = "3.3.1"

    const val intercom = "5.1.6"

    const val fotoApparat = "2.6.1"
    const val qrCodeReader = "2.0.3"
    const val microblink = "4.4.0@aar"

    const val branch = "3.+"

    const val multidex = "1.0.3"

    const val crashlytics = "2.9.7@aar"

    // test
    const val junit = "4.12"
    const val test = "1.1.0"
    const val espresso = "3.1.0"
    const val mockito = "2.8.9"
    const val powerMock = "2.0.0"
    const val supportTest = "1.0.2"
}

object Dependencies {

    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val kotlinReflectLib = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"

    const val kotlinCoroutinesCoreLib = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
    const val kotlinCoroutinesAndroidLib = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}"

    const val androidxCoreLib = "androidx.core:core:${Versions.androidxCore}"
    const val androidxAppcompatLib = "androidx.appcompat:appcompat:${Versions.androidxAppcompat}"
    const val androidxConstraintLayoutLib = "androidx.constraintlayout:constraintlayout:${Versions.androidxConstraintLayout}"
    const val androidxCardviewLib = "androidx.cardview:cardview:${Versions.androidx}"
    const val androidxVectorDrawableLib = "androidx.vectordrawable:vectordrawable:${Versions.androidx}"
    const val androidxLegacySupportLib = "androidx.legacy:legacy-support-v4:${Versions.androidxLegacySupport}"
    const val androidxMaterialLib = "com.google.android.material:material:${Versions.androidxMaterial}"

    const val androidxRoomLib = "androidx.room:room-runtime:${Versions.androidxRoom}"
    const val androidxRoomCompiler = "androidx.room:room-compiler:${Versions.androidxRoom}"

    const val androidxLifecycleExtensionsLib = "androidx.lifecycle:lifecycle-extensions:${Versions.androidxLifecycle}"
    const val androidxLifecycleViewModelsLib = "androidx.lifecycle:lifecycle-viewmodel:${Versions.androidxLifecycle}"
    const val androidxLifecycleRuntimeLib = "androidx.lifecycle:lifecycle-runtime:${Versions.androidxLifecycle}"
    const val androidxLifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.androidxLifecycle}"

    const val playServiceMapsLib = "com.google.android.gms:play-services-maps:${Versions.playServiceMaps}"
    const val playServicesLocation = "com.google.android.gms:play-services-location:${Versions.playServices}"

    const val archNavigationFragmentLib = "android.arch.navigation:navigation-fragment:${Versions.archNavigation}"
    const val archNavigationUiLib = "android.arch.navigation:navigation-ui:${Versions.archNavigation}"

    const val archWorkManagerLib = "android.arch.work:work-runtime:${Versions.archWorkManager}"

    const val rxAndroidLib = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
    const val rxKotlinLib = "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin}"
    const val rxBindingLib = "com.jakewharton.rxbinding2:rxbinding:${Versions.rxBinding}"
    const val rxRelayLib = "com.jakewharton.rxrelay2:rxrelay:${Versions.rxRelay}"

    const val retrofitLib = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitGsonConverterLib = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val retrofitRxJava2AdapterLib = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
    const val retrofitOkHttpLib = "com.squareup.okhttp3:okhttp:${Versions.retrofitOkHttp}"
    const val retrofitOkHttpLoggingInterceptorLib = "com.squareup.okhttp3:logging-interceptor:${Versions.retrofitOkHttp}"
    const val retrofitSynchronousAdapterLib = "com.jaredsburrows.retrofit:retrofit2-synchronous-adapter:${Versions.retrofitSynchronousAdapter}"

    const val gsonLib = "com.google.code.gson:gson:${Versions.gson}"

    const val koinLib = "org.koin:koin-android:${Versions.koin}"
    const val koinScopeLib = "org.koin:koin-androidx-scope:${Versions.koin}"
    const val koinViewModelLib = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    const val koinTestLib = "org.koin:koin-test:${Versions.koin}"

    const val daggerLib = "com.google.dagger:dagger:${Versions.dagger}"
    const val daggerAndroidLib = "com.google.dagger:dagger-android-support:${Versions.dagger}"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    const val daggerAndroidProcessor = "com.google.dagger:dagger-android-processor:${Versions.dagger}"

    const val mapsUtilsLib = "com.google.maps.android:android-maps-utils:${Versions.mapsUtils}"

    const val flexBoxLib = "com.google.android:flexbox:${Versions.flexBox}"
    const val picassoLib = "com.squareup.picasso:picasso:${Versions.picasso}"

    const val glideLib = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"

    const val regulaLib = "com.regula.documentreader.full:core:+@aar"
    const val regulaApiLib = "com.regula.documentreader:api:+@aar"

    const val permissionDispatcherLib = "com.github.hotchemi:permissionsdispatcher:${Versions.permissionDispatcher}"
    const val permissionDispatcherProcessor = "com.github.hotchemi:permissionsdispatcher-processor:${Versions.permissionDispatcher}"

    const val intercomLib = "io.intercom.android:intercom-sdk-base:${Versions.intercom}"

    const val fotoApparatLib = "io.fotoapparat:fotoapparat:${Versions.fotoApparat}"

    const val qrCodeReaderLib = "com.dlazaro66.qrcodereaderview:qrcodereaderview:${Versions.qrCodeReader}"

    const val microblinkLib = "com.microblink:blinkid:${Versions.microblink}"

    const val branchLib = "io.branch.sdk.android:library:${Versions.branch}"

    const val multidexLib = "com.android.support:multidex:${Versions.multidex}"

    const val discreteScrollViewLib = "com.yarolegovich:discrete-scrollview:${Versions.discreteScrollView}"

    const val crashlyticsLib = "com.crashlytics.sdk.android:crashlytics:${Versions.crashlytics}"

    // test
    const val junitLib = "junit:junit:${Versions.junit}"
    const val testRunnerLib = "androidx.test:runner:${Versions.test}"
    const val espressoLib = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val mockitoLib = "org.mockito:mockito-core:${Versions.mockito}"
    const val powerMockMockitoLib = "org.powermock:powermock-api-mockito2:${Versions.powerMock}"
    const val powerMockJunitLib = "org.powermock:powermock-module-junit4:${Versions.powerMock}"
    const val testRules = "com.android.support.test:rules:${Versions.supportTest}"
}