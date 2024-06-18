
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.your.orgname.judgment"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.your.orgname.judgment"
        minSdk = 24
        targetSdk = 34
        versionCode = 8
        versionName = "8.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        viewBinding = true
//    }
//    kapt {
//        generateStubs = true
//    }
}
//allprojects {
//    repositories {
//        maven { ("https://jitpack.io") }
//    }
//}
//allprojects {
//repositories {
//    google()
//    maven {
////             "https://www.jitpack.io"
//        url = uri( "https://jitpack.io")
//    }
//}
//}

//allprojects {
//    maven{
//        url = uri( "https://jitpack.io")
//    }
//
//}


//repositories {
//        google()
//        jcenter()
//    maven(url = "https://jitpack.io")
//}

//repositories {
//    google()
//    mavenCentral()
//
//    maven {
//        url = uri("https://jitpack.io")
//    }
//
//}
//



//
//dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
//    repositories {
//        google()
//        mavenCentral()
//        jcenter() // Warning: this repository is going to shut down soon
//        // 追記
//        maven {
//            maven(url = "https://jitpack.io")
//        }
//    }
//}
//    allprojects {
//        repositories {
//            google()
//            jcenter()
//
//
//                maven{
//                    url = uri("https://jitpack.io")
//                }
//
//        }
//    }
dependencies {

    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation ("com.google.mlkit:image-labeling:17.0.0")
    implementation ("androidx.camera:camera-camera2:1.0.0-beta07")
    implementation ("androidx.camera:camera-lifecycle:1.0.0-beta07")
    implementation ("androidx.camera:camera-view:1.0.0-alpha14")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}}
dependencies {
    implementation("androidx.camera:camera-view:1.3.3")
}
//allprojects {
//    repositories {
//        google()
//        maven {
////             "https://www.jitpack.io"
//            url = uri( "https://jitpack.io")
//        }
//    }
//}
