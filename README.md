# 🎓 CampusConnect - Customer Relationship Management (CRM)

CampusConnect is a full-stack Customer Relationship Management (CRM) system developed using **Spring Boot**, **Thymeleaf**, **Spring Data JPA**, and **MySQL**. It is designed for educational institutes to manage students, employees, course sales, inquiries, follow-ups, and administration through a session-based authentication system.

The application follows a **3-module architecture** consisting of **User**, **Employee**, and **Admin** dashboards, each having dedicated functionalities.

---

# 🚀 Features

- Session-Based Authentication
- Three Independent Modules
- Course Management
- Inquiry Management
- Employee Follow-up System
- Course Purchase System
- Feedback Management
- Employee Performance Tracking
- Admin Dashboard
- Ban / Unban Users
- Ban / Unban Employees
- Order Management
- Responsive UI with Thymeleaf
- MySQL Database Integration
- Railway Deployment Ready

---

# 🛠️ Tech Stack

### Backend
- Java 17
- Spring Boot
- Spring MVC
- Spring Data JPA
- Hibernate

### Frontend
- HTML5
- CSS3
- Bootstrap
- Thymeleaf

### Database
- MySQL

### Build Tool
- Maven

### Deployment
- Railway
- GitHub

---

# 📂 Project Structure

```
src
├── controllers
├── services
├── repositories
├── entities
├── dto
├── api
└── resources
    ├── templates
    ├── static
    └── application.properties
```

---

# 👨‍🎓 User Module

Users can:

- Register/Login
- Browse Available Courses
- Purchase Courses
- View Purchased Courses
- Submit Feedback
- Manage Profile
- View Purchase History

---

# 👨‍💼 Employee Module

Employees can:

- Login
- Sell Courses
- Manage Customer Inquiries
- Take Customer Follow-ups
- Update Follow-up Status
- View Customer Feedback
- Track Sales Records

---

# 👨‍💻 Admin Module

The administrator has complete control over the system.

Admin can:

- Manage Users
- Manage Employees
- Manage Courses
- Manage Orders
- Manage Inquiries
- View Feedback
- View Sales Reports
- Ban/Unban Users
- Ban/Unban Employees
- Monitor Complete CRM Activities

---

# 📦 Database Entities

- Admin
- User
- Employee
- Course
- Inquiry
- FollowUps
- Orders
- EmployeeOrder
- Feedback

---

# 🔄 Application Workflow

```
                User Registration/Login
                         │
                         ▼
                  Browse Courses
                         │
                         ▼
                  Purchase Course
                         │
                         ▼
                  Order Generated
                         │
                         ▼
             Employee Handles Follow-up
                         │
                         ▼
                User Gives Feedback
                         │
                         ▼
             Admin Monitors Everything
```

---

# 🔐 Authentication

This project uses **Session-Based Authentication**.

There is **no Spring Security Role-Based Authentication**.

Instead, separate login sessions are maintained for:

- User
- Employee
- Admin

Each module has its own login process and dashboard.


# ⚙️ Installation

Clone the repository

```bash
git clone https://github.com/Jatin-chaurasiya/Customer-Relationship-Management.git
```

Go to project directory

```bash
cd Customer-Relationship-Management
```

Run the project

```bash
./mvnw spring-boot:run
```

or

```bash
mvn spring-boot:run
```

---

# 🗄️ Environment Variables

Configure the following variables before deployment.

```properties
server.port=${PORT}

spring.datasource.url=${MYSQL_URL}
spring.datasource.username=${MYSQLUSER}
spring.datasource.password=${MYSQLPASSWORD}

spring.jpa.hibernate.ddl-auto=update
```

---

# 🚀 Deployment

The project is fully deployable on **Railway**.

Deployment includes:

- Spring Boot Application
- Railway MySQL Database
- Environment Variables
- Custom Domain Support

---

# ⭐ Support

If you found this project useful, please consider giving it a ⭐ on GitHub.
