import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("com.google.cloud.tools.jib") version "3.4.0"
	id("org.springframework.boot") version "3.2.0"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.20"
	kotlin("plugin.spring") version "1.9.20"
}

group = "com.example.demo"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

jib{
	val profile : String = System.getenv("COMPOSE_SPRING_PROFILES_ACTIVE") as? String ?: "local"

	from {
		image = "amazoncorretto:17"

		// M1 Mac을 사용할 경우 아래 내용을 추가.
//            platforms {
//                platform {
//                    architecture = "arm64"
//                    os = "linux"
//                }
//            }
	}

	to {
		image = "chagchagchag/jib-github-action-single-module"
		// image tag 는 여러개 지정 가능하다.
		tags = setOf("latest")
	}

	container{
		creationTime = "USE_CURRENT_TIMESTAMP"

		// jvm 옵션
		jvmFlags = listOf(
			"-Dspring.profiles.active=${profile}",
			"-XX:+UseContainerSupport",
            "-XX:+UseG1GC",
            "-verbose:gc",
//            "-XX:+PrintGCDetails",
			"-Dserver.port=8080",
			"-Dfile.encoding=UTF-8",
		)

		// 컨테이너 입장에서 외부로 노출할 포트
		ports = listOf("8080")

		labels = mapOf(
			"maintainer" to "chagachagchag.dev@gmail.com"
		)
	}

	extraDirectories{

	}
}