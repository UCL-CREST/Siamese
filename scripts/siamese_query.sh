#!/bin/sh
#for x in "bcb"; do
for x in "4" "16" "64" "256" "1024" "4096" "16384" "65536" "262144" "1048576" "bcb"; do
  echo $x
  count=1
  for i in `seq 1 100`; do
     echo $i
     mkdir -p results_for_rq3/siamese/query_time/$x
     cat config_query_bcb_template.properties | sed -e s:index=myindex:index=$x: | sed -e s:inputFolder=mydir:inputFolder=/scratch0/NOT_BACKED_UP/crest/cragkhit/queries/avg/$count: > myconfig.properties
     /usr/bin/time -v java -jar siamese-0.0.5-SNAPSHOT.jar -cf myconfig.properties &> results_for_rq3/siamese/query_time/$x/$count.log.txt
     count=$(($count+1))
  done
  cat results_for_rq3/siamese/query_time/$x/*.log.txt > results_for_rq3/siamese/query_time/$x/all
done
