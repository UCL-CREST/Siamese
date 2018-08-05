for i in "4" "16" "64" "256" "1024" "4096" "16384" "65536" "262144" "1048576"; do
#for i in "1024" "1048576" "16" "16384" "256" "262144" "4" "4096" "64" "65536"; do
#for i in "4" "16" "64" "1024" "16384" "262144"; do
     cat config.properties | sed -e s:index=test:index=$i:g > myconfig.properties
     cat myconfig.properties | grep "index="
     #echo "indexing: " $i
     /usr/bin/time -v java -jar siamese-0.0.5-SNAPSHOT.jar -i /scratch0/NOT_BACKED_UP/crest/cragkhit/siamese/data_for_rq3/$i -c index -cf myconfig.properties &> "$i".index.txt
     #echo "done"
done
# for i in "256" "4096" "65536" "1048576"; do
