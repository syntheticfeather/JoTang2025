// 导入API模块
import { productAPI, orderAPI, userAPI } from './api.js';

// 等待DOM加载完成
 document.addEventListener('DOMContentLoaded', function() {
    // 导航切换功能
    const navLinks = document.querySelectorAll('.nav-link');
    const sidebarLinks = document.querySelectorAll('.sidebar-link');
    const contentSections = document.querySelectorAll('.content-section');

    // 切换内容区域显示
    function switchSection(id) {
        // 隐藏所有内容区域
        contentSections.forEach(section => {
            section.classList.remove('active');
        });

        // 显示对应内容区域
        const activeSection = document.getElementById(id);
        if (activeSection) {
            activeSection.classList.add('active');
        }

        // 更新导航链接状态
        navLinks.forEach(link => {
            link.classList.remove('active');
            if (link.getAttribute('href') === `#${id}`) {
                link.classList.add('active');
            }
        });

        // 更新侧边栏链接状态
        sidebarLinks.forEach(link => {
            link.classList.remove('active');
            if (link.getAttribute('href') === `#${id}`) {
                link.classList.add('active');
            }
        });
    }

    // 为导航链接添加点击事件
    navLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const id = this.getAttribute('href').substring(1);
            switchSection(id);
        });
    });

    // 为侧边栏链接添加点击事件
    sidebarLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const id = this.getAttribute('href').substring(1);
            switchSection(id);
        });
    });

    // 初始化图表
    initCharts();

    // 初始化下拉菜单
    initDropdowns();

    // 初始化过滤按钮
    initFilterButtons();

    // 添加滚动事件监听
    window.addEventListener('scroll', handleScroll);

    // 加载产品数据
    loadProductsData();
});

// 初始化图表
function initCharts() {
    // 销售趋势图表
    const salesCtx = document.getElementById('salesChart');
    if (salesCtx) {
        new Chart(salesCtx, {
            type: 'line',
            data: {
                labels: ['11/4', '11/5', '11/6', '11/7', '11/8', '11/9', '11/10'],
                datasets: [{
                    label: '销售额',
                    data: [3500, 4200, 3800, 5100, 4800, 6200, 5900],
                    borderColor: '#1890ff',
                    backgroundColor: 'rgba(24, 144, 255, 0.1)',
                    borderWidth: 2,
                    tension: 0.3,
                    fill: true,
                    pointBackgroundColor: '#fff',
                    pointBorderColor: '#1890ff',
                    pointBorderWidth: 2,
                    pointRadius: 4,
                    pointHoverRadius: 6
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: false
                    },
                    tooltip: {
                        backgroundColor: 'rgba(0, 0, 0, 0.8)',
                        padding: 10,
                        cornerRadius: 4,
                        titleFont: {
                            size: 14,
                            weight: 'bold'
                        },
                        bodyFont: {
                            size: 13
                        },
                        callbacks: {
                            label: function(context) {
                                return `销售额: ¥${context.raw.toLocaleString()}`;
                            }
                        }
                    }
                },
                scales: {
                    x: {
                        grid: {
                            display: false
                        },
                        ticks: {
                            color: '#999'
                        }
                    },
                    y: {
                        beginAtZero: true,
                        grid: {
                            color: '#f0f0f0'
                        },
                        ticks: {
                            color: '#999',
                            callback: function(value) {
                                return '¥' + value.toLocaleString();
                            }
                        }
                    }
                },
                interaction: {
                    intersect: false,
                    mode: 'index'
                },
                animations: {
                    tension: {
                        duration: 1000,
                        easing: 'linear'
                    }
                }
            }
        });
    }

    // 产品分类占比图表
    const categoryCtx = document.getElementById('categoryChart');
    if (categoryCtx) {
        new Chart(categoryCtx, {
            type: 'doughnut',
            data: {
                labels: ['电子产品', '配件', '可穿戴设备', '智能家居', '其他'],
                datasets: [{
                    data: [45, 25, 15, 10, 5],
                    backgroundColor: [
                        '#1890ff',
                        '#52c41a',
                        '#faad14',
                        '#722ed1',
                        '#fa8c16'
                    ],
                    borderWidth: 0,
                    hoverOffset: 10
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'right',
                        labels: {
                            padding: 20,
                            font: {
                                size: 13
                            }
                        }
                    },
                    tooltip: {
                        backgroundColor: 'rgba(0, 0, 0, 0.8)',
                        padding: 10,
                        cornerRadius: 4,
                        callbacks: {
                            label: function(context) {
                                return `${context.label}: ${context.raw}%`;
                            }
                        }
                    }
                },
                cutout: '65%',
                animation: {
                    animateScale: true,
                    animateRotate: true
                }
            }
        });
    }
}

// 初始化下拉菜单
function initDropdowns() {
    const dropdownToggles = document.querySelectorAll('.dropdown-toggle');
    
    dropdownToggles.forEach(toggle => {
        toggle.addEventListener('click', function(e) {
            e.preventDefault();
            const dropdownMenu = this.parentElement.querySelector('.dropdown-menu');
            if (dropdownMenu) {
                // 切换下拉菜单显示状态
                dropdownMenu.style.display = dropdownMenu.style.display === 'block' ? 'none' : 'block';
            }
        });
    });

    // 点击其他区域关闭下拉菜单
    document.addEventListener('click', function(e) {
        const dropdowns = document.querySelectorAll('.dropdown-menu');
        dropdowns.forEach(dropdown => {
            if (!dropdown.parentElement.contains(e.target)) {
                dropdown.style.display = 'none';
            }
        });
    });
}

// 初始化过滤按钮
function initFilterButtons() {
    const filterButtons = document.querySelectorAll('.filter-btn');
    
    filterButtons.forEach(button => {
        button.addEventListener('click', function() {
            // 移除所有按钮的active状态
            this.parentElement.querySelectorAll('.filter-btn').forEach(btn => {
                btn.classList.remove('active');
            });
            // 添加当前按钮的active状态
            this.classList.add('active');
            
            // 这里可以添加根据过滤条件更新图表数据的逻辑
            updateChartData(this.textContent);
        });
    });
}

// 更新图表数据（根据过滤条件）
function updateChartData(filterType) {
    const salesChart = Chart.getChart('salesChart');
    if (salesChart) {
        let labels, data;
        
        switch(filterType) {
            case '日':
                labels = ['11/4', '11/5', '11/6', '11/7', '11/8', '11/9', '11/10'];
                data = [3500, 4200, 3800, 5100, 4800, 6200, 5900];
                break;
            case '周':
                labels = ['第1周', '第2周', '第3周', '第4周'];
                data = [18500, 22300, 20100, 28500];
                break;
            case '月':
                labels = ['1月', '2月', '3月', '4月', '5月', '6月'];
                data = [85000, 92000, 105000, 118000, 122000, 135000];
                break;
            case '年':
                labels = ['2019', '2020', '2021', '2022', '2023', '2024'];
                data = [1200000, 1500000, 1850000, 2200000, 2650000, 3200000];
                break;
            default:
                labels = ['11/4', '11/5', '11/6', '11/7', '11/8', '11/9', '11/10'];
                data = [3500, 4200, 3800, 5100, 4800, 6200, 5900];
        }
        
        // 更新图表数据
        salesChart.data.labels = labels;
        salesChart.data.datasets[0].data = data;
        salesChart.update();
    }
}

// 处理滚动事件
function handleScroll() {
    const navbar = document.querySelector('.navbar');
    if (navbar) {
        if (window.scrollY > 50) {
            navbar.classList.add('navbar-scrolled');
        } else {
            navbar.classList.remove('navbar-scrolled');
        }
    }
}

// 加载产品数据
function loadProductsData() {
    // 获取产品管理页面的表格
    const productsTable = document.querySelector('.products-table tbody');
    const loadingIndicator = document.querySelector('.loading-indicator');
    
    if (productsTable) {
        // 显示加载状态
        if (loadingIndicator) {
            loadingIndicator.style.display = 'block';
        }
        
        // 调用API获取产品数据
        productAPI.getProducts(
            // 成功回调
            function(products) {
                // 清空表格内容
                productsTable.innerHTML = '';
                
                // 如果没有产品数据
                if (!products || products.length === 0) {
                    const emptyRow = document.createElement('tr');
                    emptyRow.innerHTML = `<td colspan="11" style="text-align: center; padding: 20px;">暂无产品数据</td>`;
                    productsTable.appendChild(emptyRow);
                } else {
                    // 添加产品数据行
                    products.forEach(product => {
                        const row = document.createElement('tr');
                        row.innerHTML = `
                            <td><input type="checkbox"></td>
                            <td>${product.id}</td>
                            <td><img src="${product.imageUrl || `https://picsum.photos/seed/${product.id}/40/40`}" alt="${product.name}" style="width: 40px; height: 40px; object-fit: cover;"></td>
                            <td>${product.name}</td>
                            <td>${product.category}</td>
                            <td>¥${product.price}</td>
                            <td>${product.stock}</td>
                            <td>${product.sales || 0}</td>
                            <td><span class="status ${product.status === 'active' ? 'status-active' : 'status-inactive'}">${product.status === 'active' ? '在售' : '下架'}</span></td>
                            <td>${product.createTime || product.createdAt}</td>
                            <td>
                                <button class="btn btn-sm btn-primary">查看</button>
                                <button class="btn btn-sm btn-secondary">编辑</button>
                                <button class="btn btn-sm btn-danger">删除</button>
                            </td>
                        `;
                        productsTable.appendChild(row);
                    });
                }
                
                // 隐藏加载状态
                if (loadingIndicator) {
                    loadingIndicator.style.display = 'none';
                }
            }, 
            // 失败回调
            function(status, statusText) {
                // 清空表格内容
                productsTable.innerHTML = `<td colspan="11" style="text-align: center; padding: 20px; color: #ff4d4f;">加载产品数据失败，请稍后重试</td>`;
                
                // 隐藏加载状态
                if (loadingIndicator) {
                    loadingIndicator.style.display = 'none';
                }
                
                // 显示错误通知
                showNotification('加载产品数据失败', 'error');
                console.error('加载产品数据失败:', status, statusText);
            }
        );
    }
}

// 封装AJAX请求函数
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

// 显示通知函数
function showNotification(message, type = 'info') {
    // 创建通知元素
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.textContent = message;
    
    // 添加到页面
    document.body.appendChild(notification);
    
    // 添加动画
    setTimeout(() => {
        notification.classList.add('show');
    }, 10);
    
    // 设置自动关闭
    setTimeout(() => {
        notification.classList.remove('show');
        setTimeout(() => {
            document.body.removeChild(notification);
        }, 300);
    }, 3000);
}

// 添加键盘快捷键支持
function initKeyboardShortcuts() {
    document.addEventListener('keydown', function(e) {
        // Alt+D 跳转到仪表盘
        if (e.altKey && e.key === 'd') {
            e.preventDefault();
            switchSection('dashboard');
        }
        // Alt+P 跳转到产品管理
        else if (e.altKey && e.key === 'p') {
            e.preventDefault();
            switchSection('products');
        }
        // Alt+O 跳转到订单管理
        else if (e.altKey && e.key === 'o') {
            e.preventDefault();
            switchSection('orders');
        }
        // Alt+U 跳转到用户管理
        else if (e.altKey && e.key === 'u') {
            e.preventDefault();
            switchSection('users');
        }
    });
}

// 数据导出功能
function exportToExcel(tableId, filename = '数据导出') {
    // 这里可以添加表格数据导出为Excel的逻辑
    showNotification(`数据已导出为${filename}.xlsx`, 'success');
}

// 打印功能
function printPage() {
    window.print();
}

// 搜索功能
function search(query, type = 'product') {
    // 这里可以添加搜索逻辑
    showNotification(`正在搜索: ${query}`, 'info');
}