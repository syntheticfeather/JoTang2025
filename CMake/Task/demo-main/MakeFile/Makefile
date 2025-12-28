#指定编译器
CC = gcc

#编译选项
CFLAGS = -g -O2 -Wall -Werror -I include

# 最终目标
TARGET = my_program

# 所有源文件
SRCS = lib/add.c lib/mu.c hello.c

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
# make clean指令
.PHONY: clean
clean:
	rm -f $(TARGET) $(OBJS)

# main.o 和 utils.o 会自动根据模式规则生成，然后又clean掉
