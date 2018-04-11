#!/bin/bash
ARGUMENTS="C_Hack"
SERIAL="/dev/ttyAMA0"
BAUD="115200"

cd $(dirname $(realpath $0))

./update.sh

biggest="0.0.0"
FILES=target/C-Hack-Panel-Control-*-jar-with-dependencies.jar
for f in $FILES
do
  if [ "$f" == "$FILES" ] ;then continue ;fi
  currVer=${f#*-Control-}
  currVer=${currVer%-jar-*}
  biggestNum=${biggest//./0}
  currNum=${currVer//./0}

  if [ $currNum -gt $biggestNum ] ;then
    biggest=$currVer
  fi

done

echo $biggest

#Init Serial
stty -F $SERIAL $BAUD

java -jar target/C-Hack-Panel-Control-$biggest-jar-with-dependencies.jar $ARGUMENTS

