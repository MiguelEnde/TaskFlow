// ===== MODAL HELPERS =====
function openModal(id) {
  const el = document.getElementById(id);
  if (el) el.classList.add('active');
}

function closeModal(id) {
  const el = document.getElementById(id);
  if (el) el.classList.remove('active');
}

// Close modal clicking backdrop
document.addEventListener('click', function(e) {
  if (e.target.classList.contains('modal-backdrop')) {
    e.target.classList.remove('active');
  }
});

// Close modal with Escape
document.addEventListener('keydown', function(e) {
  if (e.key === 'Escape') {
    document.querySelectorAll('.modal-backdrop.active').forEach(m => m.classList.remove('active'));
    document.querySelectorAll('.task-dropdown-menu.open').forEach(m => m.classList.remove('open'));
  }
});

// ===== DROPDOWN MENUS =====
document.addEventListener('click', function(e) {
  // Toggle dropdown
  const btn = e.target.closest('.task-menu-btn');
  if (btn) {
    e.stopPropagation();
    const menu = btn.nextElementSibling;
    const isOpen = menu && menu.classList.contains('open');
    // Close all
    document.querySelectorAll('.task-dropdown-menu.open').forEach(m => m.classList.remove('open'));
    if (!isOpen && menu) menu.classList.add('open');
    return;
  }
  // Close on outside click
  if (!e.target.closest('.task-dropdown')) {
    document.querySelectorAll('.task-dropdown-menu.open').forEach(m => m.classList.remove('open'));
  }
});

// ===== EDIT TASK MODAL - populate fields =====
function openEditTask(taskId, projectId, title, description, status, priority) {
  document.getElementById('editTaskId').value = taskId;
  document.getElementById('editProjectId').value = projectId;
  document.getElementById('editTaskTitle').value = title;
  document.getElementById('editTaskDescription').value = description;
  document.getElementById('editTaskStatus').value = status;
  document.getElementById('editTaskPriority').value = priority;
  document.getElementById('editTaskForm').action = '/projects/' + projectId + '/tasks/' + taskId + '/edit';
  openModal('editTaskModal');
}

// ===== DELETE CONFIRM =====
function confirmDelete(formId) {
  if (confirm('¿Estás seguro de que quieres eliminar esto? Esta acción no se puede deshacer.')) {
    document.getElementById(formId).submit();
  }
}

// ===== ADD TASK - set default status column =====
function openAddTask(status) {
  const sel = document.getElementById('newTaskStatus');
  if (sel) sel.value = status;
  openModal('addTaskModal');
}

// ===== AUTO-DISMISS ALERTS =====
document.addEventListener('DOMContentLoaded', function() {
  const alerts = document.querySelectorAll('.alert');
  alerts.forEach(alert => {
    setTimeout(() => {
      alert.style.opacity = '0';
      alert.style.transition = 'opacity 0.5s';
      setTimeout(() => alert.remove(), 500);
    }, 4000);
  });
});
