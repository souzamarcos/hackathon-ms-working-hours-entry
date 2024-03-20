dependencies {
    implementation(project(":entity"))
    implementation(rootProject.libs.openapi)

    implementation("com.auth0:java-jwt:4.2.1")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation(platform("software.amazon.awssdk:bom:2.20.56"))
    implementation("software.amazon.awssdk:s3")
    implementation("software.amazon.awssdk:sso")
    implementation("software.amazon.awssdk:ssooidc")
    implementation("software.amazon.awssdk:secretsmanager:2.21.10")
}