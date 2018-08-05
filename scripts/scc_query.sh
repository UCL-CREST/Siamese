SCCHOME=/scratch0/NOT_BACKED_UP/crest/cragkhit/SourcererCC
CURRENT=`pwd`
INDEX=$1
for i in `seq 1 100`; do
    echo $i
    mkdir -p $CURRENT/results_for_rq3/scc/query/query/$INDEX
    cd $SCCHOME
    rm input/query/*
    cp input/queries/qt$i.file input/query
    { /usr/bin/time -v java -jar dist/indexbased.SearchManager.jar search 8; } &> $CURRENT/results_for_rq3/scc/query/query/$INDEX/"$i"_query.log.txt
done
cd $CURRENT
cat $CURRENT/results_for_rq3/scc/query/query/$INDEX/*_query.log.txt > $CURRENT/results_for_rq3/scc/query/query/$INDEX/all
