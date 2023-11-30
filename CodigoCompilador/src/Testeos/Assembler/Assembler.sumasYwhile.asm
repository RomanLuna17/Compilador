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
@aux8 dw ? 
@aux7 dd ? 
@aux6 dd ? 
@aux5 dd ? 
@aux4 dd ? 
@aux3 dd ? 
@aux2 dd ? 
$2_l dd 2
$Se$ejecuto$la$funcion$1 db "Se ejecuto la funcion 1" , 0 
$1_l dd 1
$30_l dd 30
$var3$global dd ? 
$_var5$es$diferente$a$3 db "_var5 es diferente a 3" , 0 
$_var1$global dd ? 
$long1$global$c1 dd ? 
$8_l dd 8
$10_l dd 10
$_var2$global dd ? 
$clase1_long1$global dd ? 
$_a$global$_f1 dd ? 
$_var1$es$igual$a$30 db "_var1 es igual a 30" , 0 
$100_l dd 100
$_var4$global dd ? 
$3_ui dw 3
$_var5$global dW ? 
$5_l dd 5
$1_ui dw 1
$20_l dd 20
$_var4$es$igual$a$_var2 db "_var4 es igual a _var2" , 0 
$_var4$es$igual$a$_var1 db "_var4 es igual a _var1" , 0 
@aux9 dd ? 
.code
$_f1$global: 
MOV EAX , $10_l
MOV $_a$global$_f1, EAX
invoke MessageBox, NULL, addr $Se$ejecuto$la$funcion$1, addr $Se$ejecuto$la$funcion$1, MB_OK 
ret 
main:
MOV EAX , $100_l
MOV $_var1$global, EAX
MOV EAX , $20_l
ADD EAX , $_var1$global
MOV @aux2 , EAX 
MOV EAX , @aux2
ADD EAX , $2_l
MOV @aux3 , EAX 
MOV EAX , @aux3
SUB EAX , $_var1$global
MOV @aux4 , EAX 
MOV EAX , @aux4
MOV $_var2$global , EAX 
MOV EAX , $_var2$global
IMUL EAX , $5_l
JO errorOverflowMultEntero 
MOV @aux5 , EAX 
MOV EAX , @aux5
ADD EAX , $_var1$global
MOV @aux6 , EAX 
MOV EAX , @aux6
SUB EAX , $_var2$global
MOV @aux7 , EAX 
MOV EAX , @aux7
MOV $var3$global , EAX 
MOV AX , $1_ui
MOV $_var5$global, AX
label1: 
MOV AX , $_var5$global
CMP AX , $3_ui
JE label2
MOV AX , $_var5$global
ADD AX , $1_ui
MOV @aux8 , AX 
MOV AX , @aux8
MOV $_var5$global , AX 
invoke MessageBox, NULL, addr $_var5$es$diferente$a$3, addr $_var5$es$diferente$a$3, MB_OK 
JMP label1 
label2: 
MOV EAX , $10_l
CMP EAX , $8_l
JLE label3
FILD $_var1$global 
FLD $_var1$global
FST $_var4$global
invoke MessageBox, NULL, addr $_var4$es$igual$a$_var1, addr $_var4$es$igual$a$_var1, MB_OK 
JMP label4
label3: 
FILD $_var2$global 
FLD $_var2$global
FST $_var4$global
invoke MessageBox, NULL, addr $_var4$es$igual$a$_var2, addr $_var4$es$igual$a$_var2, MB_OK 
label4: 
MOV EAX , $_var1$global
CMP EAX , $_var2$global
JE label5
MOV EAX , $30_l
MOV $_var1$global, EAX
invoke MessageBox, NULL, addr $_var1$es$igual$a$30, addr $_var1$es$igual$a$30, MB_OK 
label5: 
MOV EAX , $10_l
MOV $clase1_long1$global, EAX
label6: 
MOV EAX , $clase1_long1$global
CMP EAX , $5_l
JLE label7
MOV EAX , $clase1_long1$global 
MOV $_a$global$_f1 , EAX 
call $_f1$global 
MOV EAX , $clase1_long1$global
SUB EAX , $1_l
MOV @aux9 , EAX 
MOV EAX , @aux9
MOV $clase1_long1$global , EAX 
JMP label6 
label7: 
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