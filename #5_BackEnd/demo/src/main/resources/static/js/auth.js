// 导入API模块
import { userAPI } from './api.js';

// 等待DOM加载完成
document.addEventListener('DOMContentLoaded', function() {
    // 检查当前页面类型
    const currentPage = window.location.pathname;
    
    // 如果是登录页面
    if (currentPage.includes('login.html')) {
        initLoginPage();
    }
    // 如果是注册页面
    else if (currentPage.includes('register.html')) {
        initRegisterPage();
    }
    // 如果是忘记密码页面
    else if (currentPage.includes('forgot-password.html')) {
        initForgotPasswordPage();
    }
});

// 初始化登录页面
function initLoginPage() {
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            // 获取表单数据
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;
            const rememberMe = document.getElementById('rememberMe').checked;
            
            // 表单验证
            if (!username || !password) {
                showNotification('请填写完整的登录信息', 'error');
                return;
            }
            
            // 显示加载状态
            showLoading(true);
            
            // 调用后端登录API
            userAPI.login(username, password, 
                // 成功回调
                function(response) {
                    // 存储token
                    if (response.token) {
                        localStorage.setItem('token', response.token);
                    }
                    
                    // 如果勾选了记住我，可以使用localStorage存储用户信息
                    if (rememberMe) {
                        localStorage.setItem('rememberedUsername', username);
                    } else {
                        localStorage.removeItem('rememberedUsername');
                    }
                    
                    // 登录成功，跳转到首页
                    showNotification('登录成功，正在跳转...', 'success');
                    setTimeout(() => {
                        // 这里应该根据用户角色跳转到不同页面
                        // 普通用户跳转到用户首页
                        window.location.href = 'index.html';
                    }, 1500);
                }, 
                // 失败回调
                function(status, statusText) {
                    let errorMessage = '登录失败，请稍后重试';
                    if (status === 401) {
                        errorMessage = '用户名或密码错误，请重新输入';
                    } else if (status === 0) {
                        errorMessage = '网络连接失败，请检查网络';
                    }
                    showNotification(errorMessage, 'error');
                    showLoading(false);
                }
            );
        });
        
        // 自动填充记住的用户名
        const rememberedUsername = localStorage.getItem('rememberedUsername');
        if (rememberedUsername) {
            document.getElementById('username').value = rememberedUsername;
            document.getElementById('rememberMe').checked = true;
        }
    }
}

// 初始化注册页面
function initRegisterPage() {
    const registerForm = document.getElementById('registerForm');
    const sendCodeBtn = document.getElementById('sendCodeBtn');
    
    if (registerForm) {
        // 表单提交事件
        registerForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            // 获取表单数据
            const username = document.getElementById('username').value;
            const email = document.getElementById('email').value;
            const phone = document.getElementById('phone').value;
            const verificationCode = document.getElementById('verificationCode').value;
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            const agreeTerms = document.getElementById('agreeTerms').checked;
            
            // 表单验证
            let isValid = true;
            
            // 验证用户名
            if (!username || username.length < 3) {
                showError('username', '用户名至少需要3个字符');
                isValid = false;
            } else {
                hideError('username');
            }
            
            // 验证邮箱
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!email || !emailRegex.test(email)) {
                showError('email', '请输入有效的邮箱地址');
                isValid = false;
            } else {
                hideError('email');
            }
            
            // 验证手机号
            const phoneRegex = /^1[3-9]\d{9}$/;
            if (!phone || !phoneRegex.test(phone)) {
                showError('phone', '请输入有效的手机号码');
                isValid = false;
            } else {
                hideError('phone');
            }
            
            // 验证验证码
            if (!verificationCode || verificationCode.length !== 6 || !/^\d+$/.test(verificationCode)) {
                showError('verificationCode', '请输入6位数字验证码');
                isValid = false;
            } else {
                hideError('verificationCode');
            }
            
            // 验证密码
            if (!password || password.length < 6) {
                showError('password', '密码至少需要6个字符');
                isValid = false;
            } else {
                hideError('password');
            }
            
            // 验证确认密码
            if (password !== confirmPassword) {
                showError('confirmPassword', '两次输入的密码不一致');
                isValid = false;
            } else {
                hideError('confirmPassword');
            }
            
            // 验证是否同意用户协议
            if (!agreeTerms) {
                showNotification('请阅读并同意用户协议和隐私政策', 'error');
                isValid = false;
            }
            
            if (!isValid) {
                return;
            }
            
            // 显示加载状态
            showLoading(true);
            
            // 调用后端注册API
            userAPI.register(username, password, email, 
                // 成功回调
                function(response) {
                    showNotification('注册成功，请登录', 'success');
                    setTimeout(() => {
                        window.location.href = 'login.html';
                    }, 1500);
                    showLoading(false);
                }, 
                // 失败回调
                function(status, statusText) {
                    let errorMessage = '注册失败，请稍后重试';
                    if (status === 409) {
                        errorMessage = '用户名或邮箱已被注册';
                    } else if (status === 400) {
                        errorMessage = '输入信息格式不正确';
                    } else if (status === 0) {
                        errorMessage = '网络连接失败，请检查网络';
                    }
                    showNotification(errorMessage, 'error');
                    showLoading(false);
                }
            );
        });
    }
    
    if (sendCodeBtn) {
        // 发送验证码按钮点击事件
        sendCodeBtn.addEventListener('click', function() {
            const phone = document.getElementById('phone').value;
            const phoneRegex = /^1[3-9]\d{9}$/;
            
            if (!phone || !phoneRegex.test(phone)) {
                showError('phone', '请输入有效的手机号码');
                return;
            }
            
            // 禁用按钮并开始倒计时
            let countdown = 60;
            sendCodeBtn.disabled = true;
            sendCodeBtn.textContent = `${countdown}秒后重新发送`;
            
            const timer = setInterval(() => {
                countdown--;
                sendCodeBtn.textContent = `${countdown}秒后重新发送`;
                
                if (countdown <= 0) {
                    clearInterval(timer);
                    sendCodeBtn.disabled = false;
                    sendCodeBtn.textContent = '发送验证码';
                }
            }, 1000);
            
            // 发送验证码到后端API
            ajaxRequest(
                'POST',
                '/api/users/send-code',
                { phone: phone },
                function() {
                    showNotification('验证码已发送', 'success');
                },
                function() {
                    showNotification('验证码发送失败，请稍后重试', 'error');
                    // 重置按钮状态
                    clearInterval(timer);
                    sendCodeBtn.disabled = false;
                    sendCodeBtn.textContent = '发送验证码';
                }
            );
        });
    }
}

// 初始化忘记密码页面
function initForgotPasswordPage() {
    const resetForm = document.getElementById('resetForm');
    const sendResetCodeBtn = document.getElementById('sendResetCodeBtn');
    
    if (resetForm) {
        resetForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            // 表单处理逻辑
            const phone = document.getElementById('phone').value;
            const verificationCode = document.getElementById('verificationCode').value;
            const newPassword = document.getElementById('newPassword').value;
            const confirmNewPassword = document.getElementById('confirmNewPassword').value;
            
            // 重置错误提示
            hideError('phone');
            hideError('verificationCode');
            hideError('newPassword');
            hideError('confirmNewPassword');
            
            // 完整验证
            let isValid = true;
            
            // 验证手机号
            const phoneRegex = /^1[3-9]\d{9}$/;
            if (!phone || !phoneRegex.test(phone)) {
                showError('phone', '请输入有效的手机号码');
                isValid = false;
            }
            
            // 验证验证码
            if (!verificationCode || verificationCode.length !== 6 || !/^\d+$/.test(verificationCode)) {
                showError('verificationCode', '请输入6位数字验证码');
                isValid = false;
            }
            
            // 验证新密码
            const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,20}$/;
            if (!newPassword || !passwordRegex.test(newPassword)) {
                showError('newPassword', '密码长度8-20位，必须包含字母和数字');
                isValid = false;
            }
            
            // 验证确认密码
            if (!confirmNewPassword || newPassword !== confirmNewPassword) {
                showError('confirmNewPassword', '两次输入的密码不一致');
                isValid = false;
            }
            
            if (!isValid) {
                return;
            }
            
            // 显示加载状态
            showLoading(true);
            
            // 发送密码重置请求到后端API
            userAPI.resetPasswordWithCode(
                phone,
                verificationCode,
                newPassword,
                function(response) {
                    // 成功处理
                    showNotification('密码重置成功，请登录', 'success');
                    setTimeout(() => {
                        window.location.href = 'login.html';
                    }, 1500);
                    showLoading(false);
                },
                function(status, statusText) {
                    // 错误处理
                    console.error('密码重置失败:', status, statusText);
                    showNotification('密码重置失败，请稍后重试', 'error');
                    showLoading(false);
                }
            );
        });
    }
    
    if (sendResetCodeBtn) {
        sendResetCodeBtn.addEventListener('click', function() {
            const phone = document.getElementById('phone').value;
            const phoneRegex = /^1[3-9]\d{9}$/;
            
            // 重置手机错误提示
            hideError('phone');
            
            if (!phone || !phoneRegex.test(phone)) {
                showError('phone', '请输入有效的手机号码');
                return;
            }
            
            // 禁用按钮并开始倒计时
            let countdown = 60;
            sendResetCodeBtn.disabled = true;
            sendResetCodeBtn.textContent = `${countdown}秒后重新发送`;
            
            const timer = setInterval(() => {
                countdown--;
                sendResetCodeBtn.textContent = `${countdown}秒后重新发送`;
                
                if (countdown <= 0) {
                    clearInterval(timer);
                    sendResetCodeBtn.disabled = false;
                    sendResetCodeBtn.textContent = '发送验证码';
                }
            }, 1000);
            
            // 发送验证码到后端API
            userAPI.sendResetCode(
                phone,
                function() {
                    showNotification('验证码已发送', 'success');
                },
                function() {
                    showNotification('验证码发送失败，请稍后重试', 'error');
                    // 重置按钮状态
                    clearInterval(timer);
                    sendResetCodeBtn.disabled = false;
                    sendResetCodeBtn.textContent = '发送验证码';
                }
            );
        });
    }
}

// 显示错误信息
function showError(fieldId, message) {
    const field = document.getElementById(fieldId);
    const errorElement = document.getElementById(`${fieldId}Error`);
    
    if (field && errorElement) {
        field.classList.add('error');
        errorElement.textContent = message;
        errorElement.style.display = 'block';
    }
}

// 隐藏错误信息
function hideError(fieldId) {
    const field = document.getElementById(fieldId);
    const errorElement = document.getElementById(`${fieldId}Error`);
    
    if (field && errorElement) {
        field.classList.remove('error');
        errorElement.style.display = 'none';
    }
}

// 显示通知
function showNotification(message, type = 'info') {
    // 检查是否已存在通知元素
    let notification = document.querySelector('.notification');
    if (!notification) {
        // 创建通知元素
        notification = document.createElement('div');
        notification.className = 'notification';
        document.body.appendChild(notification);
    }
    
    // 设置通知内容和类型
    notification.textContent = message;
    notification.className = `notification notification-${type}`;
    
    // 添加动画
    notification.style.opacity = '0';
    notification.style.transform = 'translateY(-20px)';
    notification.style.transition = 'opacity 0.3s ease, transform 0.3s ease';
    notification.style.position = 'fixed';
    notification.style.top = '20px';
    notification.style.right = '20px';
    notification.style.padding = '12px 20px';
    notification.style.borderRadius = '4px';
    notification.style.boxShadow = '0 2px 8px rgba(0, 0, 0, 0.15)';
    notification.style.zIndex = '10000';
    notification.style.fontSize = '14px';
    notification.style.fontWeight = '500';
    notification.style.minWidth = '200px';
    notification.style.textAlign = 'center';
    
    // 设置不同类型的背景颜色
    if (type === 'success') {
        notification.style.backgroundColor = '#52c41a';
        notification.style.color = '#fff';
    } else if (type === 'error') {
        notification.style.backgroundColor = '#ff4d4f';
        notification.style.color = '#fff';
    } else if (type === 'warning') {
        notification.style.backgroundColor = '#faad14';
        notification.style.color = '#fff';
    } else {
        notification.style.backgroundColor = '#1890ff';
        notification.style.color = '#fff';
    }
    
    // 显示通知
    setTimeout(() => {
        notification.style.opacity = '1';
        notification.style.transform = 'translateY(0)';
    }, 10);
    
    // 设置自动关闭
    setTimeout(() => {
        notification.style.opacity = '0';
        notification.style.transform = 'translateY(-20px)';
        
        setTimeout(() => {
            if (notification && notification.parentNode) {
                notification.parentNode.removeChild(notification);
            }
        }, 300);
    }, 3000);
}

// 显示加载状态
function showLoading(show) {
    // 检查是否已存在加载元素
    let loadingOverlay = document.querySelector('.loading-overlay');
    
    if (show) {
        if (!loadingOverlay) {
            // 创建加载遮罩
            loadingOverlay = document.createElement('div');
            loadingOverlay.className = 'loading-overlay';
            loadingOverlay.style.position = 'fixed';
            loadingOverlay.style.top = '0';
            loadingOverlay.style.left = '0';
            loadingOverlay.style.right = '0';
            loadingOverlay.style.bottom = '0';
            loadingOverlay.style.backgroundColor = 'rgba(255, 255, 255, 0.7)';
            loadingOverlay.style.display = 'flex';
            loadingOverlay.style.alignItems = 'center';
            loadingOverlay.style.justifyContent = 'center';
            loadingOverlay.style.zIndex = '9999';
            
            // 创建加载动画
            const spinner = document.createElement('div');
            spinner.style.width = '40px';
            spinner.style.height = '40px';
            spinner.style.border = '3px solid #f3f3f3';
            spinner.style.borderTop = '3px solid #1890ff';
            spinner.style.borderRadius = '50%';
            spinner.style.animation = 'spin 1s linear infinite';
            
            // 添加旋转动画
            const style = document.createElement('style');
            style.textContent = '@keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }';
            document.head.appendChild(style);
            
            loadingOverlay.appendChild(spinner);
            document.body.appendChild(loadingOverlay);
        }
        loadingOverlay.style.display = 'flex';
    } else {
        if (loadingOverlay) {
            loadingOverlay.style.display = 'none';
        }
    }
}

// 使用main.js中定义的AJAX请求函数，如果不存在则使用此备用实现
if (typeof ajaxRequest !== 'function') {
    // 封装AJAX请求函数（与main.js保持一致）
    function ajaxRequest(method, url, data = null, successCallback = null, errorCallback = null) {
        const xhr = new XMLHttpRequest();
        xhr.open(method, url, true);
        xhr.setRequestHeader('Content-Type', 'application/json');
        
        // 添加JWT token（如果存在）
        const token = localStorage.getItem('token');
        if (token) {
            xhr.setRequestHeader('Authorization', `Bearer ${token}`);
        }
        
        xhr.onload = function() {
            if (xhr.status >= 200 && xhr.status < 300) {
                const response = JSON.parse(xhr.responseText);
                if (successCallback) {
                    successCallback(response);
                }
            } else {
                if (errorCallback) {
                    errorCallback(xhr.status, xhr.statusText);
                } else {
                    console.error('请求失败:', xhr.status, xhr.statusText);
                    showNotification('请求失败，请稍后重试', 'error');
                }
            }
        };
        
        xhr.onerror = function() {
            if (errorCallback) {
                errorCallback(0, '网络错误');
            } else {
                console.error('网络错误');
                showNotification('网络错误，请检查网络连接', 'error');
            }
        };
        
        if (data) {
            xhr.send(JSON.stringify(data));
        } else {
            xhr.send();
        }
    }
}