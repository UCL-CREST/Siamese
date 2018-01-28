QUERIES=/scratch0/NOT_BACKED_UP/crest/cragkhit/queries
SCCHOME=/scratch0/NOT_BACKED_UP/crest/cragkhit/SourcererCC
CURRENT=`pwd`
INDEX=$1
for i in `seq 1 100`; do
    echo $i
    mkdir -p $CURRENT/results_for_rq3/scc/query/tokenise/$INDEX
    cd $SCCHOME/parser/java;
    { /usr/bin/time -v java -jar InputBuilderClassic.jar $QUERIES/avg/$i $SCCHOME/input/queries/qt$i.file headers.file functions java 0 0 10 0 false false false 8; } &> $CURRENT/results_for_rq3/scc/query/tokenise/$INDEX/"$i"_tokenise.log.txt
    cd ../../
    #rm input/query/*
    #cp input/queries/qt$i.file input/query
    #java -jar dist/indexbased.SearchManager.jar search 8 # &> results_for_rq3/scc/query/$INDEX/$i.log.txt
done
cd $CURRENT
cat $CURRENT/results_for_rq3/scc/query/tokenise/$INDEX/*.log.txt > $CURRENT/results_for_rq3/scc/query/tokenise/$INDEX/all
