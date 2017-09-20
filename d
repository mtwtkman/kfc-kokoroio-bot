#! /bin/bash

cmd=$1

case $1 in
  "build") docker build . -t kfc;;
  "run") docker run -ti -d --env-file ./.env -p 8080:8080 --name kfc --rm -v `pwd`:/app kfc;;
  "repl") docker exec -ti kfc bash -c "cd ./kfc && sbt";;
esac
