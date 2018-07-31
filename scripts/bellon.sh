#!/bin/bash
type=$1
start=1
end=40
origIndexConfig="config_bellon_index_t"$type".properties"
origSearchConfig="config_bellon_search_t"$type".properties"
HOME="/Users/Chaiyong/Documents/phd/2017/Siamese"
OUTPUT=t$1_results

mvn compile
rm -rf t$1_results_*

for j in `seq 1 5`; do
    # clean up
    curl -X DELETE "localhost:9200/bellon*?pretty"
    curl -X GET "localhost:9200/_cat/indices?v"
    # Indexing phase
    for i in `seq $start $end`; do
            clear
            echo "type $1, n-gram size: $i"
            iconfig="itemp.properties"
            # create the config according the gram size
            cat $origIndexConfig | sed -e s/ngramSize=1/ngramSize=$i/g | \
            sed -e s/index=bellon/index=bellon$i-$j/g > $iconfig
            # index
            mvn exec:java -Dexec.mainClass=crest.siamese.main.Main -Dexec.args="-cf $iconfig" # 2> /dev/null
            echo "sleeping ..."
            sleep 60s
            rm $iconfig
    done

    # Querying phase
    OUTPUT=t$1_results_$j
    mkdir $OUTPUT
    for i in `seq $start $end`; do
        clear
        echo "type $1, n-gram size: "$i
        sconfig="stemp.properties"
        # create the config according the gram size
        cat $origSearchConfig | \
         sed -e s/ngramSize=1/ngramSize=$i/g | \
         sed -e s/outputFolder=t$1_results/outputFolder=$OUTPUT/g | \
         sed -e s/index=bellon/index=bellon$i-$j/g > $sconfig
        # start searching
        mvn exec:java -Dexec.mainClass=crest.siamese.main.Main -Dexec.args="-cf $sconfig" # 2> /dev/null
        # rename the result to match with the n-gram size
        mv $HOME/$OUTPUT/bellon$i_no_qr_* $HOME/$OUTPUT/$i.csv
        # clean up
        rm $sconfig
#        echo "sleeping ..."
#        sleep 30s
    done
#    echo "sleeping ..."
#    sleep 60s
done