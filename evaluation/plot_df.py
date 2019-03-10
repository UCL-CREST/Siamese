import pandas as pd
import os
from helpers import *
import math
import numpy as np
import matplotlib
# import matplotlib.pyplot as plt
# import seaborn as sns


def deletefile(filename):
    """ delete the file and recreate it
    :param filename: the name of file
    :return: True = succeeded, False = failed
    """
    try:
        os.remove(filename)
    except OSError:
        return False


def read_csv(filename):
    df = pd.read_csv(filename, sep=',', header=0)
    df_sorted = df.sort_values(['freq'], ascending=False)
    df_sorted = df_sorted.reset_index(drop=True)
    return df_sorted


def plot(df0, df1, df2, df3, dir, index=''):
    result = pd.concat([df0, df1, df2, df3], axis=1, join_axes=[df0.index])
    result.index += 1
    result = result.reset_index()
    result.columns = ['index', 'freq', 'freq', 'freq', 'freq']
    print('plotting ...')
    # normal scale
    ax = result.plot(x='index', y='freq', style=['r-', 'g--', 'b-.', 'k:'])
    # log-log scale
    # ax.set_xscale('log')
    # ax.set_yscale('log')
    ax.legend([r'$r_0$', r'$r_1$', r'$r_2$', r'$r_3$'], prop={'size': 22})
    ax.set_xlabel("token rank (" + index + ")")
    ax.set_xlim(0, 100)
    ax.set_ylabel("document frequency (DF)")
    ax.yaxis.label.set_size(22)
    ax.xaxis.label.set_size(22)
    ax.yaxis.set_tick_params(labelsize=20)
    ax.xaxis.set_tick_params(labelsize=20)
    # set 'K', and 'M', on y-axis tick marks.
    ax = matplotlib.pyplot.gca()
    mkfunc = lambda x, pos: '%1.0fM' % (x * 1e-6) if x >= 1e6 else '%1.0fK' % (x * 1e-3) if x >= 1e3 else '%1.0f' % x
    mkformatter = matplotlib.ticker.FuncFormatter(mkfunc)
    ax.yaxis.set_major_formatter(mkformatter)

    fig = ax.get_figure()
    # fig.set_size_inches(4, 5)
    fig.savefig(dir + '/figure_df_' + index + '.pdf', bbox_inches='tight')


def plot_no_label(df0, df1, df2, df3, index=''):
    result = pd.concat([df3, df2, df1, df0], axis=1, join_axes=[df0.index])
    result.index += 1
    result = result.reset_index()
    result.columns = ['index', 'freq', 'freq', 'freq', 'freq']
    print('plotting ...')
    # normal scale
    ax = result.plot(x='index', y='freq')
    # ax.legend(['original', 'normalised'], prop={'size': 18})
    ax.legend_.remove()
    ax.set_xlabel("token rank (" + index + ")")
    ax.set_xlim(0, 1000)
    # ax.set_ylabel("document frequency (DF)")
    ax.yaxis.label.set_size(24)
    ax.xaxis.label.set_size(24)
    ax.yaxis.set_tick_params(labelsize=22)
    ax.xaxis.set_tick_params(labelsize=22)
    # set 'K', and 'M', on y-axis tick marks.
    ax = matplotlib.pyplot.gca()
    mkfunc = lambda x, pos: '%1.0fM' % (x * 1e-6) if x >= 1e6 else '%1.0fK' % (x * 1e-3) if x >= 1e3 else '%1.0f' % x
    mkformatter = matplotlib.ticker.FuncFormatter(mkfunc)
    ax.yaxis.set_major_formatter(mkformatter)

    fig = ax.get_figure()
    fig.savefig('../figure_df_' + index + '.pdf', bbox_inches='tight')


def compute_slopes(df_list):
    # delete old slope file
    deletefile('../slopes.csv')
    df_list = df_list.reset_index()
    prev = list()
    prev_slope = -1
    writefile('../slopes.csv', 'ranka,freqa,rankb,freqb,slope,sloped,change\n', 'a', False)
    for index, row in df_list.iterrows():
        # get slope between consecutive 10 terms
        if index % 10 == 0:
            if index == 0:
                prev.append(int(row['index']))
                prev.append(int(row['freq']))
            else:
                now = list()
                now.append(int(row['index']))
                now.append(int(row['freq']))
                # calculate the gradient (y2-y1)/(x2-x1)
                slope = (now[1] - prev[1])/(now[0] - prev[0])
                slope_degree = math.degrees(math.atan(math.fabs(now[1] - prev[1])/math.fabs(now[0] - prev[0])))
                if prev_slope != 0:
                    slope_change = (slope - prev_slope)/prev_slope
                else:
                    slope_change = 0
                writefile('../slopes.csv', str(now[0]) + ',' + str(now[1]) + ',' +
                          str(prev[0]) + ',' + str(prev[1]) + ',' + str(slope) + ',' +
                          str(slope_degree) + ',' + str(slope_change) + '\n', 'a', False)
                # change now to previous
                prev = list()
                prev.append(int(row['index']))
                prev.append(int(row['freq']))
                prev_slope = slope


def plot_slopes(filename):
    data = pd.read_csv(filename, sep=',', header=0)
    rank = data.iloc[:, 0:1]
    # raw slope
    slopes = data.iloc[:, 4:5]
    result = pd.concat([rank, slopes], axis=1, join_axes=[rank.index])
    # print(result)
    ax = result.plot(x='ranka', y='slope')
    ax.set_xlabel("rank")
    ax.set_ylabel("slope")
    ax.set_xlim(0, 5000)
    fig = ax.get_figure()
    fig.savefig('../dslopes.pdf', bbox_inches='tight')
    # slope degrees
    sloped = data.iloc[:, 5:6]
    result = pd.concat([rank, sloped], axis=1, join_axes=[rank.index])
    # print(result)
    ax = result.plot(x='ranka', y='sloped')
    ax.set_xlabel("rank")
    ax.set_ylabel("slope (degrees)")
    ax.set_xlim(0, 5000)
    fig = ax.get_figure()
    fig.savefig('../dsloped.pdf', bbox_inches='tight')
    # % slope change
    change = data.iloc[:, 6:7] * 100
    result = pd.concat([rank, change], axis=1, join_axes=[rank.index])
    # print(result)
    ax = result.plot(x='ranka', y='change')
    ax.set_xlabel("rank")
    ax.set_ylabel("slope change (%)")
    ax.set_xlim(0, 5000)
    fig = ax.get_figure()
    fig.savefig('../dslope_change.pdf', bbox_inches='tight')


def main():
    # print('processing CSVs ...')
    index = 'qualitas'
    dir = '../results/results_for_rq0_qr_thresholds/'
    df_t0src_sorted = read_csv(dir + 'freq_df_t0src_' + index + '.csv')
    df_t1src_sorted = read_csv(dir + 'freq_df_t1src_' + index + '.csv')
    df_t2src_sorted = read_csv(dir + 'freq_df_t2src_' + index + '.csv')
    df_t3src_sorted = read_csv(dir + 'freq_df_t3src_' + index + '.csv')

    # if index == 'qualitas' or index == 'bcb':
    #     plot_no_label(df_src_sorted, df_toksrc_sorted, df_t2src_sorted, index)
    # else:
    plot(df_t0src_sorted, df_t1src_sorted, df_t2src_sorted, df_t3src_sorted, dir, index)
    # print('computing slopes ...')
    # compute_slopes(df_toksrc_sorted)
    # plot_slopes('../slopes.csv')


main()
