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