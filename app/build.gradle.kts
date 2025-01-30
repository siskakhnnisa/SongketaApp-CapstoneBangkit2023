import org.apache.tools.ant.util.JavaEnvUtils.VERSION_1_8
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

android {
    namespace = "com.songketa.songket_recognition_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.songketa.songket_recognition_app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_POINT", "\"https://songket-fab-backend-server-z4uvonfz2q-et.a.run.app/\"")
        buildConfigField("String", "API_POINT_ML", "\"https://songket-fab-ml-server-z4uvonfz2q-et.a.run.app/\"")
        buildConfigField("String", "API_KEY", "\"bangkitbersamasongketa\"")
        buildConfigField("String", "API_KEY_ML", "\"jalan\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    implementation ("com.synnapps:carouselview:0.1.5")

    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation ("com.github.bumptech.glide:glide:4.16.0")


    implementation("androidx.fragment:fragment-ktx:1.6.2")

    //room
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.room:room-paging:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")

    //navigation
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.5")

    //remote
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.android.libraries.places:places:3.3.0")

    //Coil
    implementation ("io.coil-kt:coil-compose:2.2.2")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    implementation ("com.jakewharton.rxbinding2:rxbinding:2.0.0")
    implementation ("io.reactivex.rxjava2:rxjava:2.2.19")
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    implementation ("androidx.cardview:cardview:1.0.0")


}