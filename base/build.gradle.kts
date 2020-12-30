plugins {
    kotlin("jvm")
    kotlin("kapt")
}

version = "0.1"
group = "beansnapper.inertia"

val kotlinVersion=project.properties.get("kotlinVersion")
repositories {
    mavenCentral()
    jcenter()
}


dependencies {
    implementation("io.github.microutils:kotlin-logging:1.7.8")
    implementation("org.mongodb:mongodb-driver-sync:4.1.1")
}

java {
    sourceCompatibility = JavaVersion.toVersion("11")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }


}

