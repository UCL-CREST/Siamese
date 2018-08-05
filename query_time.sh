#!/bin/sh
if [ "$#" -ne 1 ] || ! [ -d "$1" ]; then
    echo "=============================================================================="
    echo "A script to process Siamese and SourcererCC query time log files"
    echo "Usage: $0 DIRECTORY" >&2
    echo "=============================================================================="
    exit 1
fi

DIR=$1
for x in "4" "16" "64" "256" "1024" "4096" "16384" "65536" "262144" "1048576" "bcb"; do
    #echo "processing: " $x
    python src/process_query_time.py $DIR $x
done
