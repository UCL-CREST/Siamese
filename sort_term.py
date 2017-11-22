import pandas as pd
import numpy as np
# import matplotlib.pyplot as plt
# import seaborn as sns


# print('processing CSVs ...')
file1='freq_df_src.csv'
df_src = pd.read_csv(file1, sep=',', header=0)
df_src_sorted = df_src.sort_values(['freq'], ascending=False)
df_src_sorted = df_src_sorted.reset_index(drop=True)

# f = plt.figure()
# plt.hist(df_src_sorted.values, bins=20000, color='green', edgecolor='black', linewidth=1.2)
# plt.xlim(0, 2000)
# # plt.show()
# f.savefig("hist.pdf", bbox_inches='tight')

# df_src_sorted.index += 1
# df_src_sorted = df_src_sorted.reset_index()

file2='freq_df_toksrc.csv'
df_toksrc = pd.read_csv(file2, sep=',', header=0)
df_toksrc_sorted = df_toksrc.sort_values(['freq'], ascending=False)
df_toksrc_sorted = df_toksrc_sorted.reset_index(drop=True)

# f = plt.figure()
# plt.hist(df_toksrc_sorted.values, bins=20000, color='green', edgecolor='black', linewidth=1.2)
# plt.xlim(0, 2000)
# plt.show()
# f.savefig("hist_tok.pdf", bbox_inches='tight')

# df_toksrc_sorted.index += 1
# df_toksrc_sorted = df_toksrc_sorted.reset_index()

# print(df_toksrc_sorted)
# print(df_src_sorted)

result = pd.concat([df_toksrc_sorted, df_src_sorted], axis=1, join_axes=[df_toksrc_sorted.index])
result.index += 1
result = result.reset_index()
print(result)
result.columns = ['index', 'freq', 'freq']
print(result)

print('plotting ...')
# normal scale
ax = result.plot(x='index', y='freq')
ax.legend(['original', 'normalised'])
ax.set_xlabel("rank")
ax.set_xlim(0, 5000)
ax.set_ylabel("frequency")
fig = ax.get_figure()
fig.savefig('figure.pdf', bbox_inches='tight')

# log scale
ax = result.plot(x='index', y='freq')
ax.legend(['original', 'normalised'])
ax.set_xlabel("log(rank)")
ax.set_ylabel("log(frequency)")
ax.set_xscale("log", nonposx='clip')
ax.set_yscale("log", nonposy='clip')
fig = ax.get_figure()
fig.savefig('figure_log.pdf', bbox_inches='tight')

# # ax = df.boxplot()
# ax = result.boxplot(['orig_freq', 'norm_freq'], showfliers=True)
# ax.set_ylim(0, 300)
# ax.set_ylabel("frequency")
# # fig = ax.get_figure()
# # fig.savefig('boxplot.pdf')
# # plt.show()
# fig = plt.gcf()
# fig.savefig('boxplot.pdf')

# box plot
# result2 = result[['orig_freq']].copy()
# ax = result2.boxplot()
# print(results2)
# ax = result.boxplot(column='orig_freq')
# ax.legend(['original', 'normalised'])
# fig = ax.get_figure()
# fig.savefig('boxplot.pdf')

# x = np.random.poisson(lam =3, size=100)
# y = np.random.choice(["S{}".format(i+1) for i in range(6)], size=len(x))
# df = pd.DataFrame({"Scenario":y, "LMP":x})

# axes = sns.boxplot(results2)
# axes.boxplot([result['orig_freq'], result['norm_freq']])
# axes.set_title('Day Ahead Market')
# axes.yaxis.grid(True)
# axes.set_xlabel('Scenario')
# axes.set_ylabel('LMP ($/MWh)')

# plt.show()
