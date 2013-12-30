#!/bin/bash

# Script para generar la documentación de desarrollador empleando Sphinx
# a partir de los comentarios de JavaDoc
basepath=$(dirname $0)
cd "$basepath"
javasphinx-apidoc -f -o source/ ../src/main/java/
mv -f source/packages.rst source/index.rst
make html
echo "Developer doc successfully created."
