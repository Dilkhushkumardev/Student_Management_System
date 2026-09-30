@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-26.0.2"
set "MAVEN_CMD=C:\Program Files\JetBrains\IntelliJ IDEA 2026.1.1\plugins\maven-plugin\lib\maven3\bin\mvn.cmd"
call "%MAVEN_CMD%" clean package -DskipTests
