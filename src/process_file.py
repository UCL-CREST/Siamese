from __future__ import division
import sys

index_type=sys.argv[2]

file = open(sys.argv[1], 'r')

print("settings,precision,mrr,map")
count=0
query_name=""
rank_found=0
rank_found_sum=0
correct_ans_sum=0
avgp=[]
avgp_all=[]

for line in file:
	print(line)
	if index_type in line:
		print(line.split(',',1)[1].strip() + ",",)
	if "QUERY," in line:
		query_name=line.split(',',1)[1].split('$',1)[0]
		#print "\n" + line.split(',',1)[1].strip()
		# reset count
		count = 0
		#print query_name,
	if "ANS," in line:
		count += 1
		answer_name=line.split(',',1)[1].split('$',1)[0]
		# ans.append(answer_name)
		# found relevant doc
		if answer_name == query_name:
			#print answer_name + "," + query_name,
			# for MRR
			if rank_found == 0:
			    rank_found=count
			# for MAP
			correct_ans_sum += 1
			#print "precision at " + str(count) + "=" +  str(correct_ans_sum) + "/" + str(count)  + "=" + str(correct_ans_sum/count)
			# finding avg precision at each relevant doc
			avgp.append(correct_ans_sum/count)
			#print "agvp length = " + str(len(avgp))
		if count == 10:
			#print rank_found,
			rank_found_sum += rank_found
			# find average precision
			avgpsum=0
			for ap in avgp:
				avgpsum += ap
			#print str(avgpsum) + "/" + str(len(avgp))
			if len(avgp) == 0:
				average_precision=0.0
			else:	
				average_precision=avgpsum/len(avgp)
			#print "average precision = " + str(average_precision)
			avgp_all.append(average_precision)
			avgp=[]
			correct_ans_sum=0
		# print answer_name,
	if "PREC," in line:
		split_prec=line.split(',', 1 )
		print(split_prec[1].strip() + ",",)
		print(str(rank_found_sum/100) + ",",)

		sum_of_avgp = 0.0;
		for a in avgp_all:
			sum_of_avgp += a

		print(str(sum_of_avgp/100))
		avgp_all=[]
		rank_found_sum=0
	#else:
	#	print line