import pandas as pd
from helpers import *
import os
import numpy as np


def main():
    # TODO: pen the files in excel once before running this to fill in the missing columns
    file1_name = '../results/results_for_rq2/precision/bcb_default_3-5-8_e.csv'
    file2_name = '../results/results_for_rq2/precision/old_results/bcb_default_e.csv'
    data = pd.read_csv(file1_name, sep=',', header=None)
    data2 = pd.read_csv(file2_name, sep=',', header=None)
    QUERIES = 96
    RESULTSIZE = 16 # result size + query
    for i in range(1, (QUERIES + 1)):
        start = (i-1) * RESULTSIZE
        end = i * RESULTSIZE - 1

        print('processing:', start, '-', end)
        file1 = data.loc[start: end]
        file2 = data2.loc[start: end]

        for index, c1 in file1.iterrows():
            matched = None
            # out = str(c1[0]) + ',' + str(c1[1]) + ',' + str(c1[2]) + ',' + \
            #       str(c1[3]) + ',' + str(c1[4]) + ',,,\n'
            # writefile(file1_name.replace('.csv', '_copied.csv'), out, 'a', True)
            if index % RESULTSIZE == 0:
                c1 = c1.replace(np.nan, '', regex=True)
                out = str(c1[0]) + ',' + str(c1[1]) + ',' + str(c1[2]) + ',' + \
                      str(c1[3]) + ',' + str(c1[4]) + ',,,\n'
                writefile(file1_name.replace('.csv', '_copied.csv'), out, 'a', True)
            else:
                for index2, c2 in file2.iterrows():
                    c2 = c2.replace(np.nan, '', regex=True)
                    if c1[0] == c2[0] and c1[1] == c2[1] and c1[2] == c2[2] and c1[3] == c2[3]:
                        matched = c2
                        break
                if matched is not None:
                    out = str(matched[0]) + ',' + str(matched[1]) + ',' + str(matched[2]) + ',' + \
                          str(matched[3]) + ',' + str(matched[4]) + ',' + str(matched[5]) + ',' + \
                          str(matched[6]) + ',' + str(matched[7]) + '\n'
                else:
                    c1 = c1.replace(np.nan, '', regex=True)
                    out = str(c1[0]) + ',' + str(c1[1]) + ',' + str(c1[2]) + ',' + \
                          str(c1[3]) + ',' + str(c1[4]) + ',' + str(c1[5]) + ',,\n'
                writefile(file1_name.replace('.csv', '_copied.csv'), out, 'a', True)


main()
