for i in "4" "16" "64" "1024" "16384" "262144"; do
     cat config_index_template.properties | sed -e s:inputFolder=mydir:inputFolder=/scratch0/NOT_BACKED_UP/crest/cragkhit/siamese/data_for_rq3/$i: | sed -e s:index=myindex:index=$i: > myconfig.properties
     echo "indexing: " $i
     /usr/bin/time -v java -jar siamese-1shard-0.0.4-SNAPSHOT.jar -cf myconfig.properties &> "$i".index.txt
     echo "done"
done
# for i in "256" "4096" "65536" "1048576"; do
