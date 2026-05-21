@echo off
echo ========================================
echo    Mood Tracker - Complete Setup
echo ========================================
echo.

cd /d "%~dp0"

echo Checking prerequisites...
echo.

if not defined JAVA_HOME (
    for /f "delims=" %%i in ('where java 2^>nul') do set "JAVA_EXE=%%i"
    if defined JAVA_EXE (
        set "JAVA_HOME=%%~dpi.."
    )
)

echo 1. Checking Java...
if not defined JAVA_HOME (
    echo ❌ JAVA_HOME is not set.
    echo Please install Java 21+ from: https://adoptium.net/ or set JAVA_HOME to your JDK folder.
    echo.
    pause >nul
    exit /b 1
) else (
    echo ✅ Java is available at %JAVA_HOME%
)

echo.
echo 2. Checking Maven...
mvn -version 2>nul
if %errorlevel% neq 0 (
    if exist Backend\mvnw.cmd (
        echo ✅ Maven is not installed, but Maven Wrapper is available.
    ) else (
        echo ❌ Maven is not installed!
        echo Please install Maven from: https://maven.apache.org/download.cgi
        echo or use the Maven wrapper by adding Backend\mvnw.cmd to the Backend folder.
        pause
        exit /b 1
    )
) else (
    echo ✅ Maven is installed
)

echo.
echo 3. Checking MongoDB...
mongod --version 2>nul
if %errorlevel% neq 0 (
    echo ❌ MongoDB is not installed!
    echo Installing MongoDB via Chocolatey...
    echo.

    choco install mongodb -y
    if %errorlevel% neq 0 (
        echo Failed to install MongoDB via Chocolatey.
        echo Please install manually from: https://www.mongodb.com/try/download/community
        echo Or use MongoDB Atlas (cloud): https://www.mongodb.com/atlas
        pause
        exit /b 1
    )

    echo Starting MongoDB service...
    net start MongoDB 2>nul
    if %errorlevel% neq 0 (
        echo Warning: Could not start MongoDB service automatically.
        echo You may need to start it manually: net start MongoDB
    )
) else (
    echo ✅ MongoDB is installed
    sc query MongoDB | find "RUNNING" >nul
    if %errorlevel% neq 0 (
        echo Starting MongoDB service...
        net start MongoDB 2>nul
        if %errorlevel% neq 0 (
            echo Warning: Could not start MongoDB service.
            echo You may need to start it manually: net start MongoDB
        )
    ) else (
        echo ✅ MongoDB service is running
    )
)

echo.
echo ========================================
echo    Building Backend Application
echo ========================================
echo.

cd Backend

echo Building project...
if exist mvnw.cmd (
    .\mvnw.cmd clean compile -q
) else (
    mvn clean compile -q
)
if %errorlevel% neq 0 (
    echo ❌ Build failed! Check the error messages above.
    pause
    exit /b 1
)

echo.
echo ========================================
echo    Starting Mood Tracker Backend
echo ========================================
echo.
echo Backend API will be available at: http://localhost:8081
echo Frontend can be opened at: Frontend/moodtrack.html
echo.
echo Press Ctrl+C to stop the backend server
echo.

if exist mvnw.cmd (
    .\mvnw.cmd spring-boot:run
) else (
    mvn spring-boot:run
)
