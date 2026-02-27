<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="tr">
<head>
    <meta charset="UTF-8">
    <title>Professional To‑Do App</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-XXXXXXXXXXXXXXXX" crossorigin="anonymous">
    <style>
        /* keep gradient background as subtle layer */
        body {
            margin: 0;
            min-height: 100vh;
            background: linear-gradient(-45deg, #ee7752, #e73c7e, #23a6d5, #23d5ab);
            background-size: 400% 400%;
            animation: gradientBG 12s ease infinite;
            font-family: 'Segoe UI', sans-serif;
        }
        @keyframes gradientBG {
            0% { background-position: 0% 50%; }
            50% { background-position: 100% 50%; }
            100% { background-position: 0% 50%; }
        }
        .decor { display:none; /* hide decorative circles for cleaner look */ }
        .todo-container {
            max-width: 600px;
            margin: 60px auto;
            background: rgba(255,255,255,0.8);
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.1);
        }
        .task-item.completed span { text-decoration: line-through; color: #6c757d; }
    </style>
</head>
<body>
<div class="todo-container">
    <h1 class="mb-4 text-center">My To‑Do List</h1>
    <div class="input-group mb-3">
        <input type="text" id="newTask" class="form-control" placeholder="Add a new task..." aria-label="New task">
        <button class="btn btn-primary" id="addBtn" type="button">Add</button>
    </div>
    <ul class="list-group" id="tasksList"></ul>
</div>
<!-- Bootstrap Icons -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

    <script>
        const apiBase = '<%= request.getContextPath() %>/api/tasks';
        const listEl = document.getElementById('tasksList');
        const newTaskInput = document.getElementById('newTask');
        const addBtn = document.getElementById('addBtn');

        function renderTask(task) {
            const li = document.createElement('li');
            li.className = 'list-group-item d-flex align-items-center task-item';
            if (task.completed) li.classList.add('completed');
            li.dataset.id = task.id;

            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.className = 'form-check-input me-2';
            checkbox.checked = task.completed;
            checkbox.addEventListener('change', () => updateTask(task.id, null, checkbox.checked));

            const span = document.createElement('span');
            span.textContent = task.title;
            span.className = 'flex-grow-1';

            const editBtn = document.createElement('i');
            editBtn.className = 'bi bi-pencil text-primary mx-2';
            editBtn.style.cursor = 'pointer';
            editBtn.title = 'Edit';
            editBtn.addEventListener('click', () => {
                const newTitle = prompt('Enter new task title', task.title);
                if (newTitle && newTitle !== task.title) {
                    updateTask(task.id, newTitle, task.completed);
                }
            });

            const delBtn = document.createElement('i');
            delBtn.className = 'bi bi-trash text-danger';
            delBtn.style.cursor = 'pointer';
            delBtn.title = 'Delete';
            delBtn.addEventListener('click', () => deleteTask(task.id, li));

            li.appendChild(checkbox);
            li.appendChild(span);
            li.appendChild(editBtn);
            li.appendChild(delBtn);
            listEl.appendChild(li);
        }

        function loadTasks() {
            fetch(apiBase)
                .then(r => r.json())
                .then(data => {
                    listEl.innerHTML = '';
                    data.forEach(renderTask);
                });
        }

        function addTask(title) {
            fetch(apiBase, {
                method: 'POST',
                headers: {'Content-Type':'application/json'},
                body: JSON.stringify({title, completed:false})
            }).then(r => r.json()).then(task => {
                renderTask(task);
                newTaskInput.value = '';
            });
        }

        function updateTask(id, title, completed) {
            const body = {};
            if (title !== null) body.title = title;
            if (completed !== null) body.completed = completed;
            fetch(apiBase + '/' + id, {
                method: 'PUT',
                headers: {'Content-Type':'application/json'},
                body: JSON.stringify(body)
            }).then(_=> loadTasks());
        }

        function deleteTask(id, li) {
            fetch(apiBase + '/' + id, {method:'DELETE'})
                .then(_=> li.remove());
        }

        addBtn.addEventListener('click', () => {
            const title = newTaskInput.value.trim();
            if (title) addTask(title);
        });

        loadTasks();
    </script>

</body>
</html>
