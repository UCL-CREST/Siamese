for i in `ls -1 $1`
do
    # echo $i;
    if iconv -f utf-8 $i > /dev/null 2>&1 
    then 
        echo "$i: utf-8"  
    else 
        echo "$i: not utf-8" 
	mv $i ../notutf8
    fi
done
