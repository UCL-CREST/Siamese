    private Object getUnresolved(Object o) {
        switch(fmd.category) {
            case MDStatics.CATEGORY_COLLECTION:
                if (o instanceof Collection) {
                    Collection col = (Collection) o;
                    ArrayList oids = new ArrayList(col.size());
                    if (fmd.isElementTypePC()) {
                        for (Iterator it = col.iterator(); it.hasNext(); ) {
                            VersantDetachable detachable = (VersantDetachable) it.next();
                            oids.add(getPC(detachable));
                        }
                    } else {
                        oids.addAll(col);
                    }
                    col = createNewCol();
                    col.addAll(oids);
                    return col;
                }
                break;
            case MDStatics.CATEGORY_ARRAY:
                if (!o.getClass().isArray()) return o;
                Class type = o.getClass().getComponentType();
                int length = Array.getLength(o);
                Object newArray = Array.newInstance(type, length);
                System.arraycopy(o, 0, newArray, 0, length);
                if (fmd.isElementTypePC()) {
                    Object[] objects = (Object[]) newArray;
                    for (int i = 0; i < objects.length; i++) {
                        objects[i] = getPC((VersantDetachable) objects[i]);
                    }
                }
                return newArray;
            case MDStatics.CATEGORY_MAP:
                if (o instanceof Map) {
                    MapEntries entries = new MapEntries();
                    Map map = (Map) o;
                    int size = map.size();
                    entries.keys = new Object[size];
                    entries.values = new Object[size];
                    int x = 0;
                    for (Iterator it = map.keySet().iterator(); it.hasNext(); ) {
                        Object o1 = (Object) it.next();
                        if (fmd.isKeyTypePC()) {
                            entries.keys[x] = getPC((VersantDetachable) o1);
                        } else {
                            entries.keys[x] = o1;
                        }
                        if (fmd.isElementTypePC()) {
                            entries.values[x] = getPC((VersantDetachable) map.get(o1));
                        } else {
                            entries.values[x] = map.get(o1);
                        }
                        x++;
                    }
                    map = createNewMap();
                    length = entries.keys.length;
                    for (int i = 0; i < length; i++) {
                        Object key = entries.keys[i];
                        Object value = entries.values[i];
                        map.put(key, value);
                    }
                    return map;
                }
                break;
            case MDStatics.CATEGORY_REF:
            case MDStatics.CATEGORY_POLYREF:
                VersantDetachable detachable = (VersantDetachable) o;
                return getPC(detachable);
        }
        return o;
    }
