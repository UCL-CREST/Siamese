for x in "4" "16" "64" "256" "1024" "4096" "16384" "65536" "262144" "1048576"; do
  echo $x
  count=1
  for i in `seq 1 100`; do
     mkdir -p results_for_rq3/$x
     cat config_query_bcb_template.properties | sed -e s:index=myindex:index=$x: | sed -e s:inputFolder=mydir:inputFolder=/scratch0/NOT_BACKED_UP/crest/cragkhit/queries/avg/$count: > myconfig.properties
     /usr/bin/time -v java -jar siamese-0.0.4-SNAPSHOT.jar -cf myconfig.properties &> results_for_rq3/$x/$count.log.txt
     count=$(($count+1))
  done
  cat results_for_rq3/$x/*.log.txt > results_for_rq3/$x/all
done
