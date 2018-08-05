    public Index getIndex(String interfaceName) throws UnsupportedIndexException, IndexProviderException {
        m_log.debug("Looking for: " + interfaceName);
        for (int i = 0; i < INTERFACE_NAMES.length; i++) {
            m_log.debug("  Have: " + INTERFACE_NAMES[i]);
            if (INTERFACE_NAMES[i].equals(interfaceName)) {
                if (indexes[i] == null) {
                    try {
                        Class ixCls = Class.forName(IMPL_NAMES[i]);
                        m_log.debug("Implementation class is: " + ixCls.getName());
                        if (OzoneObject.class.isAssignableFrom(ixCls)) {
                            m_log.debug("Create index as OzoneObject...");
                            indexes[i] = (OzoneIndex) database().createObject(ixCls.getName());
                            ((OzoneIndex) indexes[i]).initialise(m_tm);
                        } else {
                            m_log.debug("Create index as standard object.");
                            Class[] paramTypes = { TopicMap.class };
                            Constructor ctor = ixCls.getConstructor(paramTypes);
                            Object[] params = { m_tm };
                            indexes[i] = (Index) ctor.newInstance(params);
                        }
                    } catch (Exception ex) {
                        throw new IndexProviderException("Error creating index: " + interfaceName + " - " + ex.toString());
                    }
                }
                return indexes[i];
            }
        }
        throw new UnsupportedIndexException(interfaceName);
    }
