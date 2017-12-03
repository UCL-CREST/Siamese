import pandas as pd

data = pd.read_csv('../results/bcb_10_search_results_manual.txt', sep=',', header=None)
# print(data)

for i in range(1, 11):
    start = i + (i - 1) * 100
    end = i * 100 + i - 1
    # print(start, end)
    q1dat = data.loc[start: end][5]
    t1 = t2 = t3 = fp = 0
    lastT = 0
    sum10 = 0

    for idx, d in enumerate(q1dat):
        if d == 'T1' or d == 'T1*' or d == 'T1*J' or d == 'MT1' or d == 'MT1*' or d == 'MT1*J':
            t1 += 1
            if idx < 10:
                sum10 += 1
        elif d == 'T2' or d == 'T2*' or d == 'T2*J' or d == 'MT2' or d == 'MT2*' or d == 'MT2*J':
            t2 += 1
            if idx < 10:
                sum10 += 1
        elif d == 'T3' or d == 'T3*' or d == 'T3*J'  or d == 'MT3' or d == 'MT3*' or d == 'MT3*J':
            t3 += 1
            if idx < 10:
                sum10 += 1

        try:
            if "T" in d:
                lastT = idx
        except TypeError as e:
            print('idx=', idx)
            print(e)

    for idx, d in enumerate(q1dat):
        if (d == 'F' or d == 'MF' or d == 'MF*' or d == 'MF*J') and idx <= lastT:
            fp += 1

    print(sum10/10, end=' ')
    print((t1 + t2 + t3)/(lastT + 1), t1, t2, t3, lastT + 1, (t1 + t2 + t3), fp)