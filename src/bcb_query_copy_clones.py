import pandas as pd
import os


def writefile(filename, fcontent, mode, isprint):
    """
    Write the string content to a file
    copied from
    http://www.pythonforbeginners.com/files/reading-and-writing-files-in-python
    :param filename: name of the file
    :param fcontent: string content to put into the file
    :param mode: writing mode, 'w' overwrite every time, 'a' append to an existing file
    :return: N/A
    """
    # try:
    file = open(filename, mode)
    file.write(fcontent)
    file.close()


def main():
    # TODO:
    # 1) open the files in excel once before running this to fill in the missing columns
    # 2) move the method names into another column
    # 3) remove the extra 51st results (there are 10 of them).
    file1_name = '../results/bcb_search_results_qr-10-10-10.csv'
    file2_name = '../results/bcb_search_results_qr-25-75-10-new_copied.csv'
    data = pd.read_csv(file1_name, sep=',', header=None)
    data2 = pd.read_csv(file2_name, sep=',', header=None)

    QUERIES = 142
    RESULTSIZE = 50

    for i in range(1, (QUERIES + 1)):
        start = i + (i - 1) * RESULTSIZE - 1
        end = i * RESULTSIZE + i - 1
        print('processing:', start, '-', end)
        file1 = data.loc[start: end]
        file2 = data2.loc[start: end]

        for index, row in file1.iterrows():
            if row[1].strip() == 'Q':
                out = str(row[0]) + ',' + row[1] + ',' + row[2] + ',' + row[3] + ',' + \
                      str(row[4]) + ',' + str(row[5]) + '\n'
                writefile(file1_name.replace('.csv', '_copied.csv'), out, 'a', True)
            else:
                matched = False
                for index2, row2 in file2.iterrows():
                    if row[2].strip() == row2[2].strip() \
                            and int(row[4]) == int(row2[4]) \
                            and int(row[5]) == int(row2[5]):
                        out = str(row[0]) + ',' + row[1] + ',' + row[2] + ',' + row[3] + ',' + \
                                  str(row[4]) + ',' + str(row[5]) + ',' + str(row[6]) + ',' + row2[7] + ',M\n'
                        writefile(file1_name.replace('.csv', '_copied.csv'), out, 'a', True)
                        matched = True
                        break
                if not matched:
                    out = str(row[0]) + ',' + row[1] + ',' + row[2] + ',' + row[3] + ',' + \
                          str(row[4]) + ',' + str(row[5]) + ',' + str(row[6]) + ',' + row[7] + ',N\n'
                    writefile(file1_name.replace('.csv', '_copied.csv'), out, 'a', True)



main()