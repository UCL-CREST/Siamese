        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (args == null) args = new Object[0];
            if (method.isAnnotationPresent(Ignore.class)) return null;
            synchronized (lock) {
                if (method.isAnnotationPresent(Filter.class)) {
                    Filter annotation = method.getAnnotation(Filter.class);
                    if (annotation.parameterFilter() != ParameterFilter.class) {
                        ParameterFilter filter = parameterFilterSingletons.get(annotation.parameterFilter());
                        if (filter == null) {
                            filter = annotation.parameterFilter().newInstance();
                            parameterFilterSingletons.put(annotation.parameterFilter(), filter);
                        }
                        args = filter.filter(instance, args);
                    }
                }
                if (method.getName().equalsIgnoreCase("addChangeListener") || method.getName().equalsIgnoreCase("removeChangeListener")) {
                    String property = (String) args[0];
                    PropertyChangeListener listener = (PropertyChangeListener) args[1];
                    HashMap<String, ArrayList<PropertyChangeListener>> beanMap = beanListeners.get(targetId);
                    if (beanMap == null) {
                        beanMap = new HashMap<String, ArrayList<PropertyChangeListener>>();
                        beanListeners.put(targetId, beanMap);
                    }
                    ArrayList listenerList = beanMap.get(property);
                    if (listenerList == null) {
                        listenerList = new ArrayList<PropertyChangeListener>();
                        beanMap.put(property, listenerList);
                    }
                    if (method.getName().equalsIgnoreCase("addChangeListener") && !listenerList.contains(listener)) listenerList.add(listener);
                    if (method.getName().equalsIgnoreCase("removeChangeListener") && listenerList.contains(listener)) listenerList.remove(listener);
                    return null;
                }
                if (method.getName().equalsIgnoreCase("getProxyStorageId") && method.getReturnType() == Long.TYPE) return targetId;
                if (method.getName().equalsIgnoreCase("getProxyStorageClass") && method.getReturnType() == Class.class) return targetClass;
                if (method.getName().equalsIgnoreCase("finalize")) {
                    System.out.println("proxystorage object " + targetId + " finalized.");
                    return null;
                }
                if (method.getName().equalsIgnoreCase("isProxyStoragePresent") && method.getReturnType() == Boolean.TYPE) return isTargetIdPresent(targetId, targetClass);
                if (method.getName().equalsIgnoreCase("equals") && args.length == 1) {
                    Object compare = args[0];
                    if (!(compare instanceof ProxyObject)) return false;
                    ProxyObject object = (ProxyObject) compare;
                    long objectId = object.getProxyStorageId();
                    return objectId == targetId;
                }
                if (method.isAnnotationPresent(Search.class)) {
                    Search annotation = method.getAnnotation(Search.class);
                    String listProperty = annotation.listProperty();
                    String searchProperty = annotation.searchProperty();
                    PreparedStatement lst = connection.prepareStatement("select " + listProperty + " from " + getTargetTableName(targetClass) + " where proxystorage_id = ?");
                    opcount++;
                    lst.setLong(1, targetId);
                    ResultSet lrs = lst.executeQuery();
                    if (!lrs.next()) throw new RuntimeException("mismatched object with id " + targetId + " and class " + targetClass.getName());
                    long listId = lrs.getLong(1);
                    if (lrs.wasNull()) {
                        lrs.close();
                        lst.close();
                        if (method.getReturnType().isArray()) return Array.newInstance(method.getReturnType().getComponentType(), 0); else return null;
                    }
                    lrs.close();
                    lst.close();
                    String capitalizedListProperty = listProperty.substring(0, 1).toUpperCase() + listProperty.substring(1);
                    String capitalizedSearchProperty = searchProperty.substring(0, 1).toUpperCase() + searchProperty.substring(1);
                    Method listGetterMethod = method.getDeclaringClass().getMethod("get" + capitalizedListProperty, new Class[0]);
                    ListType listTypeAnnotation = listGetterMethod.getAnnotation(ListType.class);
                    if (listTypeAnnotation == null) throw new RuntimeException("@ListType annotation is not present on stored list getter " + listGetterMethod.getName() + " for class " + listGetterMethod.getDeclaringClass().getName());
                    Class listType = listTypeAnnotation.value();
                    Method searchGetterMethod;
                    try {
                        searchGetterMethod = listType.getMethod("get" + capitalizedSearchProperty, new Class[0]);
                    } catch (NoSuchMethodException ex) {
                        searchGetterMethod = listType.getMethod("is" + capitalizedSearchProperty, new Class[0]);
                    }
                    PreparedStatement st = connection.prepareStatement("select value from proxystorage_collections " + "where id = ? and value in (select proxystorage_id from " + getTargetTableName(listType) + " where " + searchProperty + " " + (annotation.exact() ? "=" : "like") + " ?) order by index asc");
                    st.setLong(1, listId);
                    Object searchValue = args[0];
                    if (!annotation.exact()) {
                        searchValue = (annotation.anywhere() ? "%" : "") + ((String) searchValue).replace("*", "%") + (annotation.anywhere() ? "%" : "");
                    }
                    st.setObject(2, searchValue);
                    opcount++;
                    ResultSet rs = st.executeQuery();
                    ArrayList<Long> resultIds = new ArrayList<Long>();
                    while (rs.next()) {
                        resultIds.add(rs.getLong(1));
                    }
                    rs.close();
                    st.close();
                    Object[] results = new Object[resultIds.size()];
                    int index = 0;
                    for (long resultId : resultIds) {
                        results[index++] = getById(resultId, listType);
                    }
                    if (method.getReturnType().isArray()) {
                        Object resultArray = Array.newInstance(method.getReturnType().getComponentType(), results.length);
                        System.arraycopy(results, 0, resultArray, 0, results.length);
                        return resultArray;
                    } else {
                        if (results.length == 0) return null; else return results[0];
                    }
                }
                if (method.isAnnotationPresent(CompoundSearch.class)) {
                    CompoundSearch annotation = method.getAnnotation(CompoundSearch.class);
                    String listProperty = annotation.listProperty();
                    String[] searchProperties = annotation.searchProperties();
                    PreparedStatement lst = connection.prepareStatement("select " + listProperty + " from " + getTargetTableName(targetClass) + " where proxystorage_id = ?");
                    lst.setLong(1, targetId);
                    opcount++;
                    ResultSet lrs = lst.executeQuery();
                    if (!lrs.next()) throw new RuntimeException("mismatched object with id " + targetId + " and class " + targetClass.getName());
                    long listId = lrs.getLong(1);
                    if (lrs.wasNull()) {
                        lrs.close();
                        lst.close();
                        if (method.getReturnType().isArray()) return Array.newInstance(method.getReturnType().getComponentType(), 0); else return null;
                    }
                    lrs.close();
                    lst.close();
                    String capitalizedListProperty = listProperty.substring(0, 1).toUpperCase() + listProperty.substring(1);
                    Method listGetterMethod = method.getDeclaringClass().getMethod("get" + capitalizedListProperty, new Class[0]);
                    ListType listTypeAnnotation = listGetterMethod.getAnnotation(ListType.class);
                    if (listTypeAnnotation == null) throw new RuntimeException("@ListType annotation is not present on stored list getter " + listGetterMethod.getName() + " for class " + listGetterMethod.getDeclaringClass().getName());
                    Class listType = listTypeAnnotation.value();
                    String[] searchQueryStrings = new String[searchProperties.length];
                    Method[] searchQueryMethods = new Method[searchProperties.length];
                    for (int i = 0; i < searchProperties.length; i++) {
                        String capitalizedSearchProperty = searchProperties[i].substring(0, 1).toUpperCase() + searchProperties[i].substring(1);
                        Method searchGetterMethod;
                        try {
                            searchGetterMethod = listType.getMethod("get" + capitalizedSearchProperty, new Class[0]);
                        } catch (NoSuchMethodException ex) {
                            searchGetterMethod = listType.getMethod("is" + capitalizedSearchProperty, new Class[0]);
                        }
                        searchQueryMethods[i] = searchGetterMethod;
                        searchQueryStrings[i] = searchProperties[i] + " " + (annotation.exact()[i] ? "=" : "like") + " ?";
                    }
                    String searchQuery = StringUtils.delimited(searchQueryStrings, " and ");
                    String searchSql = "select value from proxystorage_collections " + "where id = ? and value in (select proxystorage_id from " + getTargetTableName(listType) + " where " + searchQuery + ") order by index asc";
                    PreparedStatement st = connection.prepareStatement(searchSql);
                    opcount++;
                    st.setLong(1, listId);
                    for (int i = 0; i < searchProperties.length; i++) {
                        Object searchValue = args[i];
                        if (!annotation.exact()[i]) {
                            searchValue = (annotation.anywhere()[i] ? "%" : "") + ((String) searchValue).replace("*", "%") + (annotation.anywhere()[i] ? "%" : "");
                        }
                        st.setObject(i + 2, searchValue);
                    }
                    ResultSet rs = st.executeQuery();
                    ArrayList<Long> resultIds = new ArrayList<Long>();
                    while (rs.next()) {
                        resultIds.add(rs.getLong(1));
                    }
                    rs.close();
                    st.close();
                    Object[] results = new Object[resultIds.size()];
                    int index = 0;
                    for (long resultId : resultIds) {
                        results[index++] = getById(resultId, listType);
                    }
                    if (method.getReturnType().isArray()) {
                        Object resultArray = Array.newInstance(method.getReturnType().getComponentType(), results.length);
                        System.arraycopy(results, 0, resultArray, 0, results.length);
                        return resultArray;
                    } else {
                        if (results.length == 0) return null; else return results[0];
                    }
                }
                if (method.getName().equalsIgnoreCase("hashCode") && args.length == 0) {
                    return (int) targetId * 31;
                }
                if (method.isAnnotationPresent(Constructor.class)) {
                    return ProxyStorage.this.create(method.getReturnType());
                }
                if (method.isAnnotationPresent(CustomProperty.class)) {
                    CustomProperty annotation = method.getAnnotation(CustomProperty.class);
                    Class<? extends Delegate> delegateClass = annotation.value();
                    Delegate delegate = delegateSingletons.get(delegateClass);
                    if (delegate == null) {
                        delegate = delegateClass.newInstance();
                        delegateSingletons.put(delegateClass, delegate);
                    }
                    return delegate.get(instance, method.getReturnType(), propertyNameFromAccessor(method.getName()));
                }
                if (method.getName().equals("toString")) {
                    return "ProxyStorage-id" + targetId;
                }
                if (isPropertyMethod(method)) {
                    if (method.getName().startsWith("get") || method.getName().startsWith("is")) {
                        String propertyName = propertyNameFromAccessor(method.getName());
                        BeanPropertyKey cacheKey = new BeanPropertyKey();
                        cacheKey.id = targetId;
                        cacheKey.property = propertyName;
                        Map cacheMap;
                        if (method.getReturnType().equals(String.class)) cacheMap = stringCache; else cacheMap = propertyCache;
                        Object cachedObject = cacheMap.get(cacheKey);
                        Object result;
                        if (cachedObject != null) result = cachedObject; else {
                            PreparedStatement st = connection.prepareStatement("select " + propertyName + " from " + getTargetTableName(targetClass) + " where proxystorage_id = ?");
                            opcount++;
                            st.setLong(1, targetId);
                            ResultSet rs = st.executeQuery();
                            boolean isPresent = rs.next();
                            if (!isPresent) {
                                rs.close();
                                st.close();
                                throw new IllegalStateException("The object that was queried has been deleted " + "from the database.");
                            }
                            result = rs.getObject(propertyName);
                            if (result != null) cacheMap.put(cacheKey, result);
                            rs.close();
                            st.close();
                        }
                        if (method.getReturnType() == Integer.TYPE || method.getReturnType() == Integer.class || method.getReturnType() == Long.TYPE || method.getReturnType() == Long.class || method.getReturnType() == Double.TYPE || method.getReturnType() == Double.class || method.getReturnType() == Boolean.TYPE || method.getReturnType() == Boolean.class || method.getReturnType() == String.class) {
                            if (result == null) {
                                if (method.isAnnotationPresent(Default.class)) {
                                    Default values = method.getAnnotation(Default.class);
                                    if (method.getReturnType() == Integer.TYPE) result = values.intValue();
                                    if (method.getReturnType() == Long.TYPE) result = values.longValue();
                                    if (method.getReturnType() == Double.TYPE) result = values.doubleValue();
                                    if (method.getReturnType() == Boolean.TYPE) result = values.booleanValue();
                                    if (method.getReturnType() == String.class) result = values.stringValue();
                                } else {
                                    if (method.getReturnType() == Integer.TYPE) result = (int) 0;
                                    if (method.getReturnType() == Long.TYPE) result = (long) 0;
                                    if (method.getReturnType() == Double.TYPE) result = (double) 0;
                                    if (method.getReturnType() == Boolean.TYPE) result = false;
                                }
                            }
                            return result;
                        }
                        if (method.getReturnType() == BigInteger.class) {
                            if (result == null) return null;
                            return new BigInteger(((String) result), 16);
                        }
                        if (method.getReturnType() == StoredList.class) {
                            if (result == null) {
                                result = new Long(nextId());
                                PreparedStatement ist = connection.prepareStatement("update " + getTargetTableName(targetClass) + " set " + propertyName + " = ? where proxystorage_id = ?");
                                opcount++;
                                ist.setLong(1, (Long) result);
                                ist.setLong(2, targetId);
                                ist.execute();
                                ist.close();
                            }
                            return new StoredList(ProxyStorage.this, ((ListType) method.getAnnotation(ListType.class)).value(), (Long) result);
                        }
                        if (method.getReturnType().isAnnotationPresent(ProxyBean.class)) {
                            boolean isRequired = method.isAnnotationPresent(Required.class);
                            if (result == null) {
                                if (!isRequired) return null;
                                ProxyObject newObject = (ProxyObject) create(method.getReturnType());
                                long newId = newObject.getProxyStorageId();
                                PreparedStatement ust = connection.prepareStatement("update " + getTargetTableName(targetClass) + " set " + propertyName + " = ? where proxystorage_id = ?");
                                opcount++;
                                ust.setLong(1, newId);
                                ust.setLong(2, targetId);
                                ust.executeUpdate();
                                ust.close();
                                result = newId;
                            }
                            return getById((Long) result, method.getReturnType());
                        }
                        throw new IllegalArgumentException("The method is a getter, but it's return " + "type is not a proper type.");
                    } else {
                        String propertyName = propertyNameFromAccessor(method.getName());
                        PreparedStatement st = connection.prepareStatement("update " + getTargetTableName(targetClass) + " set " + propertyName + " = ? where proxystorage_id = ?");
                        st.setLong(2, targetId);
                        Object inputObject = args[0];
                        if (inputObject != null) {
                            if (inputObject.getClass() == StoredList.class) {
                                throw new IllegalArgumentException("Setters for stored lists are not allowed.");
                            }
                            if (inputObject.getClass() == BigInteger.class) {
                                inputObject = ((BigInteger) inputObject).toString(16);
                            }
                            if (inputObject instanceof ProxyObject) {
                                inputObject = new Long(((ProxyObject) inputObject).getProxyStorageId());
                            }
                        }
                        st.setObject(1, inputObject);
                        opcount++;
                        st.execute();
                        st.close();
                        BeanPropertyKey key = new BeanPropertyKey();
                        key.id = targetId;
                        key.property = propertyName;
                        if (inputObject == null) {
                            stringCache.remove(key);
                            propertyCache.remove(key);
                        } else {
                            if (inputObject instanceof String) stringCache.put(key, inputObject); else propertyCache.put(key, inputObject);
                        }
                        HashMap<String, ArrayList<PropertyChangeListener>> beanMap = beanListeners.get(targetId);
                        if (beanMap != null) {
                            ArrayList<PropertyChangeListener> listenerList = beanMap.get(propertyName);
                            if (listenerList != null) {
                                PropertyChangeEvent event = new PropertyChangeEvent(instance, propertyName, null, null);
                                for (PropertyChangeListener listener : listenerList) {
                                    listenerExecutor.execute(new PropertyChanged(listener, event));
                                }
                            }
                        }
                        return null;
                    }
                }
                throw new UnsupportedOperationException("The method " + method.getName() + " is not supported for the proxy type " + targetClass.getName());
            }
        }
