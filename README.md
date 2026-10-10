# AI-Powered Technical Skill Assessment & Mock Interview Platform

A full-stack, enterprise-grade web application built to help students assess and elevate their technical skills for academic and career development, based on the approved project abstract and architecture workflow diagram.

---

## 🏗️ Architecture & Technology Stack

| Tier | Technologies |
| :--- | :--- |
| **Frontend** | React 18, Vite, React Router DOM, Tailwind CSS, Lucide React, Axios, Canvas Confetti, Web Speech API (Voice Recognition & Speech Synthesis) |
| **Backend** | Java 21 LTS, Spring Boot 3.3.4, Spring Security 6, Stateless JWT (JJWT), Spring Data JPA, Hibernate ORM, Maven |
| **Database** | MySQL Server 8.0 (`ai_skill_platform`), Hibernate DDL auto-generation |
| **AI & RAG** | Retrieval-Augmented Generation (RAG) chunking & semantic retrieval pipeline, AI Speech-to-Text & Text-to-Speech mock interview evaluation engine, automated resume competency matcher |
| **API Documentation** | OpenAPI 3.0 / Swagger UI (`/swagger-ui.html`) |

---

## 📁 Project Structure

```
project tt/
├── PROJECT ABSTRACT.docx         # Project abstract specification
├── start-all.bat                 # Launches both Backend & Frontend simultaneously
├── start-backend.bat             # Starts Spring Boot backend on port 8080
├── start-frontend.bat            # Starts React Vite frontend on port 5173
├── README.md                     # Documentation
│
├── backend/                      # Java Spring Boot Backend
│   ├── pom.xml                   # Maven dependencies (Spring Boot, Security, JPA, MySQL, JWT, Swagger)
│   └── src/main/
│       ├── java/com/aiskill/platform/
│       │   ├── AiSkillPlatformApplication.java
│       │   ├── config/           # SecurityConfig, JwtService, JwtAuthFilter, ApplicationConfig, OpenApiConfig
│       │   ├── controller/       # Auth, Assessment, Coding, Interview, RAG, Resume, Analytics, Admin
│       │   ├── dto/              # Request & Response DTOs
│       │   ├── model/            # User, Role, Test, Question, Result, CodingProblem, Submission, Interview, RAG Docs
│       │   ├── repository/       # Spring Data JPA Repositories
│       │   └── service/          # Core Business Logic, Evaluation Engines, RAG Pipeline, Initializer
│       └── resources/
│           └── application.yml   # MySQL datasource, JPA, and JWT configurations
│
└── frontend/                     # React.js Modern SPA
    ├── package.json
    ├── vite.config.js            # Dev server with backend API proxy (/api -> :8080)
    ├── tailwind.config.js        # Modern dark-mode styling palette
    ├── index.html
    └── src/
        ├── App.jsx               # Protected route tree & application shell
        ├── main.jsx
        ├── index.css
        ├── context/AuthContext.jsx # JWT session provider
        ├── services/api.js       # Axios client with JWT bearer interceptors
        ├── components/           # Navbar, Footer, ProtectedRoute, Timer, Badges
        └── pages/
            ├── Home.jsx          # Platform landing page & features showcase
            ├── Login.jsx         # Sign in with one-click demo login buttons
            ├── Register.jsx      # Registration with student/faculty roles & career goals
            ├── Dashboard.jsx     # Student cockpit with radar progress & quick launch
            ├── SkillTests.jsx    # Skill assessment catalog (Java, Spring Boot, React, SQL)
            ├── TakeTest.jsx      # Timed MCQ assessment with live countdown & palette
            ├── TestResult.jsx    # Scorecard, confetti, AI analysis & question review
            ├── CodingArena.jsx   # Algorithmic challenges directory
            ├── CodeProblemSolve.jsx # LeetCode-style multi-language live code solver
            ├── MockInterview.jsx # Mock interview configuration & session history
            ├── InterviewSession.jsx # Voice-enabled AI interview room with real-time feedback
            ├── InterviewReport.jsx  # Detailed interview performance scorecard & transcripts
            ├── StudyAssistant.jsx   # AI Study Assistant with RAG grounding & note uploads
            ├── ResumeAnalyzer.jsx   # Resume benchmark match, skill gaps & learning paths
            ├── Leaderboard.jsx      # Global podium and ranked student directory
            └── AdminPanel.jsx       # Admin dashboard, curriculum builder & user directory
```

---

## ⚡ Quick Start Instructions

### Option 1: One-Click Startup (Windows)
Double-click `start-all.bat` or run:
```powershell
.\start-all.bat
```
This opens two terminal windows:
1. **Backend Server**: `http://localhost:8080`
2. **Frontend UI**: `http://localhost:5173`

---

### Option 2: Manual Startup

#### Step 1: Start Backend (Spring Boot)
Ensure MySQL Server 8.0 is running on port 3306 (database `ai_skill_platform` with user `root` and password `root`).
```powershell
cd backend
mvn spring-boot:run
```
- API Base URL: `http://localhost:8080/api`
- Swagger UI Documentation: `http://localhost:8080/swagger-ui.html`

#### Step 2: Start Frontend (React + Vite)
In a separate terminal:
```powershell
cd frontend
npm run dev
```
- Web Application: `http://localhost:5173`

---

## 🔑 Pre-Configured Demo Credentials

The platform initializes the database with pre-configured accounts on initial startup:

| Account Type | Email | Password | Role |
| :--- | :--- | :--- | :--- |
| **Demo Student** | `student@skillai.com` | `student123` | `ROLE_STUDENT` |
| **Platform Administrator** | `admin@skillai.com` | `admin123` | `ROLE_ADMIN` |

*(Quick-login buttons are also available directly on the `/login` page for fast testing).*

---

## 🧩 Core Modules & Features

### 1. User Workflow & Authentication
- Secure JWT authentication with BCrypt password hashing.
- Role-based authorization (`ROLE_STUDENT` and `ROLE_ADMIN`).
- Profile management with customizable target engineering roles and bio.

### 2. Dashboard Cockpit
- Real-time performance indicators: Tests Taken, Average Score, Mock Interviews, Problems Solved, and Current Rank.
- Visual competency mastery progress across Java, Spring Boot, React, SQL, and System Design.
- AI-tailored study recommendations highlighting focus areas.

### 3. Skill Assessment & MCQ Engine
- Categorized skill assessments with difficulty tags (Beginner, Intermediate, Advanced).
- Live countdown timer with auto-submit protection.
- Question navigation palette tracking answered and remaining questions.
- Instant score calculation, confetti reward animations, and question-by-question review with comprehensive technical explanations.

### 4. Coding Arena
- In-browser code editor with support for Java, Python, and JavaScript.
- Sample and hidden test case evaluation engine with time measurements and pass/fail diffs.
- Algorithmic problem bank covering Arrays, Strings, HashMaps, Two Pointers, and Sliding Windows.

### 5. AI Mock Interview Simulator
- Role-specific dynamic interview sessions (Full Stack, Backend Java, Frontend React, AI/ML).
- **Text-to-Speech (TTS):** AI reads questions aloud via Web Speech Synthesis.
- **Voice Recognition (STT):** Candidates speak their answers via microphone dictation.
- Real-time AI evaluation on each response: Technical accuracy, articulation clarity, and constructive feedback.
- Comprehensive end-of-interview report card with score breakdown and full transcript.

### 6. AI Study Assistant (RAG Pipeline)
- Conversational study assistant answering technical questions with verified citations.
- Pre-loaded with core guides (Java 21, Spring Boot 3, React 18, SQL Optimization).
- Dynamic study material uploader: Chunks and indexes custom syllabi and notes into the knowledge base.

### 7. AI Resume & Skill Gap Analyzer
- Evaluates candidate resume text against target engineering roles.
- Computes profile match percentage, identifies matched competencies, and flags missing skill gaps.
- Recommends tailored platform assessments to bridge identified gaps.

### 8. Analytics & Global Leaderboard
- Podium rankings (Gold, Silver, Bronze) and ranked directory with search filtering.
- Point calculations based on test scores and activity completions.

### 9. Admin Operations
- Platform-wide statistics (Users, Tests, Questions, Coding Problems, Submissions).
- Admin interface to create new assessments and manage existing curriculums.
- Registered student and faculty directory.
