dependencyManagement {
    imports {
        mavenBom("io.awspring.cloud:spring-cloud-aws-dependencies:3.0.1")
    }
}

dependencies {
    implementation(project(":blink-core"))

    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Database
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("com.h2database:h2")

    // QueryDSL
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
    kapt("jakarta.persistence:jakarta.persistence-api")

    // Security
    implementation("io.jsonwebtoken:jjwt-api:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.2")

    // Firebase
    implementation("com.google.firebase:firebase-admin:9.1.1")

    // Web Scraping
    implementation("org.jsoup:jsoup:1.18.1")
    implementation("org.seleniumhq.selenium:selenium-java:4.14.1")

    // API Documentation
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
