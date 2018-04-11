#!/bin/bash

cd $(dirname $(realpath $0))

git remote update
ret=$(git status -uno|grep -E 'selben Stand|up-to-date')
if [ ${#ret} -lt 2 ] ;then
  git pull
  mvn package
  exit 0
fi

exit 1