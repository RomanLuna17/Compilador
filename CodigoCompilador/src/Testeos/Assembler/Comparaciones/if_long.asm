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
$8_l dd 8
$var2$global dd ? 
$var1$es$igual$de$var2$mas$2 db "var1 es igual de var2 mas 2" , 0 
$var2$es$menor$igual$a$var1 db "var2 es menor igual a var1" , 0 
$var2$es$mayor$a$var1 db "var2 es mayor a var1" , 0 
$var1$es$mayor$igual$a$var2 db "var1 es mayor igual a var2" , 0 
$var1$es$menor$a$var2 db "var1 es menor a var2" , 0 
@aux2 dd ? 
$var1$es$igual$10 db "var1 es igual 10" , 0 
@aux1 dd ? 
$var2$es$menor$a$var1 db "var2 es menor a var1" , 0 
$var1$es$mayor$que$var2 db "var1 es mayor que var2" , 0 
$var1$es$distinta$de$var2 db "var1 es distinta de var2" , 0 
$var1$es$mayor$a$var2 db "var1 es mayor a var2" , 0 
$var2$es$mayor$igual$a$var1 db "var2 es mayor igual a var1" , 0 
$var1$es$igual$a$var2 db "var1 es igual a var2" , 0 
$2_l dd 2
$var1$es$distinta$de$var2$mas$2 db "var1 es distinta de var2 mas 2" , 0 
$var1$es$menor$igual$a$var2 db "var1 es menor igual a var2" , 0 
$var2$es$menor$var1 db "var2 es menor var1" , 0 
$10_l dd 10
$var1$global dd 10
.code
main:
MOV EAX , $10_l
MOV $var1$global, EAX
MOV EAX , $var1$global
SUB EAX , $2_l
MOV @aux1 , EAX 
MOV EAX , @aux1
MOV $var2$global, EAX
MOV EAX , $var1$global
CMP EAX , $10_l
JNE label1
invoke MessageBox, NULL, addr $var1$es$igual$10, addr $var1$es$igual$10, MB_OK 
label1: 
MOV EAX , $var2$global
CMP EAX , $8_l
JNE label2
invoke MessageBox, NULL, addr $var2$es$igual$8, addr $var2$es$igual$8, MB_OK 
label2: 
MOV EAX , $var1$global
CMP EAX , $var2$global
JNE label3
invoke MessageBox, NULL, addr $var1$es$igual$a$var2, addr $var1$es$igual$a$var2, MB_OK 
JMP label4
label3: 
invoke MessageBox, NULL, addr $var1$es$distinta$de$var2, addr $var1$es$distinta$de$var2, MB_OK 
label4: 
MOV EAX , $var2$global
ADD EAX , $2_l
MOV @aux2 , EAX 
MOV EAX , $var1$global
CMP EAX , @aux2
JE label5
invoke MessageBox, NULL, addr $var1$es$distinta$de$var2$mas$2, addr $var1$es$distinta$de$var2$mas$2, MB_OK 
JMP label6
label5: 
invoke MessageBox, NULL, addr $var1$es$igual$de$var2$mas$2, addr $var1$es$igual$de$var2$mas$2, MB_OK 
label6: 
MOV EAX , $var1$global
CMP EAX , $var2$global
JGE label7
invoke MessageBox, NULL, addr $var1$es$menor$a$var2, addr $var1$es$menor$a$var2, MB_OK 
JMP label8
label7: 
invoke MessageBox, NULL, addr $var1$es$mayor$a$var2, addr $var1$es$mayor$a$var2, MB_OK 
label8: 
MOV EAX , $var1$global
CMP EAX , $var2$global
JG label9
invoke MessageBox, NULL, addr $var1$es$menor$igual$a$var2, addr $var1$es$menor$igual$a$var2, MB_OK 
JMP label10
label9: 
invoke MessageBox, NULL, addr $var1$es$mayor$a$var2, addr $var1$es$mayor$a$var2, MB_OK 
label10: 
MOV EAX , $var1$global
CMP EAX , $var2$global
JLE label11
invoke MessageBox, NULL, addr $var1$es$mayor$que$var2, addr $var1$es$mayor$que$var2, MB_OK 
JMP label12
label11: 
invoke MessageBox, NULL, addr $var1$es$menor$a$var2, addr $var1$es$menor$a$var2, MB_OK 
label12: 
MOV EAX , $var1$global
CMP EAX , $var2$global
JL label13
invoke MessageBox, NULL, addr $var1$es$mayor$igual$a$var2, addr $var1$es$mayor$igual$a$var2, MB_OK 
JMP label14
label13: 
invoke MessageBox, NULL, addr $var1$es$menor$a$var2, addr $var1$es$menor$a$var2, MB_OK 
label14: 
MOV EAX , $var2$global
CMP EAX , $var1$global
JG label15
invoke MessageBox, NULL, addr $var2$es$menor$igual$a$var1, addr $var2$es$menor$igual$a$var1, MB_OK 
JMP label16
label15: 
invoke MessageBox, NULL, addr $var2$es$mayor$a$var1, addr $var2$es$mayor$a$var1, MB_OK 
label16: 
MOV EAX , $var2$global
CMP EAX , $var1$global
JGE label17
invoke MessageBox, NULL, addr $var2$es$menor$a$var1, addr $var2$es$menor$a$var1, MB_OK 
JMP label18
label17: 
invoke MessageBox, NULL, addr $var2$es$mayor$a$var1, addr $var2$es$mayor$a$var1, MB_OK 
label18: 
MOV EAX , $var2$global
CMP EAX , $var1$global
JLE label19
invoke MessageBox, NULL, addr $var2$es$mayor$a$var1, addr $var2$es$mayor$a$var1, MB_OK 
JMP label20
label19: 
invoke MessageBox, NULL, addr $var2$es$menor$a$var1, addr $var2$es$menor$a$var1, MB_OK 
label20: 
MOV EAX , $var2$global
CMP EAX , $var1$global
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