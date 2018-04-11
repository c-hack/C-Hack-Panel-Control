#!/bin/bash

cd $(dirname $(realpath $0))

git remote update
ret=$(git status -uno|grep "Ihr Branch ist auf dem selben Stand wie 'origin/master'")
if [ ${#ret} -lt 2 ] ;then
  git pull
  mvn package
  exit 0
fi

exit 1