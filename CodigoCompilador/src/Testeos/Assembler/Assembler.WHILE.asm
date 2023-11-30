.386 
.model flat, stdcall 
option casemap :none 
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib 
.data 
$errorDivisionPorCero db " Error Assembler:No se puede realizar la division por cero" 
$errorOverflowMultEntero db " Error Assembler: overflow en producto de enteros " 
$errorOverflowSumaFlotantes db " Error Assembler: overflow en la suma de flotantes " 
$var2$global dW ? 
$var2$es$distinto$de$100 db "var2 es distinto de 100" , 0 
$1_l dd 1
$100_ui dw 100
$var2$es$igual$a$100 db "var2 es igual a 100" , 0 
$suma2$global dW ? 
@aux4 dw ? 
@aux3 dw ? 
@aux2 dd ? 
$1_ui dw 1
@aux1 dd ? 
$var3$global dd ? 
$var3$es$igual$a$100 db "var3 es igual a 100" , 0 
$10_ui dw 10
$suma3$global dd ? 
$entre$al$while db "entre al while" , 0 
$10_l dd 10
$var3$es$distinto$de$100 db "var3 es distinto de 100" , 0 
$100_l dd 100
.code
main:
MOV EAX , $1_l
MOV $var3$global, EAX
MOV EAX , $1_l
MOV $suma3$global, EAX
label1: 
MOV EAX , $suma3$global
CMP EAX , $10_l
JG label2
invoke MessageBox, NULL, addr $entre$al$while, addr $entre$al$while, MB_OK 
MOV EAX , $suma3$global
ADD EAX , $1_l
MOV @aux1 , EAX 
MOV EAX , @aux1
MOV $suma3$global , EAX 
MOV EAX , $var3$global
ADD EAX , $1_l
MOV @aux2 , EAX 
MOV EAX , @aux2
MOV $var3$global , EAX 
JMP label1 
label2: 
MOV EAX , $var3$global
CMP EAX , $100_l
JNE label3
invoke MessageBox, NULL, addr $var3$es$igual$a$100, addr $var3$es$igual$a$100, MB_OK 
JMP label4
label3: 
invoke MessageBox, NULL, addr $var3$es$distinto$de$100, addr $var3$es$distinto$de$100, MB_OK 
label4: 
MOV AX , $1_ui
MOV $var2$global, AX
MOV AX , $1_ui
MOV $suma2$global, AX
label5: 
MOV AX , $suma2$global
CMP AX , $10_ui
JG label6
invoke MessageBox, NULL, addr $entre$al$while, addr $entre$al$while, MB_OK 
MOV AX , $suma2$global
ADD AX , $1_ui
MOV @aux3 , AX 
MOV AX , @aux3
MOV $suma2$global , AX 
MOV AX , $var2$global
ADD AX , $1_ui
MOV @aux4 , AX 
MOV AX , @aux4
MOV $var2$global , AX 
JMP label5 
label6: 
MOV AX , $var2$global
CMP AX , $100_ui
JNE label7
invoke MessageBox, NULL, addr $var2$es$igual$a$100, addr $var2$es$igual$a$100, MB_OK 
JMP label8
label7: 
invoke MessageBox, NULL, addr $var2$es$distinto$de$100, addr $var2$es$distinto$de$100, MB_OK 
label8: 
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