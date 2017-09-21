#! /bin/sh
case $1 in
  "build") docker build . -t kfc;;
  "run") docker run -d --rm --name kfc -p 8080:8080 -v `pwd`:/app kfc sbt;;
  "at") docker attach kfc;;
  "repl") docker exec -ti kfc sh;;
  "*") $1;;
esac
