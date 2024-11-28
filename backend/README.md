# Task Manager Backend Server

This is a Node.js RESTful server built with Express.js to manage user tasks. The server uses PostgreSQL as the database and includes authentication functionality with JWT for secure access. The instructions below will guide you through setting up and running the server.

---

## Features

- **User Authentication**: Login and registration with secure password hashing and JWT-based token authentication.
- **Task Management**: Create, update, delete, and fetch tasks.
- **Refresh Token Support**: *TODO*.
- **PostgreSQL Integration**: Tasks and user data are stored in a PostgreSQL database.

---

## Prerequisites

1. **Node.js** (v16 or higher)
2. **npm** or **yarn** for package management
3. **PostgreSQL** installed and running

---

## Installation and Setup

### Step 1: Clone the Repository

```bash
git clone https://https://github.com/harish32150/TodoITest.git
cd TodoITest/backend
```

### Step 2: Install Dependencies

```bash
npm install
```

### Step 3: Configure the Environment Variables

Update variables in `.env` file in the project root directory:

```env
PORT=<server-port>
DATABASE_URL=<database-url>
```

Replace `database-url`, `server-port` with actual values.

---

### Step 4: Initialize the Database

Create the PostgreSQL database and tables. Run the SQL script provided in `database/schema.sql` to set up the database schema.

---

### Step 5: Run the Server

Start the server:

```bash
npm start
```

---

## API Endpoints

### **Authentication**
- **POST** `/api/auth/register`  
  Register a new user.

- **POST** `/api/auth/login`  
  Login and receive access and refresh tokens.

### **Tasks**
- **GET** `/api/tasks`  
  Fetch all tasks for the authenticated user.

- **POST** `/api/tasks`  
  Create a new task.

- **PUT** `/api/tasks/:id`  
  Update an existing task.

- **DELETE** `/api/tasks/:id`  
  Delete a task.

## Future Enhancements

- Add unit and integration tests.
- Implement Refresh Token functionality.
- Add pagination and filtering for tasks.