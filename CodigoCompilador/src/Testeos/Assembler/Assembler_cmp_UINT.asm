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
$no$entre db "no entre" , 0 
$var2$global dW ? 
$entro$al$if$del$if db "entro al if del if" , 0 
$10_ui dw 10
$var1$global dW ? 
$4_ui dw 4
.code
main:
MOV AX , $10_ui
MOV $var1$global, AX
MOV AX , $4_ui
MOV $var2$global, AX
MOV AX , $var1$global
CMP AX , $var2$global
JNE label1
invoke MessageBox, NULL, addr $entro$al$if$del$if, addr $entro$al$if$del$if, MB_OK 
JMP label2
label1: 
invoke MessageBox, NULL, addr $no$entre, addr $no$entre, MB_OK 
label2: 
MOV AX , $var1$global
CMP AX , $var2$global
JGE label3
invoke MessageBox, NULL, addr $entro$al$if$del$if, addr $entro$al$if$del$if, MB_OK 
JMP label4
label3: 
invoke MessageBox, NULL, addr $no$entre, addr $no$entre, MB_OK 
label4: 
MOV AX , $var1$global
CMP AX , $var2$global
JG label5
invoke MessageBox, NULL, addr $entro$al$if$del$if, addr $entro$al$if$del$if, MB_OK 
JMP label6
label5: 
invoke MessageBox, NULL, addr $no$entre, addr $no$entre, MB_OK 
label6: 
MOV AX , $var2$global
CMP AX , $var1$global
JG label7
invoke MessageBox, NULL, addr $entro$al$if$del$if, addr $entro$al$if$del$if, MB_OK 
JMP label8
label7: 
invoke MessageBox, NULL, addr $no$entre, addr $no$entre, MB_OK 
label8: 
MOV AX , $var1$global
CMP AX , $var2$global
JE label9
invoke MessageBox, NULL, addr $entro$al$if$del$if, addr $entro$al$if$del$if, MB_OK 
JMP label10
label9: 
invoke MessageBox, NULL, addr $no$entre, addr $no$entre, MB_OK 
label10: 
MOV AX , $var1$global
CMP AX , $var2$global
JLE label11
invoke MessageBox, NULL, addr $entro$al$if$del$if, addr $entro$al$if$del$if, MB_OK 
JMP label12
label11: 
invoke MessageBox, NULL, addr $no$entre, addr $no$entre, MB_OK 
label12: 
MOV AX , $var1$global
CMP AX , $var2$global
JL label13
invoke MessageBox, NULL, addr $entro$al$if$del$if, addr $entro$al$if$del$if, MB_OK 
JMP label14
label13: 
invoke MessageBox, NULL, addr $no$entre, addr $no$entre, MB_OK 
label14: 
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