# cmake最低版本
cmake_minimum_required(VERSION 3.10) 

# 项目名称
project(main LANGUAGES C) 

# c语言标准
set(CMAKE_C_STANDARD 11) 

# c语言编译选项
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall -Wextra -Werror") 

# 头文件目录
include_directories(include) 

# 添加可执行文件
add_executable(my_project lib/add.c lib/mu.c hello.c)