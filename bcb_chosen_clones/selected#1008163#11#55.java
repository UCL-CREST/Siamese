    public static boolean setValue(Persistable invoker, Persistable persistable, String atributo, Object valor, Repository repository) {
        boolean found = false;
        PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(persistable);
        for (PropertyDescriptor propertyDescriptor : descriptors) {
            if (propertyDescriptor.getName().equals(atributo)) {
                try {
                    propertyDescriptor.getWriteMethod().invoke(persistable, valor);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                found = true;
                repository.save(persistable);
                break;
            }
            try {
                Object o = propertyDescriptor.getReadMethod().invoke(persistable);
                if (o == null) {
                    o = persistable.getClass().getConstructor().newInstance();
                }
                if (o instanceof Persistable && (invoker == null || !invoker.equals(o))) {
                    if (setValue(persistable, (Persistable) o, atributo, valor, repository)) {
                        repository.save(persistable);
                        break;
                    }
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return found;
    }
