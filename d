#! /bin/bash

cmd=$1

case $1 in
  "up") docker run -ti -d --name sbt --rm -v `pwd`:/app scala bash;;
  "new") docker exec -ti sbt sbt new scalatra/scalatra-sbt.g8;;
  "repl") docker exec -ti sbt bash -c "cd ./$3 && sbt";;
esac
