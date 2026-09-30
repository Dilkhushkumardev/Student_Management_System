/**
 * SmartAttend - Centralized API & Auth Client
 */

const API_BASE = '/api';

const SmartAttendAPI = {
    // Auth State Storage
    getToken() {
        const token = localStorage.getItem('smartattend_token');
        if (!token || token === 'undefined' || token === 'null') {
            return null;
        }
        return token;
    },

    setSession(token, user) {
        if (!token && user) {
            token = user.token || user.accessToken;
        }
        if (token && token !== 'undefined') {
            localStorage.setItem('smartattend_token', token);
        }
        if (user) {
            localStorage.setItem('smartattend_user', JSON.stringify(user));
        }
    },

    getUser() {
        const user = localStorage.getItem('smartattend_user');
        if (!user || user === 'undefined' || user === 'null') {
            return null;
        }
        try {
            return JSON.parse(user);
        } catch (e) {
            return null;
        }
    },

    clearSession() {
        localStorage.removeItem('smartattend_token');
        localStorage.removeItem('smartattend_user');
    },

    isAuthenticated() {
        return !!this.getToken();
    },

    // HTTP Request Handler
    async request(endpoint, options = {}) {
        const token = this.getToken();
        const headers = {
            'Content-Type': 'application/json',
            ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
            ...options.headers
        };

        const config = {
            ...options,
            headers
        };

        try {
            const response = await fetch(`${API_BASE}${endpoint}`, config);

            // Handle 401 Unauthorized
            if (response.status === 401) {
                if (!endpoint.includes('/auth/login')) {
                    this.clearSession();
                    window.location.href = '/login.html?expired=true';
                }
            }

            // Handle file download responses (e.g. CSV export)
            const contentType = response.headers.get('content-type');
            if (contentType && contentType.includes('text/csv')) {
                return response.blob();
            }

            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.message || `Request failed with status ${response.status}`);
            }

            return data;
        } catch (error) {
            console.error(`API Error [${endpoint}]:`, error);
            throw error;
        }
    },

    // REST Method Wrappers
    get(endpoint) {
        return this.request(endpoint, { method: 'GET' });
    },

    post(endpoint, body) {
        return this.request(endpoint, {
            method: 'POST',
            body: JSON.stringify(body)
        });
    },

    put(endpoint, body) {
        return this.request(endpoint, {
            method: 'PUT',
            body: JSON.stringify(body)
        });
    },

    delete(endpoint) {
        return this.request(endpoint, { method: 'DELETE' });
    },

    // Toast Notification System
    showToast(message, type = 'info') {
        let container = document.getElementById('toast-container');
        if (!container) {
            container = document.createElement('div');
            container.id = 'toast-container';
            container.className = 'toast-container position-fixed bottom-0 end-0 p-3';
            container.style.zIndex = '9999';
            document.body.appendChild(container);
        }

        const toastId = 'toast-' + Date.now();
        const iconMap = {
            success: 'fa-check-circle text-success',
            danger: 'fa-exclamation-circle text-danger',
            warning: 'fa-triangle-exclamation text-warning',
            info: 'fa-info-circle text-primary'
        };

        const toastEl = document.createElement('div');
        toastEl.id = toastId;
        toastEl.className = 'toast align-items-center shadow-lg border-0';
        toastEl.setAttribute('role', 'alert');
        toastEl.setAttribute('aria-live', 'assertive');
        toastEl.setAttribute('aria-atomic', 'true');
        toastEl.innerHTML = `
            <div class="d-flex p-2">
                <div class="toast-body d-flex align-items-center gap-2">
                    <i class="fas ${iconMap[type] || iconMap.info} fa-lg"></i>
                    <div>${message}</div>
                </div>
                <button type="button" class="btn-close me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        `;

        container.appendChild(toastEl);
        const toast = new bootstrap.Toast(toastEl, { delay: 4000 });
        toast.show();

        toastEl.addEventListener('hidden.bs.toast', () => {
            toastEl.remove();
        });
    },

    // Global Theme Toggle
    initTheme() {
        const savedTheme = localStorage.getItem('smartattend_theme') || 'light';
        document.documentElement.setAttribute('data-theme', savedTheme);
        const toggleBtn = document.getElementById('theme-toggle-btn');
        if (toggleBtn) {
            toggleBtn.innerHTML = savedTheme === 'dark' ? '<i class="fas fa-sun"></i>' : '<i class="fas fa-moon"></i>';
            toggleBtn.addEventListener('click', () => {
                const current = document.documentElement.getAttribute('data-theme');
                const next = current === 'dark' ? 'light' : 'dark';
                document.documentElement.setAttribute('data-theme', next);
                localStorage.setItem('smartattend_theme', next);
                toggleBtn.innerHTML = next === 'dark' ? '<i class="fas fa-sun"></i>' : '<i class="fas fa-moon"></i>';
            });
        }
    }
};

window.SmartAttendAPI = SmartAttendAPI;
