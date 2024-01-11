dependencies {
    implementation(project(":entity"))
    implementation(project(":usecase"))
    implementation(project(":gateway"))
    implementation(project(":messenger"))

    implementation(rootProject.libs.spring.beans)
    implementation(rootProject.libs.openapi)
}

