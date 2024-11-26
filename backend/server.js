const express = require('express');
const bodyParser = require('body-parser');
const authRoutes = require('./routes/auth');
const taskRoutes = require('./routes/tasks');

require('dotenv').config();

const app = express();

app.use(bodyParser.json());

// Routes
app.use('/auth', authRoutes);
app.use('/tasks', taskRoutes);

// Start server
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`Server running on port ${PORT}`));