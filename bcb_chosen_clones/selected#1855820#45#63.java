    public Index getIndex(String interfaceName) throws UnsupportedIndexException, IndexProviderException {
        for (int i = 0; i < INTERFACE_NAMES.length; i++) {
            if (INTERFACE_NAMES[i].equals(interfaceName)) {
                if (indexes[i] == null) {
                    try {
                        Class[] paramTypes = { TopicMapProviderImpl.class, TopicMap.class };
                        Class ixCls = Class.forName(IMPL_NAMES[i]);
                        Constructor ctor = ixCls.getConstructor(paramTypes);
                        Object[] params = { m_provider, m_tm };
                        indexes[i] = (Index) ctor.newInstance(params);
                    } catch (Exception ex) {
                        throw new IndexProviderException("Error creating index: " + interfaceName + " - " + ex.toString());
                    }
                }
                return indexes[i];
            }
        }
        throw new UnsupportedIndexException(interfaceName);
    }
