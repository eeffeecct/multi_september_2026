javac ./src/AtomicLongSum.java

docker run --rm --cpus="4" --memory="512m" -v "%cd%:/app" -v "%cd%\output:/app/output" -w /app eclipse-temurin:21 java -XX:MaxRAMPercentage=100 AtomicLongSum