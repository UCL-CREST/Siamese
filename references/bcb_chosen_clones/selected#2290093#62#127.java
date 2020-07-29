    public Object getLazyResolvableProperty(String key, Class<?> type, Class<?>[] itemTypes) {
        if (isLazy()) {
            Object v = delegate.get(key);
            if (v == null) {
                return null;
            }
            if (v instanceof Node) {
                Node n = (Node) v;
                synchronized (n) {
                    v = delegate.get(key);
                    if (v instanceof Node) {
                        try {
                            if (List.class.isAssignableFrom(type)) {
                                Map<String, Object> jaxbConfig = new HashMap<String, Object>();
                                if (itemTypes == null) {
                                    itemTypes = listStringItemTypes;
                                }
                                jaxbConfig.put(JAXBRIContext.ANNOTATION_READER, new JaxbDirectListAnnotationReader(itemTypes[0]));
                                JAXBContext jaxbContextTmp = JAXBContext.newInstance(new Class[] { DirectListJaxbWrapper.class }, jaxbConfig);
                                v = jaxbContextTmp.createUnmarshaller().unmarshal(n, DirectListJaxbWrapper.class).getValue().list;
                            } else if (Map.class.isAssignableFrom(type)) {
                                if (itemTypes == null) {
                                    itemTypes = mapStringItemTypes;
                                }
                                Map<String, Object> jaxbConfig = new HashMap<String, Object>();
                                jaxbConfig.put(JAXBRIContext.ANNOTATION_READER, new JaxbDirectMapAnnotationReader(itemTypes[0], itemTypes[1]));
                                JAXBContext jaxbContextTmp = JAXBContext.newInstance(new Class[] { DirectMapJaxbWrapper.class }, jaxbConfig);
                                DirectMapJaxbWrapper dm = jaxbContextTmp.createUnmarshaller().unmarshal(n, DirectMapJaxbWrapper.class).getValue();
                                if (dm != null) {
                                    v = dm.getMap(type);
                                } else {
                                    v = null;
                                }
                            } else if (type.isArray()) {
                                if (itemTypes == null) {
                                    itemTypes = listStringItemTypes;
                                }
                                Map<String, Object> jaxbConfig = new HashMap<String, Object>();
                                jaxbConfig.put(JAXBRIContext.ANNOTATION_READER, new JaxbDirectListAnnotationReader(itemTypes[0]));
                                JAXBContext jaxbContextTmp = JAXBContext.newInstance(new Class[] { DirectListJaxbWrapper.class }, jaxbConfig);
                                DirectListJaxbWrapper dl = jaxbContextTmp.createUnmarshaller().unmarshal(n, DirectListJaxbWrapper.class).getValue();
                                if (dl != null && dl.list != null) {
                                    v = Array.newInstance(type.getComponentType(), dl.list.size());
                                    System.arraycopy(dl.list.toArray(), 0, v, 0, dl.list.size());
                                } else {
                                    v = null;
                                }
                            } else {
                                v = jaxbContext.createUnmarshaller().unmarshal(n, type).getValue();
                            }
                        } catch (JAXBException e) {
                            throw new IllegalArgumentException("can not parse config property : " + key + " whose type is" + type, e);
                        }
                        delegate.put(key, v);
                        return v;
                    } else {
                        return v;
                    }
                }
            } else {
                return v;
            }
        } else {
            return delegate.get(key);
        }
    }
