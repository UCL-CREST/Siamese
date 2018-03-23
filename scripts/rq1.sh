#!/usr/bin/env bash
origConfig="config_cloplag.properties.orig"
i=$1 # n-gram
j=$2 # input folder
k=$3 # parse mode
l=$4 # clone cluster file

echo $i $j
iconfig="config_cloplag.properties"

### ARP ###
# create a temp config (MR)
echo "MR"
cat $origConfig | sed -e s/ngramSize=15/ngramSize=$i/g | sed -e s/normBoost=15/normBoost=$i/g > $iconfig
printf "inputFolder="$j"\n" >> $iconfig
printf "parseMode=$k\n" >> $iconfig
printf "cloneClusterFile=$l\n" >> $iconfig
mvn compile exec:java -Dexec.mainClass=crest.siamese.experiment.Experiment $iconfig 2> /dev/null | grep "ARP"
# create a temp config (QR)
echo "QR"
cat $origConfig | sed -e s/ngramSize=15/ngramSize=$i/g | sed -e s/queryReduction=false/queryReduction=true/g | sed -e s/multirep=true/multirep=false/g | sed -e s/normBoost=15/normBoost=$i/g > $iconfig
printf "inputFolder="$j"\n" >> $iconfig
printf "parseMode=$k\n" >> $iconfig
printf "cloneClusterFile=$l\n" >> $iconfig
mvn exec:java -Dexec.mainClass=crest.siamese.experiment.Experiment $iconfig 2> /dev/null | grep "ARP"
# create a temp config (MR-QR)
echo "MR-QR"
cat $origConfig | sed -e s/ngramSize=15/ngramSize=$i/g | sed -e s/queryReduction=false/queryReduction=true/g | sed -e s/multirep=true/multirep=true/g | sed -e s/normBoost=15/normBoost=$i/g > $iconfig
printf "inputFolder="$j"\n" >> $iconfig
printf "parseMode=$k\n" >> $iconfig
printf "cloneClusterFile=$l\n" >> $iconfig
mvn exec:java -Dexec.mainClass=crest.siamese.experiment.Experiment $iconfig 2> /dev/null | grep "ARP"

### MAP ###
# create a temp config (MR)
echo "MR"
cat $origConfig | sed -e s/ngramSize=15/ngramSize=$i/g | sed -e s/normBoost=15/normBoost=$i/g | sed -e s/errorMeasure=arp/errorMeasure=map/g > $iconfig
printf "inputFolder="$j"\n" >> $iconfig
printf "parseMode=$k\n" >> $iconfig
printf "cloneClusterFile=$l\n" >> $iconfig
mvn compile exec:java -Dexec.mainClass=crest.siamese.experiment.Experiment $iconfig 2> /dev/null | grep "MAP"
# create a temp config (QR)
echo "QR"
cat $origConfig | sed -e s/ngramSize=15/ngramSize=$i/g | sed -e s/errorMeasure=arp/errorMeasure=map/g | sed -e s/queryReduction=false/queryReduction=true/g | sed -e s/multirep=true/multirep=false/g | sed -e s/normBoost=15/normBoost=$i/g > $iconfig
printf "inputFolder="$j"\n" >> $iconfig
printf "parseMode=$k\n" >> $iconfig
printf "cloneClusterFile=$l\n" >> $iconfig
mvn exec:java -Dexec.mainClass=crest.siamese.experiment.Experiment $iconfig 2> /dev/null | grep "MAP"
# create a temp config (MR-QR)
echo "MR-QR"
cat $origConfig | sed -e s/ngramSize=15/ngramSize=$i/g | sed -e s/errorMeasure=arp/errorMeasure=map/g | sed -e s/queryReduction=false/queryReduction=true/g | sed -e s/multirep=true/multirep=true/g | sed -e s/normBoost=15/normBoost=$i/g > $iconfig
printf "inputFolder="$j"\n" >> $iconfig
printf "parseMode=$k\n" >> $iconfig
printf "cloneClusterFile=$l\n" >> $iconfig
mvn exec:java -Dexec.mainClass=crest.siamese.experiment.Experiment $iconfig 2> /dev/null | grep "MAP"
