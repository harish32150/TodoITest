const express = require('express');
const pool = require('../database');
const authenticate = require('../middleware/authenticate');

const router = express.Router();

// Get all tasks for the authenticated user
router.get('/', authenticate, async (req, res) => {
    try {
        const tasks = await pool.query('SELECT * FROM tasks WHERE user_id = $1', [req.user.id]);
        res.json(tasks.rows);
    } catch (error) {
        res.status(500).json({ message: 'Error fetching tasks', error });
    }
});

// Create a new task
router.post('/', authenticate, async (req, res) => {
    const { label } = req.body;

    try {
        const result = await pool.query(
            'INSERT INTO tasks (label, user_id) VALUES ($1, $2) RETURNING *',
            [label, req.user.id]
        );
        res.status(201).json(result.rows[0]);
    } catch (error) {
        res.status(500).json({ message: 'Error creating task', error });
    }
});

// Update a task
router.put('/:id', authenticate, async (req, res) => {
    const { id } = req.params;
    const { label, completed } = req.body;

    try {
        const result = await pool.query(
            'UPDATE tasks SET label = $1, completed = $2 WHERE id = $3 AND user_id = $4 RETURNING *',
            [label, completed, id, req.user.id]
        );

        if (result.rows.length === 0) {
            return res.status(404).json({ message: 'Task not found' });
        }

        res.json(result.rows[0]);
    } catch (error) {
        res.status(500).json({ message: 'Error updating task', error });
    }
});

// Delete a task
router.delete('/:id', authenticate, async (req, res) => {
    const { id } = req.params;

    try {
        const result = await pool.query(
            'DELETE FROM tasks WHERE id = $1 AND user_id = $2 RETURNING *',
            [id, req.user.id]
        );

        if (result.rows.length === 0) {
            return res.status(404).json({ message: 'Task not found' });
        }

        res.json(result.rows[0]);
    } catch (error) {
        res.status(500).json({ message: 'Error deleting task', error });
    }
});

module.exports = router;