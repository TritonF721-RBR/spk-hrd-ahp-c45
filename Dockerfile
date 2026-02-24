# 1. Gunakan mesin Java 17 (Sesuaikan jika kamu pakai Java 21)
FROM eclipse-temurin:21-jdk-alpine

# 2. Tentukan folder kerja di dalam Docker
WORKDIR /app

# 3. Salin file JAR hasil build ke dalam folder kerja Docker
COPY target/*.jar app.jar

# 4. Perintah untuk menyalakan aplikasinya
ENTRYPOINT ["java", "-jar", "app.jar"]