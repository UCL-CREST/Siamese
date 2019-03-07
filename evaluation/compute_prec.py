import pandas as pd
from helpers import *


def read_csv(filename):
    df = pd.read_csv(filename, sep=',', header=None)
    return df


def count_tpfp_at(df, k):
    tpfp_files = dict()
    for index, row in df.iterrows():
        file, method, start, end = extract_file_name(row[0])
        # print(index, file, method, start, end, row[2], row[3])
        cf = file + '_' + method + '#' + start + '#' + end
        if cf in tpfp_files:
            tpfp = tpfp_files[cf]
            if tpfp[0] + tpfp[1] >= k:
                continue
            if row[2] == 'TP':
                tpfp[0] += 1
                tpfp[int(row[3]) + 1] += 1
            else:
                tpfp[1] += 1
        else:
            tpfp = [0, 1, 0, 0, 0, 0]
            if row[2] == 'TP':
                tpfp = [1, 0, 0, 0, 0, 0]
                tpfp[int(row[3]) + 1] = 1
            tpfp_files[cf] = tpfp
    return tpfp_files


def compute_prec(tpfp_files):
    for key, val in tpfp_files.items():
        k = val[0] + val[1]
        print('prec-at-' + str(k) + ',' + key + ',' + str((val[0] * 1.0)/k) + ',' +
              str(val[2]) + ',' + str(val[3]) + ',' + str(val[4]) + ',' + str(val[5]))


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
    tpfp_files = count_tpfp_at(read_csv('../results/results_for_rq2/10_so_snippets_clones2.csv'), 10)
    compute_prec(tpfp_files)


main()

