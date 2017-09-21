#! /bin/sh
case $1 in
  "build") docker build . -t kfc;;
  "run") docker run -ti --rm --name kfc -p 8080:8080 -v `pwd`:/app kfc sbt;;
  "*") $1;;
esac
