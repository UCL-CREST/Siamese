import seaborn as sns
import pandas as pandas
import matplotlib.pyplot as plt
import numpy

# with open("../results/results_for_rq4/es_sizes", encoding="utf-8") as file:
#     sizes = [l.strip() for l in file]

sizes = pandas.read_csv("../results/results_for_rq4/sizes.csv")
sizes = sizes/1000000
print(sizes)
print('RxJava:', numpy.nanmean(sizes['RxJava']))
print('java-design-patterns:', numpy.nanmean(sizes['java-design-patterns']))
print('Elasticsearch:', numpy.nanmean(sizes['Elasticsearch']))
exit()

sns.set_style("whitegrid")
ax = sns.boxplot(data=sizes)
ax.yaxis.label.set_size(18)
ax.xaxis.label.set_size(18)
ax.yaxis.set_tick_params(labelsize=16)
ax.xaxis.set_tick_params(labelsize=16)
plt.ylabel("size (MB)")
plt.show()

fig = ax.get_figure()
fig.savefig('../proj_sizes.pdf', bbox_inches='tight')