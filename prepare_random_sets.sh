#!/bin/sh
if [ "$#" -ne 3 ] || ! [ -d "$1" ]; then
    echo "=============================================================================="
    echo "A script to randomly select a specified number of files from a given directory"
    echo "Usage: $0 DIRECTORY LIMIT OUTPUTDIR" >&2
    echo "=============================================================================="
    exit 1
fi

LOCATION=$1
LIMIT=$2
OUTPUT=$3
count=1
ls -1 $LOCATION | sort -R | tail -$LIMIT | while read file; do
    # echo $count: $file
    mkdir -p $OUTPUT/$LIMIT
    cp $LOCATION/$file $OUTPUT/$LIMIT
    count=$(($count+1))
done
