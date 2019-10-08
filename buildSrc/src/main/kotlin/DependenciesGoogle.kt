object VersionsGoogle {
    const val playServicesMaps = "15.0.1"
    const val playServicesLocation = "17.0.0"
    const val playServicesAds = "18.2.0"

    const val mapsUtils = "0.5+"

    const val firebaseMessaging = "20.0.0"

    const val googleServices = "4.3.2"

    const val multidex = "1.0.3"
}

object DependenciesGoogle {

    const val playServicesMapsLib = "com.google.android.gms:play-services-maps:${VersionsGoogle.playServicesMaps}"
    const val playServicesLocationLib = "com.google.android.gms:play-services-location:${VersionsGoogle.playServicesLocation}"
    const val playServicesAdsLib = "com.google.android.gms:play-services-ads:${VersionsGoogle.playServicesAds}"

    const val mapsUtilsLib = "com.google.maps.android:android-maps-utils:${VersionsGoogle.mapsUtils}"

    const val firebaseMessagingLib = "com.google.firebase:firebase-messaging:${VersionsGoogle.firebaseMessaging}"

    const val googleServicesGradlePlugin = "com.google.gms:google-services:${VersionsGoogle.googleServices}"

    const val multidexLib = "com.android.support:multidex:${VersionsGoogle.multidex}"
}