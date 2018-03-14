import pandas as pd
import os
from helpers import *


def main():
    sim_threshold = 100
    prefix = '/home/cragkhit/data/github/'

    clones = pd.read_csv('../results/github_qr_15-01-18_20-06-114_utf8.csv', sep=',', header=None)
    # print(str(clones.loc[7][1]) == 'nan')
    # clones = results.loc[results[1] != pd.np.nan]
    print('total:', len(clones))
    # print(len(clones))

    count = 0
    count_license_compatible = 0
    compat_license_map = dict()
    incompat_license_map = dict()
    project_map = dict()

    for index, row in clones.iterrows():
        # print(row)
        for i in range(1, len(row)):
            if str(row[i]) != 'nan':
                query = row[0].split('#')
                result = row[i].split('#')
                # print(result)
                try:
                    if int(result[3]) >= sim_threshold:
                        # print(row[0], row[i])
                        writefile('../results/results_github_clones_' + str(sim_threshold) + '.csv',
                                  str(row[0]).replace('#', ',') + ',' +
                                  str(row[i]).replace('#', ',') + '\n', 'a', False)

                        project_name = row[i].split("#")[0].replace(prefix, '').split('/')
                        # print(project_name[0], project_name[1])
                        pname = project_name[0].strip() + '/' + project_name[1].strip()
                        if pname not in project_map:
                            project_map[pname] = 1
                        else:
                            project_map[pname] += 1

                        count += 1
                        if query[3] == result[4]:
                            count_license_compatible += 1

                            if query[3] not in compat_license_map:
                                compat_license_map[query[3]] = 1
                            else:
                                compat_license_map[query[3]] += 1

                            writefile('../results/results_github_clones_' + str(sim_threshold) + '_compatible.csv',
                                      str(row[0]).replace('#', ',') + ',' +
                                      str(row[i]).replace('#', ',') + '\n', 'a', False)
                        else:
                            license_concat = query[3] + '-' + result[4]

                            if license_concat not in incompat_license_map:
                                incompat_license_map[license_concat] = 1
                            else:
                                incompat_license_map[license_concat] += 1

                            writefile('../results/results_github_clones_' + str(sim_threshold) + '_incompatible.csv',
                                      str(row[0]).replace('#', ',') + ',' +
                                      str(row[i]).replace('#', ',') + '\n', 'a', False)
                except IndexError as e:
                    # print(index, e)
                    out = ''
                    for s in row:
                        out += str(s) + ','
                    writefile('../results/results_github_skipped' + str(sim_threshold) + '.csv',  out + '\n', 'a', False)

    print('total clones with ' + str(sim_threshold) + '% similarity: ' + str(count))
    print('found clones in ' + str(len(project_map.keys())) + ' GitHub projects:')
    sorted_project_map = [(k, project_map[k]) for k in sorted(project_map, key=project_map.get, reverse=True)]

    count = 0
    for k, v in sorted_project_map:
        if count < 10:
            print(k, v)
            count += 1

    print('clones with compatible license: ' + str(count_license_compatible))
    print('clones with incompatible license: ' + str(count - count_license_compatible))
    print('compatible license:')
    # print(compat_license_map)
    for k, v in compat_license_map.items():
        print(k, v)
    print('incompatible license:')
    # print(incompat_license_map)
    for k, v in incompat_license_map.items():
        print(k, v)


main()