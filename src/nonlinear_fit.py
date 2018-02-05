#!/usr/bin/env python
from lmfit import minimize, Minimizer, Parameters, Parameter, report_fit
import pandas as pd
import numpy as np

# create data to be fitted
print('processing CSVs ...')
file1='../freq_df_src.csv'
df_src = pd.read_csv(file1, sep=',', header=0)
df_src_sorted = df_src.sort_values(['freq'], ascending=False)
df_src_sorted = df_src_sorted.reset_index(drop=True)
df_src_sorted.index += 1
df_src_sorted = df_src_sorted.reset_index()
x = df_src_sorted.as_matrix(columns=['index'])
data = df_src_sorted.as_matrix(columns=['freq'])
# print(x)
# print(y)
# exit()

# # define objective function: returns the array to be minimized
# def fcn2min(params, x, data):
#     """ model decaying sine wave, subtract data"""
#     amp = params['amp']
#     shift = params['shift']
#     omega = params['omega']
#     decay = params['decay']
#     model = amp * np.sin(x * omega + shift) * np.exp(-x*x*decay)
#     return model - data


def zipf2min(params, x, data):
    """ model Zipf-Mandelbrot power law f = C/(r+b)^a """
    c = params['c']
    b = params['b']
    a = params['a']
    model = c/(x + b)**a
    return model - data


# create a set of Parameters
params = Parameters()
params.add('c',   value=2 * 10**5,  min=10**3, max=10**6, brute_step=100)
params.add('b', value=1, min=1, max=3, brute_step=0.1)
params.add('a', value=1, min=1., max=1.5, brute_step=0.01)

# do fit, here with leastsq model
minner = Minimizer(zipf2min, params, fcn_args=(x, data))
result = minner.minimize()

# calculate final result
final = data + result.residual

# write error report
report_fit(result)

# try to plot results
try:
    import pylab
    pylab.plot(x, data, 'k+')
    pylab.plot(x, final, 'r')
    pylab.show()
except:
    pass
