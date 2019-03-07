import numpy as np
import matplotlib.pyplot as plt


def graph(formula, x_range, xlabel, ylabel, filename):
    x = np.array(x_range)
    y = eval(formula)
    plt.plot(x, y)
    plt.xlim(0, 5000)
    # plt.xscale("log", nonposx='clip')
    # plt.yscale("log", nonposy='clip')
    # plt.show()
    plt.xlabel(xlabel)
    plt.ylabel(ylabel)
    plt.savefig(filename, bbox_inches='tight')


c = 9.6*10**8
b = 27.8
a = 2.23
# f(x)
# graph('30250/(x+1)**1.03', range(1, 3*10**4), 'term rank', 'freq', '../zipf.pdf')
# f'(x)
# graph('-(9.6*(10**8))*(2.23)/(x + 27.8)**(3.23)', range(1, 1425287), 'term rank', 'slope', '../slope.pdf')
# f''(x)
graph('((9.6*10**8)*2.23**2 + (9.6*10**6)*2.23)/(x+27.8)**(4.23)', range(1, 1425287), 'term rank', 'rate of slope change', '../slope_change.pdf')

# #!/usr/bin/env python
# #<examples/doc_basic.py>
# from lmfit import minimize, Minimizer, Parameters, Parameter, report_fit
# import numpy as np
#
# # create data to be fitted
# x = np.linspace(0, 15, 301)
# data = (5. * np.sin(2 * x - 0.1) * np.exp(-x*x*0.025) +
#         np.random.normal(size=len(x), scale=0.2) )
#
# # define objective function: returns the array to be minimized
# def fcn2min(params, x, data):
#     """ model decaying sine wave, subtract data"""
#     amp = params['amp']
#     shift = params['shift']
#     omega = params['omega']
#     decay = params['decay']
#     model = amp * np.sin(x * omega + shift) * np.exp(-x*x*decay)
#     return model - data
#
# # create a set of Parameters
# params = Parameters()
# params.add('amp',   value= 10,  min=0)
# params.add('decay', value= 0.1)
# params.add('shift', value= 0.0, min=-np.pi/2., max=np.pi/2)
# params.add('omega', value= 3.0)
#
#
# # do fit, here with leastsq model
# minner = Minimizer(fcn2min, params, fcn_args=(x, data))
# result = minner.minimize()
#
# # calculate final result
# final = data + result.residual
#
# # write error report
# report_fit(result)
#
# # try to plot results
# try:
#     import pylab
#     pylab.plot(x, data, 'k+')
#     pylab.plot(x, final, 'r')
#     pylab.show()
# except:
#     pass
#
# #<end of examples/doc_basic.py>