origIndexConfig="config_bellon_index.properties"
origSearchConfig="config_bellon_search.properties"

for i in `seq 1 20`
do
    echo $i
    iconfig="itemp.properties"
    sconfig="stemp.preperties"

    # create the config according the gram size
    cat $origIndexConfig | sed -e s/ngramSize=1/ngramSize=$i/g > $iconfig
    cat $origSearchConfig | sed -e s/ngramSize=1/ngramSize=$i/g > $sconfig

    # index and search
    mvn exec:java -Dexec.mainClass=crest.siamese.main.Main -Dexec.args="-cf $iconfig"
    mvn exec:java -Dexec.mainClass=crest.siamese.main.Main -Dexec.args="-cf $sconfig"

    # rename the result to match with the n-gram size
    mv /Users/Chaiyong/Documents/phd/2017/Siamese/exp_results/bellon_no_qr_* /Users/Chaiyong/Documents/phd/2017/Siamese/exp_results/$i.csv

    # clean up
    rm $iconfig $sconfig
done