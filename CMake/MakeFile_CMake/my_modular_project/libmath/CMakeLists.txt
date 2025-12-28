# 创建数学库
add_library(math STATIC src/math_utils.c)

# 设置库的包含目录
target_include_directories(math 
    PUBLIC 
        # PUBLIC表示使用此库的目标也会自动包含这个目录
        $<BUILD_INTERFACE:${CMAKE_CURRENT_SOURCE_DIR}/include>
        $<INSTALL_INTERFACE:include>
)

# 设置库的属性
set_target_properties(math PROPERTIES
    VERSION 1.0.0
    SOVERSION 1
)

# 安装规则：将库和头文件安装到系统目录
install(TARGETS math
    LIBRARY DESTINATION lib
    ARCHIVE DESTINATION lib
)

install(DIRECTORY include/ DESTINATION include)