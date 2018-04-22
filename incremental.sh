#!/bin/sh
if [ "$#" -ne 1 ] || ! [ -d "$1" ]; then
    echo "=============================================================================="
    echo "A script to perform Siamese incremental update"
    echo "Usage: $0 DIRECTORY" >&2
    echo "=============================================================================="
    exit 1
fi

PROJHOME=$1
SHOME=/home/cragkhit/data/siamese_study/Siamese
rm -rf $PROJHOME/unzipped/*
cp $PROJHOME/0.10.0.zip $PROJHOME/unzipped
cd $PROJHOME/unzipped
unzip 0.10.0.zip > /dev/null
rm 0.10.0.zip

cd $SHOME
java -jar siamese-0.0.5-SNAPSHOT.jar -cf config_incremental_index.properties
rm -rf $PROJHOME/unzipped/*
cd $PROJHOME
for i in `ls *.zip`; do 
    echo $i
	cp $i unzipped 
	cd unzipped 
	unzip $i  > /dev/null
	ls -l 
	rm $i 
	# read
	cd $SHOME
    echo ">> START-UPDATE"
	/usr/bin/time -v java -jar siamese-0.0.5-SNAPSHOT.jar -cf config_delete_incremental.properties
	/usr/bin/time -v java -jar siamese-0.0.5-SNAPSHOT.jar -cf config_incremental_index.properties
    echo ">> END-UPDATE"
	cd $PROJHOME/unzipped
	rm -rf elasticsearch*
	cd ..
done
