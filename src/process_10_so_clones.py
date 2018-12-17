from __future__ import print_function
import sys


def gen_pairs(line, limit):
    parts = line.split(',')
    return parts[:limit]


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


def extract_file_name(file):
    # print(file)
    parts = file.split(".java_")
    # print(parts)
    filename = parts[0] + '.java'
    parts2 = parts[1].split('#')
    method = parts2[0]
    start = parts2[1]
    end = parts2[2]
    return filename, method, start, end


def main():
    file = open(sys.argv[1], 'r')
    for line in file:
        parts = gen_pairs(line, 21)
        iterparts = iter(parts)
        query = next(iterparts)
        for part in iterparts:
            print(query + ',' + part)
            # qf, qm, qs, qe = extract_file_name(query)
            # rf, rm, rs, re = extract_file_name(part)
            # print('vim -O ' + rf + ' ' + qf + ' +' + rs)

main()