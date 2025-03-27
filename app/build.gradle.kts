
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.navigation.safe.args.plugin)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.blooms"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.blooms"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.lottie)
    implementation(libs.androidx.cardview)
    implementation(libs.firebase.auth)
    implementation(libs.view.model)
    implementation(libs.live.data)
    implementation(libs.coroutines)
    implementation(libs.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.picasso)
    implementation(libs.room.runtime)
    implementation(libs.room)
    implementation(libs.gson)
    implementation(libs.circle)
    ksp(libs.room.compiler)
        // Retrofit
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")

        // Gson
        implementation("com.google.code.gson:gson:2.9.0")

        // Coroutines
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

        // ViewModel and LiveData
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation ("com.google.android.material:material:1.4.0")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("androidx.coordinatorlayout:coordinatorlayout:1.1.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.recyclerview)
}