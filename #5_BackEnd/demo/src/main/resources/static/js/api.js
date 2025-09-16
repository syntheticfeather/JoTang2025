// API基础URL
const API_BASE_URL = window.location.origin;

// 确保ajaxRequest函数存在
if (typeof ajaxRequest !== 'function') {
    // 封装AJAX请求函数（与main.js保持一致）
    function ajaxRequest(method, url, data = null, successCallback = null, errorCallback = null) {
        const xhr = new XMLHttpRequest();
        xhr.open(method, url, true);
        xhr.setRequestHeader('Content-Type', 'application/json');

        // 添加JWT token（如果存在）
        const token = localStorage.getItem('token');
        if (token) {
            xhr.setRequestHeader('Authorization', `${token}`);
        }

        xhr.onload = function () {
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

        xhr.onerror = function () {
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

// 显示通知函数
function showNotification(message, type = 'success') {
    // 创建通知元素
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.textContent = message;

    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 12px 20px;
        border-radius: 4px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        z-index: 10000;
        transform: translateX(100%);
        transition: transform 0.3s ease;
        font-size: 14px;
        max-width: 300px;
        word-wrap: break-word;
    `;

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

    // 添加到body
    document.body.appendChild(notification);

    // 显示通知
    setTimeout(() => {
        notification.style.transform = 'translateX(0)';
    }, 100);

    // 3秒后隐藏通知
    setTimeout(() => {
        notification.style.transform = 'translateX(100%)';
        // 动画结束后移除元素
        setTimeout(() => {
            if (notification && notification.parentNode) {
                notification.parentNode.removeChild(notification);
            }
        }, 300);
    }, 3000);
}

// 用户相关API
export const userAPI = {
    // 登录
    login: function (username, password, successCallback, errorCallback) {
        ajaxRequest(
            'POST',
            `${API_BASE_URL}/users/login`,
            { username, password },
            successCallback,
            errorCallback
        );
    },

    // 注册
    register: function (username, password, email, phone, successCallback, errorCallback) {
        ajaxRequest(
            'POST',
            `${API_BASE_URL}/users/register`,
            { username, password, email, phone },
            successCallback,
            errorCallback
        );
    },

    // 获取当前用户信息
    getCurrentUser: function (successCallback, errorCallback) {
        ajaxRequest(
            'GET',
            `${API_BASE_URL}/users/me`,
            null,
            successCallback,
            errorCallback
        );
    },

    // 更新用户信息
    updateUser: function (userData, successCallback, errorCallback) {
        ajaxRequest(
            'PUT',
            `${API_BASE_URL}/users/update`,
            userData,
            successCallback,
            errorCallback
        );
    },

    // 重置密码（已登录状态）
    resetPassword: function (oldPassword, newPassword, successCallback, errorCallback) {
        ajaxRequest(
            'PUT',
            `${API_BASE_URL}/users/resetpwd`,
            { oldPassword, newPassword },
            successCallback,
            errorCallback
        );
    },

    // // 发送密码重置验证码
    // sendResetCode: function (phone, successCallback, errorCallback) {
    //     ajaxRequest(
    //         'POST',
    //         `${API_BASE_URL}/users/send-reset-code`,
    //         { phone },
    //         successCallback,
    //         errorCallback
    //     );
    // },

    // // 通过验证码重置密码（忘记密码流程）
    // resetPasswordWithCode: function (phone, verificationCode, newPassword, successCallback, errorCallback) {
    //     ajaxRequest(
    //         'POST',
    //         `${API_BASE_URL}/users/reset-password`,
    //         { phone, verificationCode, newPassword },
    //         successCallback,
    //         errorCallback
    //     );
    // }
};

// 商品相关API
export const productAPI = {
    // 获取商品列表（用于后端管理页面）
    getProducts: function (successCallback, errorCallback) {
        // 直接调用getProductList，但不使用分页参数
        // 这是为了兼容main.js中的调用
        ajaxRequest(
            'GET',
            `${API_BASE_URL}/products/list`,
            null,
            successCallback,
            errorCallback
        );
    },

    // 获取商品列表
    getProductList: function (page = 1, pageSize = 10, filters = {}, successCallback, errorCallback) {
        let queryParams = `page=${page}&pageSize=${pageSize}`;

        // 添加过滤条件
        Object.keys(filters).forEach(key => {
            queryParams += `&${key}=${encodeURIComponent(filters[key])}`;
        });

        ajaxRequest(
            'GET',
            `${API_BASE_URL}/products/list?${queryParams}`,
            null,
            successCallback,
            errorCallback
        );
    },

    // 获取推荐商品
    getRecommendedProducts: function (successCallback, errorCallback) {
        ajaxRequest(
            'GET',
            `${API_BASE_URL}/products/recommended`,
            null,
            successCallback,
            errorCallback
        );
    },

    // 获取商品详情
    getProductDetail: function (productId, successCallback, errorCallback) {
        ajaxRequest(
            'GET',
            `${API_BASE_URL}/products?id=${productId}`,
            null,
            successCallback,
            errorCallback
        );
    },

    // 搜索商品
    searchProducts: function (keyword, successCallback, errorCallback) {
        ajaxRequest(
            'GET',
            `${API_BASE_URL}/products/search?keyword=${encodeURIComponent(keyword)}`,
            null,
            successCallback,
            errorCallback
        );
    },

    // 过滤商品
    filterProducts: function (category, minPrice, maxPrice, successCallback, errorCallback) {
        let queryParams = '';
        if (category) queryParams += `category=${encodeURIComponent(category)}&`;
        if (minPrice) queryParams += `minPrice=${minPrice}&`;
        if (maxPrice) queryParams += `maxPrice=${maxPrice}`;

        ajaxRequest(
            'GET',
            `${API_BASE_URL}/products/filter?${queryParams}`,
            null,
            successCallback,
            errorCallback
        );
    }
};

// 购物车相关API
export const cartAPI = {
    // 获取购物车列表
    getCartItems: function (successCallback, errorCallback) {
        ajaxRequest(
            'GET',
            `${API_BASE_URL}/cart/items`,
            null,
            successCallback,
            errorCallback
        );
    },

    // 添加商品到购物车
    addToCart: function (productId, quantity = 1, successCallback, errorCallback) {
        ajaxRequest(
            'POST',
            `${API_BASE_URL}/cart/add`,
            { productId, quantity },
            successCallback,
            errorCallback
        );
    },

    // 更新购物车商品数量
    updateCartItem: function (cartItemId, quantity, successCallback, errorCallback) {
        ajaxRequest(
            'PUT',
            `${API_BASE_URL}/cart/update`,
            { cartItemId, quantity },
            successCallback,
            errorCallback
        );
    },

    // 删除购物车商品
    deleteCartItem: function (cartItemId, successCallback, errorCallback) {
        ajaxRequest(
            'DELETE',
            `${API_BASE_URL}/cart/delete?cartItemId=${cartItemId}`,
            null,
            successCallback,
            errorCallback
        );
    },

    // 清空购物车
    clearCart: function (successCallback, errorCallback) {
        ajaxRequest(
            'DELETE',
            `${API_BASE_URL}/cart/clear`,
            null,
            successCallback,
            errorCallback
        );
    }
};

// 订单相关API
export const orderAPI = {
    // 创建订单
    createOrder: function (orderData, successCallback, errorCallback) {
        ajaxRequest(
            'POST',
            `${API_BASE_URL}/orders/add`,
            orderData,
            successCallback,
            errorCallback
        );
    },

    // 获取我的订单
    getMyOrders: function (page = 1, pageSize = 10, successCallback, errorCallback) {
        ajaxRequest(
            'GET',
            `${API_BASE_URL}/orders/my?page=${page}&pageSize=${pageSize}`,
            null,
            successCallback,
            errorCallback
        );
    },

    // 获取订单详情
    getOrderDetail: function (orderId, successCallback, errorCallback) {
        ajaxRequest(
            'GET',
            `${API_BASE_URL}/orders/${orderId}`,
            null,
            successCallback,
            errorCallback
        );
    },

    // 取消订单
    cancelOrder: function (orderId, successCallback, errorCallback) {
        ajaxRequest(
            'POST',
            `${API_BASE_URL}/orders/${orderId}/cancel`,
            null,
            successCallback,
            errorCallback
        );
    },

    // 确认收货
    confirmOrder: function (orderId, successCallback, errorCallback) {
        ajaxRequest(
            'POST',
            `${API_BASE_URL}/orders/${orderId}/confirm`,
            null,
            successCallback,
            errorCallback
        );
    }
};

// 收藏相关API
export const favoriteAPI = {
    // 获取我的收藏
    getFavorites: function (page = 1, pageSize = 10, successCallback, errorCallback) {
        ajaxRequest(
            'GET',
            `${API_BASE_URL}/favorites?page=${page}&pageSize=${pageSize}`,
            null,
            successCallback,
            errorCallback
        );
    },

    // 添加收藏
    addFavorite: function (productId, successCallback, errorCallback) {
        ajaxRequest(
            'POST',
            `${API_BASE_URL}/favorites/add`,
            { productId },
            successCallback,
            errorCallback
        );
    },

    // 取消收藏
    removeFavorite: function (productId, successCallback, errorCallback) {
        ajaxRequest(
            'DELETE',
            `${API_BASE_URL}/favorites/remove?productId=${productId}`,
            null,
            successCallback,
            errorCallback
        );
    }
};