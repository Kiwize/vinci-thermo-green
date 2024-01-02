#!/bin/bash

ant

cd build

#TODO replace static identifier
file=$(ls | grep '007')

echo $file

jarsigner -keystore ../ressources/data/pubkey $file alias-name

jarsigner -verify $file


