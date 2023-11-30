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
$var2$global dd ? 
$entro$al$if$del$if db "entro al if del if" , 0 
$10_0 dd 10.0
@aux1 dd ? 
$var1$global dd ? 
$2_0 dd 2.0
$no$entre db "no entre" , 0 
.code
main:
FLD $10_0
FST $var1$global
FLD $var1$global
FSUB $2_0
FST @aux1
FLD @aux1
FST $var2$global
FLD $var1$global
FCOM $var2$global
FSTSW AX 
SAHF 
JNE label1
invoke MessageBox, NULL, addr $entro$al$if$del$if, addr $entro$al$if$del$if, MB_OK 
JMP label2
label1: 
invoke MessageBox, NULL, addr $no$entre, addr $no$entre, MB_OK 
label2: 
FLD $var1$global
FCOM $var2$global
FSTSW AX 
SAHF 
JGE label3
invoke MessageBox, NULL, addr $entro$al$if$del$if, addr $entro$al$if$del$if, MB_OK 
JMP label4
label3: 
invoke MessageBox, NULL, addr $no$entre, addr $no$entre, MB_OK 
label4: 
FLD $var1$global
FCOM $var2$global
FSTSW AX 
SAHF 
JL label5
invoke MessageBox, NULL, addr $entro$al$if$del$if, addr $entro$al$if$del$if, MB_OK 
JMP label6
label5: 
invoke MessageBox, NULL, addr $no$entre, addr $no$entre, MB_OK 
label6: 
FLD $var1$global
FCOM $var2$global
FSTSW AX 
SAHF 
JG label7
invoke MessageBox, NULL, addr $entro$al$if$del$if, addr $entro$al$if$del$if, MB_OK 
JMP label8
label7: 
invoke MessageBox, NULL, addr $no$entre, addr $no$entre, MB_OK 
label8: 
FLD $var2$global
FCOM $var1$global
FSTSW AX 
SAHF 
JG label9
invoke MessageBox, NULL, addr $entro$al$if$del$if, addr $entro$al$if$del$if, MB_OK 
JMP label10
label9: 
invoke MessageBox, NULL, addr $no$entre, addr $no$entre, MB_OK 
label10: 
FLD $var1$global
FCOM $var2$global
FSTSW AX 
SAHF 
JE label11
invoke MessageBox, NULL, addr $entro$al$if$del$if, addr $entro$al$if$del$if, MB_OK 
JMP label12
label11: 
invoke MessageBox, NULL, addr $no$entre, addr $no$entre, MB_OK 
label12: 
FLD $var1$global
FCOM $var2$global
FSTSW AX 
SAHF 
JLE label13
invoke MessageBox, NULL, addr $entro$al$if$del$if, addr $entro$al$if$del$if, MB_OK 
JMP label14
label13: 
invoke MessageBox, NULL, addr $no$entre, addr $no$entre, MB_OK 
label14: 
FLD $var1$global
FCOM $var2$global
FSTSW AX 
SAHF 
JGE label15
invoke MessageBox, NULL, addr $entro$al$if$del$if, addr $entro$al$if$del$if, MB_OK 
JMP label16
label15: 
invoke MessageBox, NULL, addr $no$entre, addr $no$entre, MB_OK 
label16: 
FLD $var1$global
FCOM $var2$global
FSTSW AX 
SAHF 
JL label17
invoke MessageBox, NULL, addr $entro$al$if$del$if, addr $entro$al$if$del$if, MB_OK 
JMP label18
label17: 
invoke MessageBox, NULL, addr $no$entre, addr $no$entre, MB_OK 
label18: 
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