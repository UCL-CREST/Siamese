#!/bin/bash
type=$1
start=1
end=40
origIndexConfig="config_bellon_index_t"$type".properties"
origSearchConfig="config_bellon_search_t"$type".properties"

rm -rf t$1_results_*

for j in `seq 1 3`
do
    OUTPUT=t$1_results_$j
    mkdir $OUTPUT
    for i in `seq $start $end`
    do
        clear
        echo "type $1, n-gram size: "$i
        iconfig="itemp.properties"
        sconfig="stemp.properties"

        # create the config according the gram size
        cat $origIndexConfig | sed -e s/ngramSize=1/ngramSize=$i/g > $iconfig
        cat $origSearchConfig | sed -e s/ngramSize=1/ngramSize=$i/g | sed -e s/outputFolder=t$1_results/outputFolder=$OUTPUT/g > $sconfig

        # index
        mvn exec:java -Dexec.mainClass=crest.siamese.main.Main -Dexec.args="-cf $iconfig" # 2> /dev/null

        # wait for the index to be ready to search
        echo "sleeping ..."
        sleep 600s

        # start searching
        mvn exec:java -Dexec.mainClass=crest.siamese.main.Main -Dexec.args="-cf $sconfig" # 2> /dev/null

        # rename the result to match with the n-gram size
        mv /Users/Chaiyong/Documents/phd/2017/Siamese/$OUTPUT/bellon_no_qr_* /Users/Chaiyong/Documents/phd/2017/Siamese/$OUTPUT/$i.csv

        # clean up
        rm $iconfig $sconfig

        echo "sleeping ..."
        sleep 60s

    done

    echo "sleeping ..."
    sleep 60s
done