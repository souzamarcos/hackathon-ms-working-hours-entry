dependencies {
    implementation(project(":entity"))
    implementation(project(":usecase"))
    implementation(project(":messenger"))

    implementation(rootProject.libs.spring.beans)
    implementation(rootProject.libs.openapi)
}

