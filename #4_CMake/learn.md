# 对c文件的生命周期理解

从多个c文件以及h文件到最后的可执行文件整个过程被称为(构建)，有以下步骤:

## 1. 预处理(Preprocessing)：
### **输入多个.c和.h文件**
### **由预处理器进行**

    - 预处理器会处理所有的#include指令:  
        - 就是把#include<stdio.h>这一行用stdio.h的完整内容替代
    - 然后就是宏的展开:
        - #define PI 3.14，就会把所有的PI变成3.14
    - 当然还有h文件中各种#endif #ifdef #else #if等条件编译
    - 最后把各种注释删了

### **输出多个.i文件**

## 2. 编译(Compilation)：(刚好在学编译原理)

    编译器会将预处理后的代码编译成汇编语言。
### **输入多个.i文件**
### **由编译器进行**
      
    前端（Front End）：负责理解源代码。

        词法分析：把字符流变成单词流。

        语法分析：把单词流组合成语法树。

        语义分析：检查这句话有没有“道理”。

    后端（Back End）：负责生成目标代码。

        中间代码生成：生成一种抽象、简单的指令。

        优化：让代码变得更快、更小。

        代码生成：生成最终的机器码或汇编代码。

### **输出多个.s汇编文件**

## 3. 汇编(Assembly)：汇编器会将汇编语言代码转换成机器语言。

### **输入编译的.s文件**
### **由汇编器进行**

    将汇编代码一对一地翻译成机器可以直接执行的机器指令 (Machine Instructions)，即二进制代码。

    但是呢现在还不能运行，因为是一个一个散装的

### **输出.o文件**

## 4. 链接(Linking)：链接器会将所有的目标文件和库文件链接成一个可执行文件。

### **输入多个.o文件**
### **处理工具：链接器 (Linker, 如 ld, link.exe)。**

    将所有.o文件“拼接”在一起，组织成最终可执行文件。

    解析符号：这是链接的核心任务。在一个 .c 文件中，你可能会调用另一个 .c 文件中定义的函数（如 main.c 中调用了 utils.c 里的 calculate() 函数）。在编译单个文件时，calculate() 的地址是未知的，编译器会留下一个“未解析的符号引用”。链接器会在所有目标文件和库中查找这个 calculate 符号的真实地址，并将其修正。

    链接库函数:(如 printf, malloc),它们的代码存在于C标准库（如 libc.a）中。链接器将这些库中的代码（静态链接）或对这些库的引用（动态链接）也加入到最终的可执行文件中。

### **输出：生成最终的可执行文件，在Windows下就是 .exe 文件，在Linux/Unix下是没有扩展名的文件（通常具有可执行权限）。**


# 对gcc makefile Cmake Cmakelists的理解

## 发展角度来看

### 最开始gcc指令进行文件的编译。

    但是后期多文件编译，文件数量上升，每次都要复制一串代码变得非常麻烦，而且当我们只修改个别代码时，需要重新编译整个项目，效率非常低。

### makefile出现，makefile可以自动化编译，可以指定编译的顺序，可以指定编译的依赖关系。

    但是挺难写的，相当于把gcc指令保存下来

### 所以CMake和CMakeLists.txt出现了，通过写非常简单的CMakeLists.txt文件就可以通过CMake实现Makefile的功能。
    
### 总体来说可以建房子来做比方

    CMakeLists就是个写蓝图的，还要规定在哪建(什么操作系统)，有哪些三方公司(依赖库)

    CMake读蓝图，生成一个给工人们用的操作手册(makefile文件)，给下面的包工头讲清楚用什么类型的材料()，哪到哪是墙，哪是门。

    make就是包工头，理清楚一系列的规划。

    然后make指令告诉下面的gcc(各种编译器工具)工人们怎么做，浇水泥，砌砖，搭建房子。

## 整个例子来看看

惯例安装:

`sudo apt update`
`sudo apt install make`
`sudo apt install cmake`

这是文件目录:

    my_project/
    ├── include/
    │   └── utils.h
    └── src/
        ├── main.c
        └── utils.c

### **传统思路(gcc指令):**

`gcc -I include -o my_project src/main.c src/utils.c`
    
    这是一步编译，也就是说没改过的也会编译一遍,效率低

`gcc -c src/main.c -I include -o main.o`  
`gcc -c src/utils.c -I include -o utils.o`

    -Iinclude 告诉编译器在 include 目录中查找头文件，

    可以只重新编译修改过的文件(如果你没有删除.o文件)，

    当然会很麻烦，而且中间产物.o会很多，看着真乱。

`gcc main.o utils.o -o my_program`

    将所有的 .o 文件链接成一个可执行文件
---
### **Makefile思路:**

    my_project/
    ├── include/
    │   └── utils.h
    ├── src/
    │   ├── main.c
    │   └── utils.c
    └── makefile  <-- 我们将完善这个文件

```makefile
# makefile文件
# 定义编译器
CC = gcc
# 编译选项：显示所有警告、包含头文件目录、调试信息
CFLAGS = -Wall -I include -g
# 最终目标
TARGET = my_program
# 所有的源文件
SRCS = src/main.c src/utils.c
# 所有的目标文件（将 .c 替换为 .o）
OBJS = $(SRCS:.c=.o)

# 默认目标：构建最终的可执行文件
# 其实就是 gcc main.o utils.o -o my_program
$(TARGET): $(OBJS)
	$(CC) $(OBJS) -o $(TARGET)

# 模式规则：如何从 .c 文件编译成 .o 文件
%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

# 清理生成的文件
.PHONY: clean
clean:
	rm -f $(TARGET) $(OBJS)

# main.o 和 utils.o 会自动根据模式规则生成，然后又clean掉
```
**执行:**

`make`

`make clean`

**输出:**

```
gcc -Wall -I include -g -c src/main.c -o src/main.o
gcc -Wall -I include -g -c src/utils.c -o src/utils.o
gcc src/main.o src/utils.o -o my_program
```
如果不clean再次编译，并且没有对c文件进行修改，那就只有:  
```
gcc src/main.o src/utils.o -o my_program
```

#### **这样看起来还是有点乱，my_program和.o文件到处都是**

可以优化到都丢到bin目录下:
```
# 定义编译器
CC = gcc
# 编译选项：显示所有警告、包含头文件目录、调试信息
CFLAGS = -Wall -I include -g
# 最终目标（输出到 bin 目录）
TARGET = bin/my_program
# 所有的源文件
SRCS = src/main.c src/utils.c
# 所有的目标文件（将 src/xxx.c 转换为 bin/xxx.o）
OBJS = $(patsubst src/%.c,bin/%.o,$(SRCS))

# 默认目标：构建最终的可执行文件
$(TARGET): $(OBJS)
	@mkdir -p bin  # 确保 bin 目录存在
	$(CC) $^ -o $@

# 模式规则：从 src/ 下的 .c 生成 bin/ 下的 .o
bin/%.o: src/%.c
	@mkdir -p bin  # 确保 bin 目录存在
	$(CC) $(CFLAGS) -c $< -o $@

# 清理生成的文件（删除整个 bin 目录）
.PHONY: clean
clean:
	rm -rf bin

# main.o 和 utils.o 会自动根据模式规则生成，然后又 clean 掉
```

    my_project/
    ├── bin/
    │   └── main.o
    │   └── utils.o
    │   └── my_program
    ├── include/
    │   └── utils.h
    └── src/
        ├── main.c
        └── utils.c

### 看起来也清爽多了


---
### **CMake思路:**

    my_project/
    ├── include/
    │   └── utils.h
    ├── src/
    │   ├── main.c
    │   └── utils.c
    └── CMakeLists.txt  <-- 我们将完善这个文件

**CMakeLists.txt:**

```CMake
# cmake最低版本
cmake_minimum_required(VERSION 3.10) 

# 项目名称
# C(CXX表示C++)
project(main LANGUAGES C) 

# c语言标准
set(CMAKE_C_STANDARD 11) 

# c++语言标准(不用就注释)
# set(CMAKE_CXX_STANDARD 11) 

# c语言编译选项
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall -Wextra -Werror") 

# c++语言编译选项
# set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wall -Wextra -Werror") 

# 头文件目录
include_directories(include) 

# 添加可执行文件
add_executable(my_project src/main.c src/utils.c) 
```

**生成可执行文件**

`mkdir build`  
`cd build`  
`cmake ..`  
`make`  

**运行**
  
`./my_project`

```
gcc@ubuntu:~/Documents/JoTang2025/#4_CMake/my_project/build$ cmake ..
-- Configuring done
-- Generating done
-- Build files have been written to: /home/gcc/Documents/JoTang2025/#4_CMake/my_project/build

gcc@ubuntu:~/Documents/JoTang2025/#4_CMake/my_project/build$ make
Scanning dependencies of target my_project
[ 33%] Building C object CMakeFiles/my_project.dir/src/main.c.o
[ 66%] Building C object CMakeFiles/my_project.dir/src/utils.c.o
[100%] Linking C executable my_project
[100%] Built target my_project

gcc@ubuntu:~/Documents/JoTang2025/#4_CMake/my_project/build$ ./my_project
Hello from the main program!
This is a message from the utils library!
```

    my_project/
    ├── build/
    │   ├── CmakeCache.txt
    │   ├── CMakeFiles/
    │   ....
    ├── include/
    │   └── utils.h
    ├── src/
    │   ├── main.c
    │   └── utils.c
    └── CMakeLists.txt

---
# 当然CMake肯定不止这些，我了解了一下模块化，打包

### **新的目录:**

    my_modular_project/
    ├── CMakeLists.txt              # 根目录的 CMake
    ├── app/
    │   ├── CMakeLists.txt          # 主程序CMake
    │   └── src/
    │       └── main.c              
    ├── libmath/
    │   ├── CMakeLists.txt          # 数学库CMake
    │   ├── include/
    │   │   └── libmath/
    │   │       └── math_utils.h    
    │   └── src/
    │       └── math_utils.c        
    ├── libprint/
    │   ├── CMakeLists.txt          # 打印库CMake
    │   ├── include/
    │   │   └── libprint/
    │   │       └── print_utils.h   
    │   └── src/
    │       └── print_utils.c       
    └── tests/                      # 测试目录(没实现，可以有)
        └── CMakeLists.txt          # 测试的CMake
---
### **模块化部分:**

指令:

```
# 进入项目目录
cd my_modular_project

# 创建构建目录并配置
mkdir build
cd build
cmake ..

# 编译项目
make 

# 运行
./my_project
```

解释:

    库的CMakeLists.txt文件在读库的h文件和c文件，然后设置一下最后的库生成在什么位置

    主程序的CMakeLists.txt文件就是读main.c然后和库链接在一起，生成可执行文件。

当然这CMakeLists.txt看着还是挺累的，为什么这么写，语法是什么样的....

### **前面的路以后再来探索吧**
---
### **至于打包:**

**这部分确实有点复杂，我也没怎么用过，可能还没写对，等用到的时候再研究研究。**


在根目录的CMakeLists.txt文件中加上:

```
# 包含 CPack 模块
include(CPack)

# 设置包的基本信息
set(CPACK_PACKAGE_NAME "${PROJECT_NAME}") 
set(CPACK_PACKAGE_VENDOR "Your Company")  
set(CPACK_PACKAGE_DESCRIPTION_SUMMARY "A modular math and print utilities library")
set(CPACK_PACKAGE_VERSION "${PROJECT_VERSION}")
set(CPACK_PACKAGE_VERSION_MAJOR "${PROJECT_VERSION_MAJOR}")
set(CPACK_PACKAGE_VERSION_MINOR "${PROJECT_VERSION_MINOR}")
set(CPACK_PACKAGE_VERSION_PATCH "${PROJECT_VERSION_PATCH}")

# 设置包的文件名（可选）
set(CPACK_PACKAGE_FILE_NAME "${PROJECT_NAME}-${PROJECT_VERSION}-${CMAKE_SYSTEM_NAME}")

# 设置README文件（如果有的话）
# set(CPACK_RESOURCE_FILE_README "${CMAKE_SOURCE_DIR}/README.md")
```

### **linux上:**

```
# 设置生成DEB和RPM包
set(CPACK_GENERATOR "DEB;RPM;TGZ")

# DEB包特定设置
set(CPACK_DEBIAN_PACKAGE_MAINTAINER "Your Name <your.email@example.com>")
set(CPACK_DEBIAN_PACKAGE_SECTION "utils")
set(CPACK_DEBIAN_PACKAGE_DEPENDS "libc6 (>= 2.19)")

# RPM包特定设置
set(CPACK_RPM_PACKAGE_RELEASE "1")
set(CPACK_RPM_PACKAGE_LICENSE "MIT")
set(CPACK_RPM_PACKAGE_REQUIRES "libc >= 2.19")
```

### **windows上:**

```
# 设置生成NSIS安装程序
set(CPACK_GENERATOR "NSIS;ZIP")

# NSIS安装程序特定设置
set(CPACK_NSIS_MODIFY_PATH ON)  # 将安装目录添加到系统PATH
set(CPACK_NSIS_DISPLAY_NAME "Modular Project")
set(CPACK_NSIS_PACKAGE_NAME "Modular Project")
set(CPACK_NSIS_INSTALLED_ICON_NAME "bin\\\\modular_app.exe")
# set(CPACK_NSIS_HELP_LINK "https://example.com") (没有help网站 T_T)
# set(CPACK_NSIS_URL_INFO_ABOUT "https://example.com") (没有info网站 T_T)
# set(CPACK_NSIS_CONTACT "2391317090@qq.com") (肯定没人找我 T_T)
```

**指令:**

    mkdir build
    cd build
    cmake ..
    make
    make install
    make package
    make package_source

**emmmmmm目前生成出来了一大堆东西，都没见过。先到这把**  


# 有趣的bug和现象

## 切换目录失败

    gcc@ubuntu:~$ ls
    Desktop  Documents  Downloads  Music  Pictures  Public  Templates  test  Videos

    gcc@ubuntu:~$ cd Documents
    gcc@ubuntu:~/Documents$ ls
    '#1_git'   compile   JoTang2025   target

    gcc@ubuntu:~/Documents$ cd JoTang2025
    gcc@ubuntu:~/Documents/JoTang2025$ ls
    '#1_git'  '#2_markdown'  '#3_ubuntu'  '#4_CMake'   linux.md

    gcc@ubuntu:~/Documents/JoTang2025$ cd #4_CMake
    gcc@ubuntu:~$
### 为什么最后cd #4_CMake不能进去?

    linux系统中 # 为特殊字符，需要用\转义，不转义那就是引起注释的意思。

    所以实际上linux终端看到的是

`cd` 

    最后就会回到根目录

## gcc编译后文件的名称差异

    linux 上 gcc demo.c生成的是a.out文件

    而windows 上 gcc demo.c生成的是a文件

    为什么会有这样的差异？

    
### **众所周知，linux是没有后缀名的，那a.out是什么意思呢？**  


    a： 代表 “assembler”（汇编器）。在早期，编译过程的一个关键步骤是生成汇编代码，然后由汇编器处理。这个名字从那个时代沿用了下来，可以简单地理解为“输出”。

    out： 代表 “output”（输出）。所以 a.out 合起来就是 “汇编器的输出” 或 “默认的输出文件”。

    .是linux的一种命名方式，就像python用_命名法，java用驼峰命名法

### **windows上为什么.out没了呢**

    因为windows极度依赖后缀名，

    所以如果命名成a.out.exe 那后缀就是.out.exe

    不是可执行文件。

    所以gcc就把命名改为 a 
    
    然后加上windows的.exe文后缀名。

    如果显示后缀名，最后就是a.exe

### 小结

    操作系统的不同，windows依赖后缀名而linux不依赖，导致了gcc的不同生成文件名。

    完整命名:

        windows: a.exe

        linux: a.out

    但两者的.是完全不一样的含义


## 哎还有个名称也就可以理解了，CMakeLists.txt

那我们linux不是不需要后缀名吗，可是实践发现linux上不能省去.txt，所以这是CMake的强制规定。

## 纯C语言的代码，Cmake为什么会问我C++的编译器在哪呢

```
# cmake最低版本
cmake_minimum_required(VERSION 3.10) 

# 项目名称
project(excuteFile) 

# c语言标准
set(CMAKE_C_STANDARD 11) 

# c语言编译选项
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall -Wextra -Werror") 

# 头文件目录
include_directories(include) 

# 添加可执行文件
add_executable(my_project src/main.c src/utils.c)
```

**报错:**

    gcc@ubuntu:~/Documents/JoTang2025/#4_CMake/my_project/build$ cmake ..
    -- The C compiler identification is GNU 9.4.0
    -- The CXX compiler identification is unknown
    -- Check for working C compiler: /usr/bin/cc
    -- Check for working C compiler: /usr/bin/cc -- works
    -- Detecting C compiler ABI info
    -- Detecting C compiler ABI info - done
    -- Detecting C compile features
    -- Detecting C compile features - done
    CMake Error at CMakeLists.txt:5 (project):
    No CMAKE_CXX_COMPILER could be found.

    Tell CMake where to find the compiler by setting either the environment
    variable "CXX" or the CMake cache entry CMAKE_CXX_COMPILER to the full path
    to the compiler, or to the compiler name if it is in the PATH.


    -- Configuring incomplete, errors occurred!
    See also "/home/gcc/Documents/JoTang2025/#4_CMake/my_project/build/CMakeFiles/CMakeOutput.log".
    See also "/home/gcc/Documents/JoTang2025/#4_CMake/my_project/build/CMakeFiles/CMakeError.log".

**原因:**

CMake因为某种原因，导致设计为C和C++都要检测，

那么我们就需要指定我们的代码的语言

project(excuteFile LANGUAGES C)  # 添加 LANGUAGES C

project(excuteFile LANGUAGES CXX)  # 添加 LANGUAGES C++