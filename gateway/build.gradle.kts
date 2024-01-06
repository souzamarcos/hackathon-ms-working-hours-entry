dependencies {
    implementation(project(":entity"))
    implementation(project(":usecase"))

    implementation(rootProject.libs.flyway.core)
    implementation(rootProject.libs.flyway.mysql)
    implementation(rootProject.libs.spring.boot.starter.data.jpa)
    implementation(rootProject.libs.hibernate.validator)
    implementation(rootProject.libs.spring.boot.starter.web)


    implementation(rootProject.libs.aws.dynamodb.enhanced)

    runtimeOnly(rootProject.libs.mysql.connector)
}