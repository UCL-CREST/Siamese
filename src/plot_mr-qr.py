import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import numpy as np


df = pd.DataFrame({'Group': ['A', 'A', 'A', 'B', 'C', 'B', 'B', 'C', 'A', 'C'],
                   'Apple': np.random.rand(10),'Orange': np.random.rand(10)})
# df = df[['Group','Apple','Orange']]
dd = pd.melt(df, id_vars=['Group'], value_vars=['Apple', 'Orange'], var_name='Fruits')
sns.boxplot(x='Group', y='value', data=dd, hue='Fruits')
plt.show()
