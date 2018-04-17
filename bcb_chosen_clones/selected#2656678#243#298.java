    @SuppressWarnings({ "rawtypes", "unchecked", "null" })
    public Object set(Object arrayOrList, int index, Object item, GenericBean<Object> arrayReceiver, int maximumGrowth) throws NlsIllegalArgumentException {
        if (arrayOrList == null) {
            throw new NlsNullPointerException("arrayOrList");
        }
        int maxGrowth = maximumGrowth;
        Class<?> type = arrayOrList.getClass();
        List list = null;
        int size;
        if (type.isArray()) {
            size = Array.getLength(arrayOrList);
            if (arrayReceiver == null) {
                maxGrowth = 0;
            }
        } else if (List.class.isAssignableFrom(type)) {
            list = (List) arrayOrList;
            size = list.size();
        } else {
            throw new NlsIllegalArgumentException(arrayOrList);
        }
        int growth = index - size + 1;
        if (growth > maxGrowth) {
            throw new ContainerGrowthException(growth, maxGrowth);
        }
        if (type.isArray()) {
            if (growth > 0) {
                if (getLogger().isTraceEnabled()) {
                    getLogger().trace("Increasing array size by " + growth);
                }
                Object newArray = Array.newInstance(type.getComponentType(), index + 1);
                System.arraycopy(arrayOrList, 0, newArray, 0, size);
                Array.set(newArray, index, item);
                arrayReceiver.setValue(newArray);
                return null;
            } else {
                Object old = Array.get(arrayOrList, index);
                Array.set(arrayOrList, index, item);
                return old;
            }
        } else {
            if (growth > 0) {
                if (getLogger().isTraceEnabled()) {
                    getLogger().trace("Increasing list size by " + growth);
                }
                growth--;
                while (growth > 0) {
                    list.add(null);
                    growth--;
                }
                list.add(item);
                return null;
            } else {
                return list.set(index, item);
            }
        }
    }
