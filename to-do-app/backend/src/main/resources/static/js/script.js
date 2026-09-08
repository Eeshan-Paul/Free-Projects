const apiBase = '/todo';
const taskTableBody = document.getElementById('taskTableBody');
const taskForm = document.getElementById('taskForm');
const searchInput = document.getElementById('searchInput');
const searchBtn = document.getElementById('searchBtn');
const loadAllBtn = document.getElementById('loadAllBtn');
const messageBox = document.getElementById('messageBox');
const cancelEditBtn = document.getElementById('cancelEdit');

const setMessage = (message, type = 'success') => {
    messageBox.textContent = message;
    messageBox.className = `message ${type}`;
    messageBox.classList.remove('hidden');
};

const clearMessage = () => {
    messageBox.className = 'message hidden';
    messageBox.textContent = '';
};

const renderTasks = (tasks) => {
    taskTableBody.innerHTML = '';

    if (!tasks || tasks.length === 0) {
        taskTableBody.innerHTML = '<tr><td colspan="4">No tasks found</td></tr>';
        return;
    }

    tasks.forEach(task => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${task.id}</td>
            <td>${task.title}</td>
            <td>${task.description}</td>
            <td>
                <div class="actions">
                    <button class="edit-btn" data-id="${task.id}" data-title="${task.title}" data-description="${task.description}">Edit</button>
                    <button class="delete-btn" data-id="${task.id}">Delete</button>
                </div>
            </td>
        `;
        taskTableBody.appendChild(row);
    });

    document.querySelectorAll('.edit-btn').forEach(button => {
        button.addEventListener('click', () => {
            document.getElementById('taskId').value = button.dataset.id;
            document.getElementById('title').value = button.dataset.title;
            document.getElementById('description').value = button.dataset.description;
            cancelEditBtn.classList.remove('hidden');
            document.getElementById('title').focus();
        });
    });

    document.querySelectorAll('.delete-btn').forEach(button => {
        button.addEventListener('click', async () => {
            const id = button.dataset.id;
            await deleteTask(id);
        });
    });
};

const fetchJson = async (url, options = {}) => {
    const response = await fetch(url, {
        headers: {
            'Content-Type': 'application/json',
            ...(options.headers || {})
        },
        ...options
    });

    if (!response.ok) {
        throw new Error('Request failed');
    }

    return response.json();
};

const loadAllTasks = async () => {
    try {
        clearMessage();
        const result = await fetchJson(`${apiBase}/getAllTasks`);
        renderTasks(result.data || []);
    } catch (error) {
        setMessage('Failed to load tasks', 'error');
    }
};

const searchTasks = async () => {
    const query = searchInput.value.trim();

    if (!query) {
        loadAllTasks();
        return;
    }

    try {
        clearMessage();
        const result = await fetchJson(`${apiBase}/getTaskByTitleOrDesc?query=${encodeURIComponent(query)}`);
        if (result.success) {
            renderTasks(result.data || []);
        } else {
            renderTasks([]);
            setMessage('No matching tasks found', 'error');
        }
    } catch (error) {
        setMessage('Search failed', 'error');
    }
};

const saveTask = async (event) => {
    event.preventDefault();

    const taskId = document.getElementById('taskId').value;
    const task = {
        title: document.getElementById('title').value.trim(),
        description: document.getElementById('description').value.trim()
    };

    if (!task.title || !task.description) {
        setMessage('Title and description are required', 'error');
        return;
    }

    try {
        clearMessage();

        if (taskId) {
            const payload = {
                title: task.title,
                description: task.description
            };

            await fetchJson(`${apiBase}/updateTaskById/${taskId}`, {
                method: 'PATCH',
                body: JSON.stringify(payload)
            });
            setMessage('Task updated successfully');
        } else {
            await fetchJson(`${apiBase}/saveTask`, {
                method: 'POST',
                body: JSON.stringify(task)
            });
            setMessage('Task saved successfully');
        }

        taskForm.reset();
        cancelEditBtn.classList.add('hidden');
        loadAllTasks();
    } catch (error) {
        setMessage('Failed to save task', 'error');
    }
};

const deleteTask = async (id) => {
    try {
        const response = await fetch(`${apiBase}/deleteTask/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Delete failed');
        }

        setMessage('Task deleted successfully');
        loadAllTasks();
    } catch (error) {
        setMessage('Failed to delete task', 'error');
    }
};

searchBtn.addEventListener('click', searchTasks);
searchInput.addEventListener('keypress', (event) => {
    if (event.key === 'Enter') {
        searchTasks();
    }
});
loadAllBtn.addEventListener('click', loadAllTasks);
cancelEditBtn.addEventListener('click', () => {
    taskForm.reset();
    cancelEditBtn.classList.add('hidden');
    document.getElementById('taskId').value = '';
});
taskForm.addEventListener('submit', saveTask);

loadAllTasks();
