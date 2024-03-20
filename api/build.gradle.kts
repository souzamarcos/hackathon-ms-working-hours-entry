dependencies {
    implementation(project(":entity"))
    implementation(project(":usecase"))
    implementation(project(":controller"))

    implementation(rootProject.libs.spring.boot.starter.web)
    implementation(rootProject.libs.spring.boot.starter.security)
    implementation(rootProject.libs.openapi)

    implementation("com.auth0:java-jwt:4.2.1")
}