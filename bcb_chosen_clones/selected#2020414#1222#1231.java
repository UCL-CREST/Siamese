        protected void addDataRow(int index) {
            int size = getRowCount();
            Object[] functions = (Object[]) Array.newInstance(getData().getClass().getComponentType(), size + 1);
            if (index == 0) System.arraycopy(getData(), 0, functions, 1, size); else {
                System.arraycopy(getData(), 0, functions, 0, index);
                System.arraycopy(getData(), index, functions, index + 1, size - index);
            }
            functions[index] = newData(defaultValue(0, index));
            setData(functions);
        }
