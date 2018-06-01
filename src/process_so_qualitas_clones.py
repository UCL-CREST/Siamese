from __future__ import print_function
import sys
from helpers import *


class Fragment(object):
    def __init__(self, file, start, end):
        self.file = file
        self.start = start
        self.end = end

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


def format_clone(c, ctype, threshold):
    parts = c.split('#')
    f = None
    if ctype is 'query':
        f = Fragment(parts[0].split('.java_')[0] + '.java', parts[1], parts[2])
    else:
        try:
            if int(parts[3]) >= threshold:
                f = Clone(parts[0].split('.java_')[0] + '.java', parts[1], parts[2], parts[3])
        except ValueError: # failure from fuzzywuzzy results in no similarity value
            return None
    return f


def extract_clone_set(line, prefix, threshold):
    clones = line.split(',')
    clone_pairs = list()
    for idx, c in enumerate(clones):
        ctype = 'clones'
        if idx == 0:
            ctype = 'query'

        c = format_clone(c.replace(prefix, ''), ctype, threshold)
        if c is not None:
            clone_pairs.append(c)

    # if len(clone_pairs) > 1:
    #     for c in clone_pairs:
    #         c.print()

    return clone_pairs


def print_clone_pairs(clones):
    for cs in clones:
        query = cs[0] # query
        for c in cs[1:]:
            print(query.file, query.start, query.end, c.file, c.start, c.end, c.similarity)
            writefile('../results/results_for_thesis/so-qualitas_clone_pairs.csv',
                      query.file + ',' + query.start + ',' + query.end + ',' +
                      c.file + ',' + c.start + ',' + c.end + ',' + c.similarity + '\n', 'a', False)


def main():
    inputfile = sys.argv[1]
    prefix = ''
    if len(sys.argv) >= 3:
        prefix = sys.argv[2]
    sim = 80
    if len(sys.argv) >= 4:
        sim = int(sys.argv[3])

    file = open(inputfile, 'r')

    all_clones = list()
    for line in file:
        all_clones.append(extract_clone_set(line, prefix, sim))

    print_clone_pairs(all_clones)


main()