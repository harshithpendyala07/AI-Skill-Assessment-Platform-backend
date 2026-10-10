@echo off
echo ========================================================
echo Launching AI-Powered Skill Assessment Platform (Full Stack)
echo ========================================================
start "AI Skill Platform - Backend (Spring Boot)" cmd /k "cd /d %~dp0\backend && mvn spring-boot:run"
timeout /t 8 /nobreak
start "AI Skill Platform - Frontend (React Vite)" cmd /k "cd /d %~dp0\frontend && npm run dev"
echo Backend launched at http://localhost:8080 (Swagger: http://localhost:8080/swagger-ui.html)
echo Frontend launched at http://localhost:5173
pause
