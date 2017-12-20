for i in `ls -1 $1`
do
    echo $i
    iconv -f windows-1252 -t utf-8 $i > u$i
done
