plugins {
	java
	id("org.sonarqube") version "3.5.0.2730"
	jacoco
}

repositories {
	mavenCentral()
}

subprojects {

	apply {
		plugin("java")
		plugin("jacoco")
	}

	group = "com.fiap"
	version = "0.0.1-SNAPSHOT"
	java.sourceCompatibility = JavaVersion.VERSION_19

	repositories {
		mavenCentral()
	}

	dependencies {
		implementation(rootProject.libs.gson)
		implementation("org.jacoco:org.jacoco.ant:0.8.10")

		//Test
		testImplementation(rootProject.libs.spring.boot.starter.test)
		testImplementation(rootProject.libs.cucumber.java)
		testImplementation(rootProject.libs.cucumber.junit)
		testImplementation(rootProject.libs.cucumber.spring)
		testImplementation(rootProject.libs.cucumber.junit.platform.engine)
		testImplementation(rootProject.libs.h2)
		testImplementation("org.junit.platform:junit-platform-suite:1.9.3")
		testImplementation("io.rest-assured:rest-assured:5.3.0")
		testImplementation("io.rest-assured:json-schema-validator:5.3.1")
	}

	tasks.withType<Test> {
		useJUnitPlatform()
		testLogging {
			events("passed", "skipped", "failed")
		}
		finalizedBy(tasks.jacocoTestReport)
	}

	tasks.jacocoTestReport {
		dependsOn(tasks.test) // tests are required to run before generating the report
		reports{
			xml.required.set(true)
		}
	}
}

tasks.register<JacocoReport>("codeCoverageReport") {
	// If a subproject applies the 'jacoco' plugin, add the result it to the report
	subprojects {
		val subproject = this
		subproject.plugins.withType<JacocoPlugin>().configureEach {
			subproject.tasks.matching { it.extensions.findByType<JacocoTaskExtension>() != null }.configureEach {
				val testTask = this
				sourceSets(subproject.sourceSets.main.get())
				executionData(testTask)
			}

			// To automatically run `test` every time `./gradlew codeCoverageReport` is called,
			// you may want to set up a task dependency between them as shown below.
			// Note that this requires the `test` tasks to be resolved eagerly (see `forEach`) which
			// may have a negative effect on the configuration time of your build.
			subproject.tasks.matching { it.extensions.findByType<JacocoTaskExtension>() != null }.forEach {
				rootProject.tasks["codeCoverageReport"].dependsOn(it)
			}
		}
	}

	// enable the different report types (html, xml, csv)
	reports {
		// xml is usually used to integrate code coverage with
		// other tools like SonarQube, Coveralls or Codecov
		xml.required.set(true)

		// HTML reports can be used to see code coverage
		// without any external tools
		html.required.set(true)
	}
}

tasks.test {
	dependsOn(tasks.named("codeCoverageReport"))
}

sonarqube {
	properties {
		property("sonar.exclusions", "**/entity/**,**/secret/**,**/*Configuration.java")
	}
}
