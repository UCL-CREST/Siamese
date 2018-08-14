from __future__ import print_function
import sys


def gen_pairs(line):
    parts = line.split(',')
    return parts


def format_query(query, prefix):
    parts = query.split('#')
    return parts[0].replace(prefix, '').strip() + ',' \
           + parts[1].strip() + '.java,' + parts[2].strip() + ',' \
           + parts[3].replace('.java_method', '').strip()


def format_clone(clone, prefix):
    parts = clone.split('#')
    filepart = parts[0].split('.java_')
    filename = filepart[0].replace(prefix, '').replace('/', ',') + '.java'
    return filename.strip() + ',' + parts[1].strip() + ',' + parts[2].strip()


file = open(sys.argv[1], 'r')
for line in file:
    parts = gen_pairs(line)
    iterparts = iter(parts)
    query = next(iterparts)
    for part in iterparts:
        print(format_query(query, '/scratch0/NOT_BACKED_UP/crest/cragkhit/siamese/') + ',' +
              format_clone(part, '/scratch0/NOT_BACKED_UP/crest/cragkhit/dataset/'))
