@echo off
echo ===============================================================================
echo   SmartAttend - Subject Wise Attendance Management System
echo   Bihar Engineering University (BEU), Patna - Session 2026-2030
echo ===============================================================================

set "JAVA_HOME=C:\Program Files\Java\jdk-26.0.2"
set "MAVEN_CMD=C:\Program Files\JetBrains\IntelliJ IDEA 2026.1.1\plugins\maven-plugin\lib\maven3\bin\mvn.cmd"

echo.
echo [1/3] Compiling and packaging SmartAttend application...
call "%MAVEN_CMD%" clean package -DskipTests

if %ERRORLEVEL% neq 0 (
    echo [ERROR] Maven build failed!
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo [2/3] Starting SmartAttend Spring Boot Application on http://localhost:8080 ...
echo [INFO] Demo Logins:
echo        Admin:   admin   / Admin@123
echo        Faculty: faculty / Faculty@123
echo        Student: student / Student@123
echo.
"%JAVA_HOME%\bin\java.exe" -Dnet.bytebuddy.experimental=true -jar target\smart-attend-1.0.0.jar
pause
