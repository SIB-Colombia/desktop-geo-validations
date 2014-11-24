#!/bin/bash

echo Validación de Departamentos y Municipios...
java -jar ./Municipios/SHPMunicipioV.jar
read -p "Press [Enter] key to continue..."
echo Validación de Areas Marítimas...
java -jar ./AreasMaritimas/SHPAreaMaritimaV.jar
read -p "Press [Enter] key to continue..."


