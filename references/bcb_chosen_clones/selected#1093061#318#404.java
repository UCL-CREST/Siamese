    public Object getObjectField(VersantDetachable pc, int fieldNo) {
        State state = getState(pc);
        ClassMetaData cmd = state.getClassMetaData();
        FieldMetaData fmd = cmd.stateFields[cmd.absToRel[fieldNo]];
        int category = fmd.category;
        Object o = state.getInternalObjectFieldAbs(fieldNo);
        switch(category) {
            case MDStatics.CATEGORY_COLLECTION:
                if (o != null) {
                    VersantSCOCollectionFactory factory = scoFactoryRegistry.getJDOGenieSCOCollectionFactory(fmd);
                    if (o instanceof VersantSCOCollection) {
                        CollectionData collectionData = new CollectionData();
                        ((VersantSCOCollection) o).fillCollectionData(collectionData);
                        collectionData.valueCount = getDetachCopy(collectionData.values, collectionData.valueCount);
                        return factory.createSCOCollection(pc, pm, detachedStateManager, fmd, collectionData);
                    } else {
                        CollectionData collectionData = new CollectionData();
                        Object[] values = (Object[]) o;
                        int length = values.length;
                        collectionData.values = new Object[length];
                        System.arraycopy(values, 0, collectionData.values, 0, length);
                        collectionData.valueCount = collectionData.values.length;
                        collectionData.valueCount = getDetachCopy(collectionData.values, collectionData.valueCount);
                        return factory.createSCOCollection(pc, pm, detachedStateManager, fmd, collectionData);
                    }
                }
                break;
            case MDStatics.CATEGORY_ARRAY:
                if (o != null) {
                    if (!o.getClass().isArray()) return o;
                    Class type = o.getClass().getComponentType();
                    int length = Array.getLength(o);
                    Object newArray = Array.newInstance(type, length);
                    System.arraycopy(o, 0, newArray, 0, length);
                    if (fmd.isElementTypePC()) {
                        getDetachCopy((Object[]) newArray, length);
                    }
                    return newArray;
                }
                break;
            case MDStatics.CATEGORY_MAP:
                if (o != null) {
                    VersantSCOMapFactory factory = scoFactoryRegistry.getJDOGenieSCOMapFactory(fmd);
                    if (o instanceof VersantSCOMap) {
                        MapData mapData = new MapData();
                        ((VersantSCOMap) o).fillMapData(mapData);
                        mapData.entryCount = getDetachCopy(mapData.keys, mapData.entryCount);
                        mapData.entryCount = getDetachCopy(mapData.values, mapData.entryCount);
                        return factory.createSCOHashMap(pc, pm, detachedStateManager, fmd, mapData);
                    } else {
                        MapEntries entries = (MapEntries) o;
                        MapData mapData = new MapData();
                        mapData.entryCount = entries.keys.length;
                        Object[] keys = new Object[mapData.entryCount];
                        System.arraycopy(entries.keys, 0, keys, 0, mapData.entryCount);
                        Object[] values = new Object[mapData.entryCount];
                        System.arraycopy(entries.values, 0, values, 0, mapData.entryCount);
                        mapData.keys = keys;
                        mapData.values = values;
                        mapData.entryCount = getDetachCopy(mapData.keys, mapData.entryCount);
                        mapData.entryCount = getDetachCopy(mapData.values, mapData.entryCount);
                        return factory.createSCOHashMap(pc, pm, detachedStateManager, fmd, mapData);
                    }
                }
                break;
            case MDStatics.CATEGORY_EXTERNALIZED:
                if (o != null) {
                    if (state.isResolvedForClient(fieldNo)) {
                        o = fmd.externalizer.toExternalForm(pm, o);
                    }
                    o = fmd.externalizer.fromExternalForm(pm, o);
                }
                return o;
            case MDStatics.CATEGORY_REF:
            case MDStatics.CATEGORY_POLYREF:
                if (o != null) {
                    if (fmd.scoField) {
                        VersantSCOFactory factory = scoFactoryRegistry.getJdoGenieSCOFactory(fmd);
                        return factory.createSCO(pc, pm, detachedStateManager, fmd, o);
                    } else {
                        return getDetachCopy(o);
                    }
                }
                break;
        }
        return o;
    }
