import matplotlib.pyplot as plt
import sys
import numpy as np
import seaborn as sns
import pandas as pd

interval = 20


def extract_data(file):
    releases = []
    update_time = []
    labels = []
    with open(file, encoding="utf-8") as file:
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
    df1 = pd.DataFrame({'RxJava': data1})
    df2 = pd.DataFrame({'java-design-patterns': data2})
    df3 = pd.DataFrame({'Elasticsearch': data3})
    df = pd.concat([df1,df2,df3], ignore_index=True, axis=1)
    df.columns = ['RxJava', 'java-design-patterns', 'Elasticsearch']
    # print(df)
    sns.set_style("whitegrid")
    ax = sns.boxplot(data=df)
    ax.yaxis.label.set_size(18)
    ax.xaxis.label.set_size(18)
    ax.yaxis.set_tick_params(labelsize=16)
    ax.xaxis.set_tick_params(labelsize=16)
    plt.ylabel("index update time (s)")
    # plt.show()

    fig = ax.get_figure()
    fig.set_size_inches(10, 5)
    fig.savefig('../update_time.pdf', bbox_inches='tight')


def stats(data1, data2, data3):
    print('mean', np.mean(data1), 'med', np.median(data1), 'max', max(data1))
    print('mean', np.mean(data2), 'med', np.median(data2), 'max', max(data2))
    print('mean', np.mean(data3), 'med', np.median(data3), 'max', max(data3), 'min', min(data3))


def main():
    releases1, update_time1, labels1 = extract_data(sys.argv[1])
    releases2, update_time2, labels2 = extract_data(sys.argv[2])
    releases3, update_time3, labels3 = extract_data(sys.argv[3])
    stats(update_time1, update_time2, update_time3)
    # plot()
    boxplot(update_time1, update_time2, update_time3)


main()