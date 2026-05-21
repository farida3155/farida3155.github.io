# Mello 🍃

**A comprehensive mental health and habit tracking platform designed to bring calm to your daily routine.**

Mello is a full-stack web application aimed at helping users manage their mental well-being through mood tracking, habit formation, and personalized wellness recommendations. With a soothing interface featuring interactive 3D elements and mindfulness exercises, Mello provides a safe space for personal growth.

## 🌟 Key Features

- **Mood Tracking**: Log your daily moods and visualize your emotional journey over time.
- **Habit Management**: Build and maintain positive routines with streak tracking and activity logging.
- **Interactive Dashboard**: A comprehensive overview of your mental wellness journey.
- **Mindfulness Exercises**: Includes interactive tools like the "Breathing Orb" and "Calm Clouds" for guided relaxation.
- **Sanctuary Map**: A 3D interactive map to navigate the app with a personalized avatar.
- **Analytics & Insights**: Detailed charts and graphs to understand your mental health trends.
- **Smart Notifications**: Daily email summaries and in-app alerts to keep you engaged without feeling overwhelmed.
- **Personalized Recommendations**: Actionable wellness suggestions based on your logged data.

## 🛠️ Tech Stack

**Frontend:**
- [React](https://react.dev/) - UI Library
- [Vite](https://vitejs.dev/) - Build Tool
- [React Router](https://reactrouter.com/) - Navigation
- [Three.js](https://threejs.org/) & [React Three Fiber](https://docs.pmnd.rs/react-three-fiber/) - 3D Interactions
- [Framer Motion](https://www.framer.com/motion/) - Fluid Animations
- [Chart.js](https://www.chartjs.org/) - Data Visualization

**Backend:**
- [Spring Boot 3](https://spring.io/projects/spring-boot) - Java Framework
- [Java 17](https://jdk.java.net/17/) - Core Language
- [MongoDB](https://www.mongodb.com/) - NoSQL Database
- [Spring Security](https://spring.io/projects/spring-security) - Authentication & Authorization
- JavaMailSender - Email Notifications

## 🚀 Getting Started

### Prerequisites
- Node.js (v18 or higher)
- Java 17
- MongoDB (Running locally or via Atlas)
- Maven

### Installation & Setup

1. **Clone the repository**
   ```bash
    git clone https://github.com/farahkhaledl/mello-Software-design-project.git
   cd mello-Software-design-project
   ```

2. **Backend Setup**
   Configure your MongoDB connection in `backend/src/main/resources/application.properties`, then start the Spring Boot server:
   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```

3. **Frontend Setup**
   Open a new terminal window, install dependencies, and start the development server:
   ```bash
   # Return to project root if needed
   npm install
   npm run dev
   ```

## 📁 Project Structure

```text
mello/
├── backend/                # Spring Boot application
│   ├── src/main/java       # Java source code
│   └── src/main/resources  # Application properties
├── src/                    # React frontend application
│   ├── components/         # Reusable UI components
│   ├── pages/              # Main route components (Dashboard, Mood, Habits)
│   ├── services/           # API integration and external services
│   ├── utils/              # Helper functions
│   └── styles/             # Global CSS
└── package.json            # Frontend dependencies and scripts
```
## 👥 Team Collaboration

- **Malak Moataz**: Analytics and Recommendation Module/Page
- **Sama Albaghdady**: Mood Tracking Module/Page
- **Farida Hossam**: Landing Page, Home Page, and Authentication Page
- **Farah Khaled**: Habit Management Module/Page
- **Nourhan Yasser**: Notification Module/Page and Email Service
