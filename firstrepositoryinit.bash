#!/bin/bash
git init
echo Zadaj URL GIT repozitara
read -r varname
git remote add origin "$varname"