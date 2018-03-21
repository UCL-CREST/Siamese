#!/bin/bash
type=2
origIndexConfig="config_bellon_index_" + $type + ".properties"
origSearchConfig="config_bellon_search_" + $type + ".properties"

for i in `seq 1 40`
do
    clear
    echo "n-gram size: "$i
    iconfig="itemp.properties"
    sconfig="stemp.properties"

    # create the config according the gram size
    cat $origIndexConfig | sed -e s/ngramSize=1/ngramSize=$i/g > $iconfig
    cat $origSearchConfig | sed -e s/ngramSize=1/ngramSize=$i/g > $sconfig

    # index
    mvn exec:java -Dexec.mainClass=crest.siamese.main.Main -Dexec.args="-cf $iconfig" 2> /dev/null
    # wait for the index to be ready to search
    echo "sleeping ..."
    sleep 60s
    # start searching
    mvn exec:java -Dexec.mainClass=crest.siamese.main.Main -Dexec.args="-cf $sconfig" 2> /dev/null

    # rename the result to match with the n-gram size
    mv /Users/Chaiyong/Documents/phd/2017/Siamese/exp_results/bellon_no_qr_* /Users/Chaiyong/Documents/phd/2017/Siamese/exp_results/$i.csv

    # clean up
    rm $iconfig $sconfig
done
