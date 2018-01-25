count=1
for i in `seq 1 100`; do
     cat config_query_bcb_summit.properties | sed -e s:inputFolder=/scratch0/NOT_BACKED_UP/crest/cragkhit/queries/1:inputFolder=/scratch0/NOT_BACKED_UP/crest/cragkhit/queries/avg/$count: > myconfig.properties
     /usr/bin/time -v java -jar siamese-0.0.4-SNAPSHOT.jar -cf myconfig.properties &> "$count".log.txt
     count=$(($count+1))
done
