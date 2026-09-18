javac --release 21 -d . ./src/AtomicLongSum.java

docker run --rm --cpus="0.25" --memory="512m" -v "%cd%:/app" -v "%cd%\output:/app/output" -w /app eclipse-temurin:21 java -XX:MaxRAMPercentage=100 AtomicLongSum