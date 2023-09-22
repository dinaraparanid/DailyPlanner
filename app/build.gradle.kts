plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10"
    id("com.google.devtools.ksp")
    kotlin("kapt")
}

apply {
    plugin("kotlin-android")
    plugin("kotlin-kapt")
    plugin("com.google.dagger.hilt.android")
}

android {
    namespace = "com.paranid5.daily_planner"
    compileSdk = 34

    dataBinding {
        enable = true
    }

    defaultConfig {
        applicationId = "com.paranid5.daily_planner"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "0.1.0.0"
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
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.activity:activity-ktx:1.7.2")
    implementation("androidx.fragment:fragment-ktx:1.6.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.6.2")
    implementation("androidx.work:work-runtime:2.8.1")
    implementation("androidx.hilt:hilt-work:1.0.0")
    implementation("androidx.room:room-runtime:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")

    ksp("androidx.room:room-compiler:2.5.2")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    kapt("com.google.dagger:hilt-compiler:2.48")

    implementation("com.google.android.material:material:1.9.0")
    implementation("com.google.dagger:hilt-android:2.48")
    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.10-1.0.13")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")

    implementation("com.vmadalin:easypermissions-ktx:1.0.0")

    arrayOf(
        "io.noties.markwon:core:4.6.2",
        "io.noties.markwon:editor:4.6.2",
        "io.noties.markwon:ext-latex:4.6.2",
        "io.noties.markwon:ext-strikethrough:4.6.2",
        "io.noties.markwon:ext-tables:4.6.2",
        "io.noties.markwon:ext-tasklist:4.6.2",
        "io.noties.markwon:html:4.6.2",
        "io.noties.markwon:image:4.6.2",
        "io.noties.markwon:image-picasso:4.6.2",
        "io.noties.markwon:syntax-highlight:4.6.2",
        "io.noties:prism4j:2.0.0"
    ).forEach {
        implementation(it) {
            exclude(group = "org.jetbrains", module = "annotations-java5")
        }
    }

    kapt("io.noties:prism4j-bundler:2.0.0")

    api("tk.zielony:carbon:0.17.0")
}