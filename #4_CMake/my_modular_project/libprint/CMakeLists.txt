# 创建打印库
add_library(print STATIC src/print_utils.c)

# 设置库的包含目录
target_include_directories(print 
    PUBLIC 
        $<BUILD_INTERFACE:${CMAKE_CURRENT_SOURCE_DIR}/include>
        $<INSTALL_INTERFACE:include>
)

# 设置库的属性
set_target_properties(print PROPERTIES
    VERSION 1.0.0
    SOVERSION 1
)

# 安装规则
install(TARGETS print
    LIBRARY DESTINATION lib
    ARCHIVE DESTINATION lib
)

install(DIRECTORY include/ DESTINATION include)