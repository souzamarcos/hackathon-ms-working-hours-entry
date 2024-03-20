plugins {
    application
    id("org.springframework.boot") version "3.0.0"
    id("io.spring.dependency-management") version "1.1.0"
}

application {
    mainClass.set("com.fiap.hackathon.application.boot.BurgerApplication")
    applicationDefaultJvmArgs = listOf(
        "-Duser.timezone=America/Sao_Paulo",
        "--add-opens=java.base/java.time=ALL-UNNAMED"
    )
}

dependencies {
    implementation(project(":entity"))
    implementation(project(":usecase"))
    implementation(project(":controller"))
    implementation(project(":api"))
    implementation(project(":messenger"))

    implementation(rootProject.libs.spring.boot.starter.web)
    implementation(rootProject.libs.spring.messaging)
    implementation(rootProject.libs.spring.cloud.starter.aws)
    implementation(rootProject.libs.spring.cloud.starter.aws.messaging)
}