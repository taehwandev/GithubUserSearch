@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApp)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
    id("kotlinx-serialization")
}

android {
    namespace = "tech.thdev.githubusersearch"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "tech.thdev.githubusersearch"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = "${libs.versions.major.get()}.${libs.versions.minor.get()}.${libs.versions.hotfix.get()}"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.serializationJson)

    implementation(libs.coroutines.android)

    implementation(libs.androidx.appCompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.recyclerView)
    implementation(libs.androidx.constraintLayout)
    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle.viewModel)
    implementation(libs.androidx.room)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    implementation(libs.google.material)

    implementation(libs.network.retrofit)
    implementation(libs.network.retrofit.kotlinxSerializationConvert)
    implementation(libs.network.okhttp)
    implementation(libs.network.okhttp.logging)

    implementation(libs.glide)

    libs.test.run {
        testImplementation(androidx.core)
        testImplementation(androidx.runner)
        testImplementation(androidx.junit)
        testImplementation(mockito.kotlin)
        testImplementation(junit5)
        testImplementation(junit5.engine)
        testRuntimeOnly(junit5.vintage)
        testImplementation(coroutines)
        testImplementation(coroutines.turbine)
    }
}