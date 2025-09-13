cmake_minimum_required(VERSION 3.10)

# 设置项目名称、版本和语言
project(ModularProject
    VERSION 1.0.0
    DESCRIPTION "A modular C project with math and print libraries"
    LANGUAGES C
)

# 设置C标准
set(CMAKE_C_STANDARD 11)
set(CMAKE_C_STANDARD_REQUIRED ON)

# 设置默认构建类型（如果没有指定）
if(NOT CMAKE_BUILD_TYPE)
    set(CMAKE_BUILD_TYPE Release)
endif()

# 设置编译警告选项
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall -Wextra -Wpedantic")
set(CMAKE_C_FLAGS_DEBUG "${CMAKE_C_FLAGS_DEBUG} -g -O0")
set(CMAKE_C_FLAGS_RELEASE "${CMAKE_C_FLAGS_RELEASE} -O3")

# 添加子目录 - 顺序很重要，依赖项先添加
add_subdirectory(libmath)   # 数学库
add_subdirectory(libprint)  # 打印库
add_subdirectory(app)       # 主应用程序

# 安装目标：将文档和许可证文件安装到标准位置
install(FILES
    README.md
    DESTINATION share/doc/${PROJECT_NAME}
)

# 包含CPack模块以启用打包功能
include(CPack)

# 设置CPack基本配置
set(CPACK_PACKAGE_NAME "${PROJECT_NAME}")
set(CPACK_PACKAGE_VERSION "${PROJECT_VERSION}")
set(CPACK_PACKAGE_VENDOR "Your Company")
set(CPACK_PACKAGE_CONTACT "contact@example.com")
set(CPACK_PACKAGE_DESCRIPTION_SUMMARY "A modular C project with math and print libraries")
set(CPACK_PACKAGE_DESCRIPTION "This project provides a modular architecture for C applications, featuring reusable math and print libraries.")

# 设置包文件名
set(CPACK_PACKAGE_FILE_NAME "${PROJECT_NAME}-${PROJECT_VERSION}-${CMAKE_SYSTEM_NAME}")

# 根据平台设置默认的包生成器
if(WIN32)
    set(CPACK_GENERATOR "ZIP;NSIS")
    set(CPACK_NSIS_MODIFY_PATH ON)
elseif(APPLE)
    set(CPACK_GENERATOR "ZIP;DragNDrop")
else()
    set(CPACK_GENERATOR "DEB;RPM;TGZ")
    # Linux特定设置
    set(CPACK_DEBIAN_PACKAGE_MAINTAINER "Your Name <your.email@example.com>")
    set(CPACK_DEBIAN_PACKAGE_HOMEPAGE "https://github.com/yourusername/ModularProject")
    set(CPACK_RPM_PACKAGE_RELEASE "1")
endif()

# 源码包配置
set(CPACK_SOURCE_GENERATOR "TGZ;ZIP")
set(CPACK_SOURCE_IGNORE_FILES
    "/\\\\.git/"
    "/\\\\.gitignore"
    "/build/"
    "/\\\\.DS_Store"
    "/\\\\.idea/"
    "\\\\.swp$"
    "\\\\.bak$"
    "\\\\.o$"
    "\\\\.a$"
)
set(CPACK_SOURCE_PACKAGE_FILE_NAME "${PROJECT_NAME}-${PROJECT_VERSION}-src")

# 可选：添加组件支持
# set(CPACK_COMPONENTS_ALL applications libraries headers)
# set(CPACK_COMPONENT_APPLICATIONS_DISPLAY_NAME "Applications")
# set(CPACK_COMPONENT_LIBRARIES_DISPLAY_NAME "Libraries")
# set(CPACK_COMPONENT_HEADERS_DISPLAY_NAME "Header Files")

# 打印配置状态信息
message(STATUS "Configuring ${PROJECT_NAME} version ${PROJECT_VERSION}")
message(STATUS "Build type: ${CMAKE_BUILD_TYPE}")
message(STATUS "C compiler: ${CMAKE_C_COMPILER}")
message(STATUS "Install prefix: ${CMAKE_INSTALL_PREFIX}")
message(STATUS "Package generators: ${CPACK_GENERATOR}")