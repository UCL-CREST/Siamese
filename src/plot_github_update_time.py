import matplotlib.pyplot as plt
import sys
import numpy as np
import seaborn as sns
import pandas as pd

interval = 20


def extract_data():
    releases = []
    update_time = []
    labels = []
    with open(sys.argv[1], encoding="utf-8") as file:
        times = [l.strip() for l in file]

    for idx, time in enumerate(times):
        splitted_time = time.split(',')
        releases.append(splitted_time[0])
        deletion_time = splitted_time[1].split(':')
        # print(deletion_time)
        indexing_time = splitted_time[2].split(':')
        # print(indexing_time)
        total = float(deletion_time[0]) * 60 + float(deletion_time[1]) + float(indexing_time[0]) * 60 + float(indexing_time[1])
        # print(len(time.split(',')), time.split(','))
        # print(total)
        update_time.append(total)
        if idx % interval == 0:
            labels.append(splitted_time[0])

    # for idx, release in enumerate(releases):
    #     print(release, update_time[idx])
    # print(labels)
    return releases, update_time, labels


def plot():
    fig = plt.figure()
    ax = fig.add_subplot(111)
    ax.plot(update_time, c="b", marker="s", label="Elasticsearch")
    # plt.yscale('log', basey=10)
    # plt.xscale('log', basex=10)
    # plt.ylim(ymax=100)
    # plt.xlabel("Project")
    plt.ylabel("Update time (s)")
    plt.legend(loc=2)
    plt.xticks(np.arange(1, 214, interval), labels, rotation=45)
    # plt.show()

    fig = ax.get_figure()
    fig.savefig('update_time.pdf', bbox_inches='tight')


def boxplot(data1, data2, data3):
    df = pd.DataFrame(
        {'RxJava': data1,
         'java-design-patterns': data1,
         'Elasticsearch': data1
         })

    print(df)
    sns.set_style("whitegrid")
    ax = sns.boxplot(data=df)
    ax.yaxis.label.set_size(18)
    ax.xaxis.label.set_size(18)
    ax.yaxis.set_tick_params(labelsize=16)
    ax.xaxis.set_tick_params(labelsize=16)
    plt.ylabel("index update time (s)")
    plt.show()

    fig = ax.get_figure()
    fig.savefig('../update_time.pdf', bbox_inches='tight')


def main():
    releases, update_time, labels = extract_data()
    # plot()
    boxplot(update_time, update_time, update_time)


main()