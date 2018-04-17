    public static synchronized boolean setValue(Repository repository, Persistable persistable, String[] atributos, Object valor, int j) {
        boolean success = false;
        PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(persistable);
        for (int i = 0; i < descriptors.length; i++) {
            if (descriptors[i].getName().equals(atributos[j])) {
                success = true;
                try {
                    Object o = descriptors[i].getReadMethod().invoke(persistable);
                    if (o == null) {
                        o = descriptors[i].getReadMethod().getReturnType().getConstructor().newInstance();
                        descriptors[i].getWriteMethod().invoke(persistable, o);
                        repository.save(persistable);
                    }
                    if (j < atributos.length - 1) {
                        if (o instanceof Persistable) {
                            success = setValue(repository, (Persistable) o, atributos, valor, ++j);
                        }
                    } else {
                        if (o.equals(valor)) {
                            success = false;
                            break;
                        }
                        success = true;
                        descriptors[i].getWriteMethod().invoke(persistable, valor);
                        repository.save(persistable);
                    }
                    break;
                } catch (IllegalArgumentException e) {
                    success = false;
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    success = false;
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    success = false;
                    e.printStackTrace();
                } catch (SecurityException e) {
                    success = false;
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    success = false;
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    success = false;
                    e.printStackTrace();
                }
            }
        }
        return success;
    }
