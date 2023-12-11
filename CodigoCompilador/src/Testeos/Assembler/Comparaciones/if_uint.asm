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
$var2$es$igual$8 db "var2 es igual 8" , 0 
$var2$global dw ? 
$var1$es$igual$de$var2$mas$2 db "var1 es igual de var2 mas 2" , 0 
$var2$es$menor$igual$a$var1 db "var2 es menor igual a var1" , 0 
$var2$es$mayor$a$var1 db "var2 es mayor a var1" , 0 
$var1$es$mayor$igual$a$var2 db "var1 es mayor igual a var2" , 0 
$var1$es$menor$a$var2 db "var1 es menor a var2" , 0 
@aux2 dw ? 
$var1$es$igual$10 db "var1 es igual 10" , 0 
@aux1 dw ? 
$var2$es$menor$a$var1 db "var2 es menor a var1" , 0 
$8_ui dw 8
$var1$es$mayor$que$var2 db "var1 es mayor que var2" , 0 
$var1$es$distinta$de$var2 db "var1 es distinta de var2" , 0 
$10_ui dw 10
$var1$es$mayor$a$var2 db "var1 es mayor a var2" , 0 
$2_ui dw 2
$var2$es$mayor$igual$a$var1 db "var2 es mayor igual a var1" , 0 
$var1$es$igual$a$var2 db "var1 es igual a var2" , 0 
$var1$es$distinta$de$var2$mas$2 db "var1 es distinta de var2 mas 2" , 0 
$var1$es$menor$igual$a$var2 db "var1 es menor igual a var2" , 0 
$var2$es$menor$var1 db "var2 es menor var1" , 0 
$var1$global dw 10 
.code
main:
MOV AX , $10_ui
MOV $var1$global, AX
MOV AX , $var1$global
SUB AX , $2_ui
MOV @aux1 , AX 
MOV AX , @aux1
MOV $var2$global, AX
MOV AX , $var1$global
CMP AX , $10_ui
JNE label1
invoke MessageBox, NULL, addr $var1$es$igual$10, addr $var1$es$igual$10, MB_OK 
label1: 
MOV AX , $var2$global
CMP AX , $8_ui
JNE label2
invoke MessageBox, NULL, addr $var2$es$igual$8, addr $var2$es$igual$8, MB_OK 
label2: 
MOV AX , $var1$global
CMP AX , $var2$global
JNE label3
invoke MessageBox, NULL, addr $var1$es$igual$a$var2, addr $var1$es$igual$a$var2, MB_OK 
JMP label4
label3: 
invoke MessageBox, NULL, addr $var1$es$distinta$de$var2, addr $var1$es$distinta$de$var2, MB_OK 
label4: 
MOV AX , $var2$global
ADD AX , $2_ui
MOV @aux2 , AX 
MOV AX , $var1$global
CMP AX , @aux2
JE label5
invoke MessageBox, NULL, addr $var1$es$distinta$de$var2$mas$2, addr $var1$es$distinta$de$var2$mas$2, MB_OK 
JMP label6
label5: 
invoke MessageBox, NULL, addr $var1$es$igual$de$var2$mas$2, addr $var1$es$igual$de$var2$mas$2, MB_OK 
label6: 
MOV AX , $var1$global
CMP AX , $var2$global
JAE label7
invoke MessageBox, NULL, addr $var1$es$menor$a$var2, addr $var1$es$menor$a$var2, MB_OK 
JMP label8
label7: 
invoke MessageBox, NULL, addr $var1$es$mayor$a$var2, addr $var1$es$mayor$a$var2, MB_OK 
label8: 
MOV AX , $var1$global
CMP AX , $var2$global
JG label9
invoke MessageBox, NULL, addr $var1$es$menor$igual$a$var2, addr $var1$es$menor$igual$a$var2, MB_OK 
JMP label10
label9: 
invoke MessageBox, NULL, addr $var1$es$mayor$a$var2, addr $var1$es$mayor$a$var2, MB_OK 
label10: 
MOV AX , $var1$global
CMP AX , $var2$global
JLE label11
invoke MessageBox, NULL, addr $var1$es$mayor$que$var2, addr $var1$es$mayor$que$var2, MB_OK 
JMP label12
label11: 
invoke MessageBox, NULL, addr $var1$es$menor$a$var2, addr $var1$es$menor$a$var2, MB_OK 
label12: 
MOV AX , $var1$global
CMP AX , $var2$global
JL label13
invoke MessageBox, NULL, addr $var1$es$mayor$igual$a$var2, addr $var1$es$mayor$igual$a$var2, MB_OK 
JMP label14
label13: 
invoke MessageBox, NULL, addr $var1$es$menor$a$var2, addr $var1$es$menor$a$var2, MB_OK 
label14: 
MOV AX , $var2$global
CMP AX , $var1$global
JG label15
invoke MessageBox, NULL, addr $var2$es$menor$igual$a$var1, addr $var2$es$menor$igual$a$var1, MB_OK 
JMP label16
label15: 
invoke MessageBox, NULL, addr $var2$es$mayor$a$var1, addr $var2$es$mayor$a$var1, MB_OK 
label16: 
MOV AX , $var2$global
CMP AX , $var1$global
JAE label17
invoke MessageBox, NULL, addr $var2$es$menor$a$var1, addr $var2$es$menor$a$var1, MB_OK 
JMP label18
label17: 
invoke MessageBox, NULL, addr $var2$es$mayor$a$var1, addr $var2$es$mayor$a$var1, MB_OK 
label18: 
MOV AX , $var2$global
CMP AX , $var1$global
JLE label19
invoke MessageBox, NULL, addr $var2$es$mayor$a$var1, addr $var2$es$mayor$a$var1, MB_OK 
JMP label20
label19: 
invoke MessageBox, NULL, addr $var2$es$menor$igual$a$var1, addr $var2$es$menor$igual$a$var1, MB_OK 
label20: 
MOV AX , $var2$global
CMP AX , $var1$global
JL label21
invoke MessageBox, NULL, addr $var2$es$mayor$igual$a$var1, addr $var2$es$mayor$igual$a$var1, MB_OK 
JMP label22
label21: 
invoke MessageBox, NULL, addr $var2$es$menor$var1, addr $var2$es$menor$var1, MB_OK 
label22: 
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
