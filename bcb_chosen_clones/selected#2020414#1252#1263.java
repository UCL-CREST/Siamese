        protected void removeDataRow(int index) {
            int size = getRowCount() - 1;
            Object[] functions = (Object[]) Array.newInstance(getData().getClass().getComponentType(), size);
            if (index == 0) System.arraycopy(getData(), 1, functions, 0, size); else {
                System.arraycopy(getData(), 0, functions, 0, index);
                System.arraycopy(getData(), index + 1, functions, index, size - index);
            }
            boolean flag = shouldNotyfy();
            setAllChangesNotification(false);
            setData(functions);
            setAllChangesNotification(flag);
        }
