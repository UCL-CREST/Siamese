import seaborn as sns
import pandas as pandas
import matplotlib.pyplot as plt
import numpy

# with open("../results/results_for_rq4/es_sizes", encoding="utf-8") as file:
#     sizes = [l.strip() for l in file]

sizes = pandas.read_csv("../results/results_for_rq4/sizes.csv")
sizes = sizes/1000 # the original size is in KB
print('Project Min Max Avg. Std')
print('RxJava:', numpy.min(sizes['RxJava']),
      numpy.max(sizes['RxJava']), numpy.nanmean(sizes['RxJava']) , numpy.std(sizes['RxJava']))
print('java-design-patterns:', numpy.min(sizes['java-design-patterns']),
      numpy.max(sizes['java-design-patterns']), numpy.nanmean(sizes['java-design-patterns']),
      numpy.std(sizes['java-design-patterns']))
print('Elasticsearch:', numpy.min(sizes['Elasticsearch']),
      numpy.max(sizes['Elasticsearch']), numpy.nanmean(sizes['Elasticsearch']), numpy.std(sizes['Elasticsearch']))

# sns.set_style("whitegrid")
# ax = sns.boxplot(data=sizes)
# ax.yaxis.label.set_size(18)
# ax.xaxis.label.set_size(18)
# ax.yaxis.set_tick_params(labelsize=16)
# ax.xaxis.set_tick_params(labelsize=16)
# plt.ylabel("size (MB)")
# plt.show()
#
# fig = ax.get_figure()
# # fig.set_size_inches(8, 3)
# fig.savefig('../proj_sizes.pdf', bbox_inches='tight')

sizes = pandas.read_csv("../results/results_for_rq4/files.csv")
print('Project Min Max Avg. Std')
print('RxJava:', numpy.min(sizes['RxJava']),
      numpy.max(sizes['RxJava']), numpy.nanmean(sizes['RxJava']) , numpy.std(sizes['RxJava']))
print('java-design-patterns:', numpy.min(sizes['java-design-patterns']),
      numpy.max(sizes['java-design-patterns']), numpy.nanmean(sizes['java-design-patterns']),
      numpy.std(sizes['java-design-patterns']))
print('Elasticsearch:', numpy.min(sizes['Elasticsearch']),
      numpy.max(sizes['Elasticsearch']), numpy.nanmean(sizes['Elasticsearch']), numpy.std(sizes['Elasticsearch']))

# sns.set_style("whitegrid")
# ax = sns.boxplot(data=sizes)
# ax.yaxis.label.set_size(18)
# ax.xaxis.label.set_size(18)
# ax.yaxis.set_tick_params(labelsize=16)
# ax.xaxis.set_tick_params(labelsize=16)
# plt.ylabel("no. of files")
# plt.show()
#
# fig = ax.get_figure()
# fig.set_size_inches(8, 3)
# fig.savefig('../proj_files.pdf', bbox_inches='tight')

sizes = pandas.read_csv("../results/results_for_rq4/sloc.csv")
print('Project Min Max Avg. Std')
print('RxJava:', numpy.min(sizes['RxJava']),
      numpy.max(sizes['RxJava']), numpy.nanmean(sizes['RxJava']) , numpy.std(sizes['RxJava']))
print('java-design-patterns:', numpy.min(sizes['java-design-patterns']),
      numpy.max(sizes['java-design-patterns']), numpy.nanmean(sizes['java-design-patterns']),
      numpy.std(sizes['java-design-patterns']))
print('Elasticsearch:', numpy.min(sizes['Elasticsearch']),
      numpy.max(sizes['Elasticsearch']), numpy.nanmean(sizes['Elasticsearch']), numpy.std(sizes['Elasticsearch']))