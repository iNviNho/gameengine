#!/bin/bash
git add .
echo Zadaj presný nazov pripravovaného commitu
read -r varname
git commit -m "$varname"
git push origin master