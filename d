#! /bin/bash

cmd=$1

case $1 in
  "build") docker build . -t kfc;;
  "up") docker run -ti -d -p 8080:8080 --name sbt --rm -v `pwd`:/app kfc bash;;
  "new") docker exec -ti sbt sbt new scalatra/scalatra-sbt.g8;;
  "repl") docker exec -ti sbt bash -c "cd ./kfc && sbt";;
esac
