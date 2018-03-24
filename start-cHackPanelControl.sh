#!/bin/bash
ARGUMENTS="C_Hack"
cd $(dirname $(realpath $0))

git remote update
ret=$(git status -uno|grep "Ihr Branch ist auf dem selben Stand wie 'origin/master'")
if [ ${#ret} -lt 2 ] ;then
  git pull
  mvn package
fi

biggest="0.0.0"
FILES=target/C-Hack-Panel-Control-*-jar-with-dependencies.jar
for f in $FILES
do
  currVer=${f#*-Control-}
  currVer=${currVer%-jar-*}
  biggestNum=${biggest//./0}
  currNum=${currVer//./0}

  if [ $currNum -gt $biggestNum ] ;then
    biggest=$currVer
  fi

done

echo $biggest

java -jar target/C-Hack-Panel-Control-$biggest-jar-with-dependencies.jar $ARGUMENTS

