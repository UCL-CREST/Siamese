    public Object[] getAll(Object key, Object[] array) {
        if (key == null) {
            Class t = (array == null) ? Object.class : array.getClass().getComponentType();
            int sz = size();
            if (array.length < sz) array = (Object[]) java.lang.reflect.Array.newInstance(t, sz);
            for (int x = 0, i = 0; i < collectionList.size(); i++) {
                ArrayList list = (ArrayList) collectionList.get(i);
                copyList = list.toArray(copyList);
                int z = list.size();
                System.arraycopy(copyList, 0, array, x, z);
                x += z;
            }
            if (array.length > sz) java.util.Arrays.fill(array, sz, array.length, null);
            java.util.Arrays.fill(copyList, null);
            return array;
        }
        ArrayList list = (ArrayList) keyMap.get(key);
        return list.toArray(array);
    }
