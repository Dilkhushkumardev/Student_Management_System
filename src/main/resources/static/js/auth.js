/**
 * SmartAttend - Authentication Manager
 */

document.addEventListener('DOMContentLoaded', () => {
    SmartAttendAPI.initTheme();

    const loginForm = document.getElementById('login-form');
    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }

    // Demo Account Fillers
    window.fillCredentials = function(role) {
        const usernameInput = document.getElementById('username');
        const passwordInput = document.getElementById('password');
        if (!usernameInput || !passwordInput) return;

        if (role === 'admin') {
            usernameInput.value = 'admin';
            passwordInput.value = 'Admin@123';
        } else if (role === 'faculty') {
            usernameInput.value = 'faculty';
            passwordInput.value = 'Faculty@123';
        } else if (role === 'student') {
            usernameInput.value = 'student';
            passwordInput.value = 'Student@123';
        }
    };
});

async function handleLogin(e) {
    e.preventDefault();
    const usernameInput = document.getElementById('username');
    const passwordInput = document.getElementById('password');
    const submitBtn = document.getElementById('login-btn');
    const errorAlert = document.getElementById('login-error');

    if (errorAlert) errorAlert.classList.add('d-none');

    const username = usernameInput.value.trim();
    const password = passwordInput.value.trim();

    if (!username || !password) {
        if (errorAlert) {
            errorAlert.textContent = 'Please enter both username and password';
            errorAlert.classList.remove('d-none');
        }
        return;
    }

    try {
        if (submitBtn) {
            submitBtn.disabled = true;
            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i> Authenticating...';
        }

        const response = await SmartAttendAPI.post('/auth/login', { username, password });

        if (response.success && response.data) {
            const authData = response.data;
            const token = authData.token || authData.accessToken;
            SmartAttendAPI.setSession(token, authData);

            SmartAttendAPI.showToast('Login successful! Redirecting...', 'success');

            // Redirect based on primary role
            setTimeout(() => {
                redirectByRole(authData.roles);
            }, 300);
        } else {
            throw new Error(response.message || 'Login failed');
        }
    } catch (err) {
        if (errorAlert) {
            errorAlert.textContent = err.message || 'Invalid username or password';
            errorAlert.classList.remove('d-none');
        }
        SmartAttendAPI.showToast(err.message || 'Authentication error', 'danger');
    } finally {
        if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.innerHTML = 'Sign In to Portal <i class="fas fa-arrow-right ms-2"></i>';
        }
    }
}

function redirectByRole(roles) {
    if (!roles || roles.length === 0) {
        window.location.href = '/index.html';
        return;
    }

    if (roles.includes('ROLE_SUPER_ADMIN') || roles.includes('ROLE_ADMIN')) {
        window.location.href = '/admin/index.html';
    } else if (roles.includes('ROLE_FACULTY')) {
        window.location.href = '/faculty/index.html';
    } else if (roles.includes('ROLE_STUDENT')) {
        window.location.href = '/student/index.html';
    } else {
        window.location.href = '/index.html';
    }
}

function logout() {
    SmartAttendAPI.clearSession();
    window.location.href = '/login.html?logged_out=true';
}

window.logout = logout;
window.redirectByRole = redirectByRole;
