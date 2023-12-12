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
$30_0 dd 30.0
$var4$global dd 40.0 
$var3$global dd 30.0 
$20_0 dd 20.0
$var2$global dd 20.0 
$var3$es$igual$a$41_0 db "var3 es igual a 41.0" , 0 
$10_0 dd 10.0
@aux2 dd ? 
@aux1 dd ? 
$var1$global dd 10.0 
$40_0 dd 40.0
$var3$es$distinto$de$41_0 db "var3 es distinto de 41.0" , 0 
$41_0 dd 41.0
.code
main:
FLD $10_0
FSTP $var1$global
FLD $20_0
FSTP $var2$global
FLD $30_0
FSTP $var3$global
FLD $40_0
FSTP $var4$global
FLD $var2$global 
FTST 
FSTSW AX 
SAHF 
JE errorDivisionPorCero 
FLD $var2$global 
FDIV $var2$global
FSTP @aux1
FLD $var4$global
FADD @aux1
FNSTSW AX 
SAHF 
TEST AH, 1 
JNZ errorOverflowSumaFlotantes 
FSTP @aux2
FLD @aux2
FSTP $var3$global
FLD $var3$global
FCOM $41_0
FSTSW AX 
SAHF 
JNE label1
invoke MessageBox, NULL, addr $var3$es$igual$a$41_0, addr $var3$es$igual$a$41_0, MB_OK 
JMP label2
label1: 
invoke MessageBox, NULL, addr $var3$es$distinto$de$41_0, addr $var3$es$distinto$de$41_0, MB_OK 
label2: 
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
