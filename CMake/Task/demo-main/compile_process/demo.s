	.file	"demo.c"
	.text
	.section	.rodata
.LC1:
	.string	"%d%c"
.LC0:
	.string	" \n"
	.text
	.globl	main
	.type	main, @function
main:
.LFB6:
	.cfi_startproc
	endbr64
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$48, %rsp
	movq	%fs:40, %rax
	movq	%rax, -8(%rbp)
	xorl	%eax, %eax
	movl	$1, -32(%rbp)
	movl	$2, -28(%rbp)
	movl	$3, -24(%rbp)
	movl	$4, -20(%rbp)
	movl	$5, -16(%rbp)
	movl	$5, -36(%rbp)
	movl	$0, -40(%rbp)
	jmp	.L2
.L3:
	movl	-36(%rbp), %eax
	subl	$1, %eax
	cmpl	%eax, -40(%rbp)
	sete	%al
	movzbl	%al, %eax
	cltq
	leaq	.LC0(%rip), %rdx
	movzbl	(%rax,%rdx), %eax
	movsbl	%al, %edx
	movl	-40(%rbp), %eax
	cltq
	movl	-32(%rbp,%rax,4), %eax
	movl	%eax, %esi
	leaq	.LC1(%rip), %rdi
	movl	$0, %eax
	call	printf@PLT
	addl	$1, -40(%rbp)
.L2:
	movl	-40(%rbp), %eax
	cmpl	-36(%rbp), %eax
	jl	.L3
	movl	$0, %edi
	call	exit@PLT
	.cfi_endproc
.LFE6:
	.size	main, .-main
	.ident	"GCC: (Ubuntu 9.4.0-1ubuntu1~20.04.2) 9.4.0"
	.section	.note.GNU-stack,"",@progbits
	.section	.note.gnu.property,"a"
	.align 8
	.long	 1f - 0f
	.long	 4f - 1f
	.long	 5
0:
	.string	 "GNU"
1:
	.align 8
	.long	 0xc0000002
	.long	 3f - 2f
2:
	.long	 0x3
3:
	.align 8
4:
