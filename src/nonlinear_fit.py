#!/usr/bin/env python
from lmfit import minimize, Minimizer, Parameters, Parameter, report_fit
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np


def zipf2min(params, x, data):
    """ model Zipf-Mandelbrot power law f = C/(r+b)^a """
    c = params['c']
    b = params['b']
    a = params['a']
    model = c/(x + b)**a
    # print(c, b, a, model)
    return model - data


def original():
    # create data to be fitted
    print('processing CSVs ...')
    file1='../freq_df_toksrc_qualitas.csv'
    df_src = pd.read_csv(file1, sep=',', header=0)
    df_src_sorted = df_src.sort_values(['freq'], ascending=False)
    df_src_sorted = df_src_sorted.reset_index(drop=True)
    df_src_sorted.index += 1
    df_src_sorted = df_src_sorted.reset_index()
    # x = df_src_sorted.as_matrix(columns=['index'])[:100000, :1][:, 0]
    # data = df_src_sorted.as_matrix(columns=['freq'])[:100000, :1][:, 0]
    x = df_src_sorted.as_matrix(columns=['index'])[:, 0]
    data = df_src_sorted.as_matrix(columns=['freq'])[:, 0]
    print(x)
    print(data)
    # exit()
    # create a set of Parameters
    params = Parameters()
    # params.add('c', value=10**6, min=10**5)
    # params.add('b', value=2, min=0, max=5)
    # params.add('a', value=1, min=0, max=3)

    params.add('c', value=3*(10**6))
    params.add('b', value=5)
    params.add('a', value=1.07)

    print('fitting the model ...')
    # do fit, here with leastsq model
    minner = Minimizer(zipf2min, params, fcn_args=(x, data))
    result = minner.minimize()
    # calculate final result
    final = data + result.residual
    # write error report
    report_fit(result)
    print('r^2 = ', 1 - result.residual.var() / np.var(data))

    # # try to plot results
    plt.plot(x, data, 'k+')
    plt.plot(x, final, 'r')
    plt.xlim(0, 100)
    plt.legend(['original', 'regression'])
    plt.xlabel("token rank")
    plt.ylabel("document frequency (DF)")
    plt.show()
    # plt.savefig('../plot_fitting.pdf', bbox_inches='tight')


def normalised():
    # create data to be fitted
    print('processing CSVs ...')
    file1='../freq_df_src_qualitas.csv'
    df_src = pd.read_csv(file1, sep=',', header=0)
    df_src_sorted = df_src.sort_values(['freq'], ascending=False)
    df_src_sorted = df_src_sorted.reset_index(drop=True)
    df_src_sorted.index += 1
    df_src_sorted = df_src_sorted.reset_index()
    # x = df_src_sorted.as_matrix(columns=['index'])[:1000, :1][:, 0]
    # data = df_src_sorted.as_matrix(columns=['freq'])[:1000, :1][:, 0]
    x = df_src_sorted.as_matrix(columns=['index'])[:, 0]
    data = df_src_sorted.as_matrix(columns=['freq'])[:, 0]
    # print(np.isnan(x).any())
    # print(np.isnan(data).any())
    # exit()
    # create a set of Parameters
    params = Parameters()
    params.add('c', value=10**5, min=0)
    params.add('b', value=2, min=0)
    params.add('a', value=1.2, min=0)

    print('fitting the model ...')
    # do fit, here with leastsq model
    minner = Minimizer(zipf2min, params, fcn_args=(x, data))
    result = minner.minimize()
    # calculate final result
    final = data + result.residual
    # write error report
    report_fit(result)
    print('r^2 = ', 1 - result.residual.var() / np.var(data))

    # # try to plot results
    try:
        plt.plot(x, data, 'k+')
        plt.plot(x, final, 'r')
        plt.xscale("log", nonposx='clip')
        plt.yscale("log", nonposy='clip')
        plt.show()
    except:
        pass


def main():
    original()
    # normalised()


main()