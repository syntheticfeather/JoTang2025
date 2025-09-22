# 任务1

`gcc -E demo.c -o demo.i`

    这步是在预处理阶段，将demo.c文件中的宏定义、条件编译等内容展开，生成demo.i文件。

`gcc -S demo.i -o demo.s`

    这部是在编译阶段，将demo.i文件编译成汇编语言，生成demo.s文件。

`gcc -c demo.s -o demo.o`  

    这步是在编译阶段，将src/main.c文件编译成目标文件，生成main.o文件。

`gcc demo.o -o demo.out`

    这步是在链接阶段，将main.o和utils.o文件链接成可执行文件my_program。

`./demo.out`

    运行程序

# 任务2

见文件夹