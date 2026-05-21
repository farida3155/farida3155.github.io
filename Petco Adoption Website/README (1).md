# 🐾 PetCo – Full-Stack Pet Adoption Platform (Node.js, Express, MongoDB)


A full-stack web application for a multi-feature pet adoption platform.  
Built using Node.js, Express, MongoDB, and server-rendered views, this system handles authentication, pet listings, adoption workflows, donations, and admin operations.

---

## 📷 Demo

▶️ Watch demo video:  
[Click here to view video](https://drive.google.com/file/d/11T0qHMGNsyLUeM0CJ_NrzVE751D1sJ24/view?usp=sharing)


---

## 🚀 Features
- User Signup & Login system  
- Secure password hashing using bcrypt  
- Password reset functionality (forgot/reset password)  

- 🐾 Pet Management System  
  - Admin can perform full CRUD operations on pets  
  - Users can submit pets for adoption  
  - Image upload support by multer  
  - Detailed pet information required (breed, type, location, etc.)  

- ❤️ Adoption System  
  - Submit adoption requests  
  - Track adoption requests and history  

- 💬 Communication Features  
  - Contact form and request handling  
  - Newsletter subscription system  

- 💸 Donation System  
  - Submit and manage donation requests (regular periodic packages or one time donations)

- 🛠️ Admin Features  
  - Admin dashboard & insights  
  - Manage pets, users, and requests  

- RESTful API architecture   

---

## ⚙️ Tech Stack
- **Backend:** Node.js, Express.js  
- **Database:** MongoDB (Mongoose)  
- **Authentication:** Custom authentication logic  
- **Security:** bcrypt  
- **File Uploads:** multer  
- **Environment:** dotenv  

---

## 📁 Project Structure
```text
.
├── models/
├── routes/
├── controllers/
├── middleware/
├── config/
├── public/uploads # Uploaded images
├── .env
├── server.js
└── README.md
```
---

## 🔌 API Endpoints

### 🔐 Authentication
- `POST /api/signup` → Register new user  
- `POST /api/signin` → Login user  
- `POST /api/forgot-password` → Request password reset  
- `POST /api/reset-password` → Reset password  

---

### 🐾 Pets
- `GET {API_URL}/pets` → Get available pets  
- `GET {API_URL}/pets/all` → Get all pets (admin)  
- `GET {API_URL}/pets/adopted` → Get adopted pets (admin)  
- `GET {API_URL}/pets/:id` → Get pet by ID  
- `POST {API_URL}/pets` → Add new pet (with image upload)  
- `PUT {API_URL}/pets/:id` → Update pet  
- `DELETE {API_URL}/pets/:id` → Delete pet  

---

### ❤️ Adoption
- `POST /api/adoption` → Submit adoption request  
- `GET /api/adoption-requests` → View adoption requests  
- `GET /api/adoption-history` → View adoption history  

---

### 💬 Contact
- `POST /api/contact` → Send message  
- `GET /api/contact-requests` → View messages  

---

### 💸 Donations
- `POST /api/donations` → Submit donation  
- `GET /api/donations-requests` → View donation requests  

---

### 📰 Newsletter
- `POST /api/newsletter` → Subscribe to newsletter  

---

### 🛠️ Admin
- `GET /api/admin/insights` → Platform analytics  
- `CRUD /api/admins` → Manage admin accounts  
- `/admin` → Admin interface routes  
- `/admin-api` → Additional admin operations  

---

## 🛠️ How to Run

1. Clone the repository:
```bash
git clone https://github.com/faridaseif/petco.git
Install dependencies:
npm install
Create a .env file:
DB_CONNECTION=your_mongodb_connection
API_URL=/api/v1
PORT=3000
Run the server:
npm start

Server runs on:

https://localhost:3000
```
---

##  🧠 Key Takeaways
Built a modular REST API using Express
Implemented secure password handling with bcrypt
Managed file uploads using multer
Designed multi-feature backend (adoption, donations, admin)
Structured project using MVC architecture
