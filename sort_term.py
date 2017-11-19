import pandas as pd


print('processing CSVs ...')
file1='freq_src.csv'
df_src = pd.read_csv(file1, sep=',', header=0)
df_src_sorted = df_src.sort_values(['freq'], ascending=False)
df_src_sorted = df_src_sorted.reset_index(drop=True)
# df_src_sorted.index += 1
# df_src_sorted = df_src_sorted.reset_index()

file2='freq_toksrc.csv'
df_toksrc = pd.read_csv(file2, sep=',', header=0)
df_toksrc_sorted = df_toksrc.sort_values(['freq'], ascending=False)
df_toksrc_sorted = df_toksrc_sorted.reset_index(drop=True)
df_toksrc_sorted.index += 1
df_toksrc_sorted = df_toksrc_sorted.reset_index()

# print(df_toksrc_sorted)
# print(df_src_sorted)

result = pd.concat([df_toksrc_sorted, df_src_sorted], axis=1, join_axes=[df_toksrc_sorted.index])
# print(result)

print('plotting ...')
# normal scale
ax = result.plot(x='index', y='freq')
ax.legend(['original', 'normalised'])
ax.set_xlabel("rank")
ax.set_xlim(0, 30000)
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
fig.savefig('figure_log.pdf')

