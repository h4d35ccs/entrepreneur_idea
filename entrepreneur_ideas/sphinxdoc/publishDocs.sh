#!/bin/bash

# Script para generar la documentación de desarrollador empleando Sphinx
# a partir de los comentarios de JavaDoc
basepath=$(dirname $0)
cd "$basepath"
echo $1
cp  -R -f  build/html/*  $1
echo "sphinx generated documentation moved to: $1"
