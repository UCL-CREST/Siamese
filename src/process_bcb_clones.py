from __future__ import print_function
import sys


def gen_pairs(line):
    parts = line.split(',')
    return parts


def format_query(query, prefix):
    parts = query.split('#')
    return parts[0].replace(prefix, '') + ',' + parts[1] + '.java,' + parts[2] + ',' + parts[3].replace('.java_method', '')


def format_clone(clone, prefix):
    parts = clone.split('#')
    filepart = parts[0].split('.java_')
    filename = filepart[0].replace(prefix, '').replace('/', ',') + '.java'
    return filename + ',' + parts[1] + ',' + parts[2]


file = open(sys.argv[1], 'r')
for line in file:
    parts = gen_pairs(line)
    iterparts = iter(parts)
    query = next(iterparts)
    # print(format_query(query, '/Users/Chaiyong/Documents/phd/2017/Siamese/bcb_clones/'))
    for part in iterparts:
        print(format_query(query, '/Users/Chaiyong/Documents/phd/2017/Siamese/bcb_clones/') + ',' +
              format_clone(part, '/Users/Chaiyong/Downloads/bcb_dataset/'))
