from __future__ import print_function
import sys
from helpers import *


class Fragment(object):
    def __init__(self, file, start, end, license):
        self.file = file
        self.start = start
        self.end = end
        self.license = license

    def print(self):
        print(self.file + '(' + self.start + ',' + self.end + ')')


class Clone(Fragment):
    def __init__(self, file, start, end, similarity):
        self.file = file
        self.start = start
        self.end = end
        self.similarity = similarity

    def print(self):
        print(self.file + '(' + self.start + ',' + self.end + ') ' + self.similarity)


def format_clone(c, ctype, threshold, include_license):
    parts = c.split('#')
    print(parts)
    # f = None
    # if ctype is 'query':
    if not include_license:
        f = Fragment(parts[0].split('.java_')[0] + '.java', parts[1], parts[2], None)
    else:
        f = Fragment(parts[0].split('.java_')[0] + '.java', parts[1], parts[2], parts[3])
    # else:
    #     try:
    #         if int(parts[3]) >= threshold:
    #             f = Clone(parts[0].split('.java_')[0] + '.java', parts[1], parts[2], parts[3])
    #     except ValueError: # failure from fuzzywuzzy results in no similarity value
    #         return None
    return f


def extract_clone_set(line, prefix, threshold, include_license):
    clones = line.split(',')
    clone_pairs = list()
    if len(clones) >= 2 and clones[1] is not '\n': # skip blank result
        for idx, c in enumerate(clones):
            ctype = 'clones'
            if idx == 0:
                ctype = 'query'

            c = format_clone(c.replace(prefix, ''), ctype, threshold, include_license)
            if c is not None:
                clone_pairs.append(c)

    # if len(clone_pairs) > 1:
    #     for c in clone_pairs:
    #         c.print()

    return clone_pairs


def print_clone_pairs(clones, outputfile):
    for cs in clones:
        query = cs[0] # query
        for c in cs[1:]:
            # print(query.file, query.start, query.end, c.file, c.start, c.end)
            writefile(outputfile,
                      query.file + ',' + query.start + ',' + query.end + ',' +
                      c.file + ',' + c.start + ',' + c.end.strip() + ',' +
                      str(query.license) + ',' + str(c.license).strip() + '\n', 'a', False)


def main():
    inputfile = sys.argv[1]
    prefix = ''
    if len(sys.argv) >= 3:
        prefix = sys.argv[2]
    sim = 80
    if len(sys.argv) >= 4:
        sim = int(sys.argv[3])
    outputfile = '../results/results_for_thesis/so-qualitas_clone_pairs.csv'
    if len(sys.argv) >= 5:
        outputfile = sys.argv[4]
    if len(sys.argv) >= 6:
        include_license = sys.argv[5]
    file = open(inputfile, 'r')
    all_clones = list()
    for line in file:
        clones = extract_clone_set(line, prefix, sim, include_license)
        if len(clones) > 0:
            all_clones.append(clones)
    print_clone_pairs(all_clones, outputfile)


main()