#! /bin/bash

cmd=$1

case $1 in
  "build") docker build . -t kfc;;
  "up") docker run -ti -d --env-file ./.env -p 8080:8080 --name kfc --rm -v `pwd`:/app kfc bash;;
  "new") docker exec -ti kfc sbt new scalatra/scalatra-sbt.g8;;
  "repl") docker exec -ti kfc bash -c "cd ./kfc && sbt";;
esac
