.386 
.model flat, stdcall 
option casemap :none 
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib 
.data 
$errorDivisionPorCero db " Error Assembler:No se puede realizar la division por cero" , 0 
$errorOverflowMultEntero db " Error Assembler: overflow en producto de enteros ", 0 
$errorOverflowSumaFlotantes db " Error Assembler: overflow en la suma de flotantes ", 0 
$_numero2$global dd 10
$_numero1$global dd 15
$_numero1$global$fun1 dd ? 
$parametro$global$fun1 dd ? 
@aux1 dd ? 
$25_l dd 25
$fun1$_numero1$es$distinto$25_l db "fun1 _numero1 es distinto 25_l" , 0 
$fun1$_numero1$es$igual$25_l db "fun1 _numero1 es igual 25_l" , 0 
$10_l dd 10
$15_l dd 15
.code
$fun1$global: 
MOV EAX , $parametro$global$fun1
ADD EAX , $_numero2$global
MOV @aux1 , EAX 
MOV EAX , @aux1
MOV $_numero1$global$fun1, EAX
MOV EAX , $_numero1$global$fun1
CMP EAX , $25_l
JNE label1
invoke MessageBox, NULL, addr $fun1$_numero1$es$igual$25_l, addr $fun1$_numero1$es$igual$25_l, MB_OK 
JMP label2
label1: 
invoke MessageBox, NULL, addr $fun1$_numero1$es$distinto$25_l, addr $fun1$_numero1$es$distinto$25_l, MB_OK 
label2: 
ret 
main:
MOV EAX , $15_l
MOV $_numero1$global, EAX
MOV EAX , $10_l
MOV $_numero2$global, EAX
MOV EAX , $_numero1$global 
MOV $parametro$global$fun1 , EAX 
call $fun1$global 
invoke ExitProcess, 0 
errorDivisionPorCero: 
invoke MessageBox, NULL, addr $errorDivisionPorCero , addr $errorDivisionPorCero , MB_OK 
invoke ExitProcess, 0 
errorOverflowMultEntero: 
invoke MessageBox, NULL, addr $errorOverflowMultEntero , addr $errorOverflowMultEntero , MB_OK 
invoke ExitProcess, 0 
errorOverflowSumaFlotantes: 
invoke MessageBox, NULL, addr $errorOverflowSumaFlotantes , addr $errorOverflowSumaFlotantes , MB_OK 
invoke ExitProcess, 0 
end main