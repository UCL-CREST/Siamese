import pandas as pd
import os
from helpers import *


def main():
    sim_threshold = 100
    prefix = '/home/cragkhit/data/cloverflow/github_max_to_10/'
    dir = '../results/results_for_thesis/'
    clones = pd.read_csv(
        '../results/results_for_thesis/github_license_qr_17-08-18_16-46-780_for_github_query.csv',
        sep=',',
        header=None)
    print('line total:', len(clones))
    count = 0
    count_license_compatible = 0
    compat_license_map = dict()
    incompat_license_map = dict()
    project_map = dict()

    for index, row in clones.iterrows():
        for i in range(1, len(row)):
            if str(row[i]) != 'nan':
                query = row[0].split('#')
                result = row[i].split('#')
                try:
                    writefile(dir + 'results_github_clones_' + str(sim_threshold) + '.csv',
                              str(row[0]).replace('#', ',') + ',' +
                              str(row[i]).replace('#', ',').replace(prefix, '') + '\n', 'a', False)
                    # count the number of clone pairs
                    count += 1
                    # create a frequency map of project names for compatible-license clones
                    # and incompatible-license clones
                    project_name = row[i].split("#")[0].replace(prefix, '').split('/')
                    pname = project_name[0].strip() + '/' + project_name[1].strip()
                    if pname not in project_map:
                        project_map[pname] = 1
                    else:
                        project_map[pname] += 1
                    if query[3] == result[3]:
                        count_license_compatible += 1
                        if query[3] not in compat_license_map:
                            compat_license_map[query[3]] = 1
                        else:
                            compat_license_map[query[3]] += 1
                        writefile(dir + 'results_github_clones_' + str(sim_threshold) + '_compatible.csv',
                                  str(row[0]).replace('#', ',') + ',' +
                                  str(row[i]).replace('#', ',') + '\n', 'a', False)
                    else:
                        license_concat = query[3] + '-' + result[3]
                        if license_concat not in incompat_license_map:
                            incompat_license_map[license_concat] = 1
                        else:
                            incompat_license_map[license_concat] += 1
                        writefile(dir + 'results_github_clones_' + str(sim_threshold) + '_incompatible.csv',
                                  str(row[0]).replace('#', ',') + ',' +
                                  str(row[i]).replace('#', ',') + '\n', 'a', False)
                except IndexError:
                    out = ''
                    for s in row:
                        out += str(s) + ','
                    writefile(dir + 'results_github_skipped' + str(sim_threshold) + '.csv',  out + '\n', 'a', False)

    print('total clones with ' + str(sim_threshold) + '% similarity: ' + str(count))
    print('found clones in ' + str(len(project_map.keys())) + ' GitHub projects:')
    sorted_project_map = [(k, project_map[k]) for k in sorted(project_map, key=project_map.get, reverse=True)]
    print('-' * 50)
    print('top 10 projects:')
    pcount = 0
    for k, v in sorted_project_map:
        if pcount < 10:
            print(k, v)
            pcount += 1
    print('-' * 50)
    print('clones with compatible license: ' + str(count_license_compatible))
    print('clones with incompatible license: ' + str(count - count_license_compatible))
    print('-' * 50)
    print('compatible license:')
    for k, v in compat_license_map.items():
        print(k, v)
    print('-' * 50)
    print('incompatible license:')
    for k, v in incompat_license_map.items():
        print(k, v)
    print('-' * 50)


main()