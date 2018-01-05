for i in `ls -1 $1/*.java`
do
    echo $i
    encoding=`uchardet $i`
    echo $encoding
    iconv -f $encoding -t utf-8 $i > $2/$i
done
