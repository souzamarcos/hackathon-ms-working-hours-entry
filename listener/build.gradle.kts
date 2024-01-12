dependencies {
    implementation(project(":entity"))
    implementation(project(":usecase"))
    implementation(project(":controller"))

    implementation(rootProject.libs.spring.beans)
    implementation(rootProject.libs.openapi)
    implementation(rootProject.libs.spring.messaging)
    implementation(rootProject.libs.spring.cloud.starter.aws)
    implementation(rootProject.libs.spring.cloud.starter.aws.messaging)
}
