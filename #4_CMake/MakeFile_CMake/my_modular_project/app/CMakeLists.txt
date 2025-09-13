# 创建可执行文件
add_executable(modular_app src/main.c)

# 链接所需的库
target_link_libraries(modular_app 
    PRIVATE 
        math    # 链接数学库
        print   # 链接打印库
)

# 设置可执行文件的安装规则
install(TARGETS modular_app DESTINATION bin)