@echo off
echo Validación de Departamentos y Municipios...
java -jar Municipios\SHPMunicipioV.jar
pause
echo Validación de Areas Marítimas...
java -jar AreasMaritimas\SHPAreaMaritimaV.jar
pause
exit