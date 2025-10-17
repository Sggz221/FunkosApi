plugins {
	java
	id("org.springframework.boot") version "3.5.6"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"
description = "Funkos"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {

	implementation("org.springframework.boot:spring-boot-starter-web")

	//Validación
	implementation("org.springframework.boot:spring-boot-starter-validation")

	//Caché
	implementation("org.springframework.boot:spring-boot-starter-cache")

	//Lombok
	implementation("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	//Test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// JPA
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("com.h2database:h2") // base de datos a usar, puede ser otra
}

tasks.withType<Test> {
	useJUnitPlatform()
}
