pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {

    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        mavenCentral()
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://maven.aliyun.com/repository/jcenter") }
        maven { url = uri("https://maven.aliyun.com/repository/central") }
        // maven { url 'https://maven.aliyun.com/repository/gradle-plugin") }
        maven { url = uri("https://maven.aliyun.com/nexus/content/groups/public") }

        maven {
            // All of React Native (JS, Obj-C sources, Android binaries) is installed from npm
            url = uri("$rootDir/../node_modules/react-native/android")
        }
        maven {
            // Android JSC is installed from npm
            url = uri("$rootDir/../node_modules/jsc-android/dist")
        }
        mavenCentral {
            // We don't want to fetch react-native from Maven Central as there are
            // older versions over there.
            content {
                excludeGroup("com.facebook.react")
            }
        }
        google()
        maven {
            url = uri("https://www.jitpack.io")
        }
    }
}

rootProject.name = "gchat"
include(":app")
 