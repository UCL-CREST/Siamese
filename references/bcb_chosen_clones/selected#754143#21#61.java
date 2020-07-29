    public IAdaptable createAdaptableObject(IAdaptable basisObject) {
        IAdaptable adaptableObject = null;
        adaptableObject = objectMap.get(basisObject);
        if (adaptableObject == null) {
            Constructor constructor = null;
            try {
                constructor = adaptableClass.getConstructor(basisObject.getClass());
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            if (constructor != null) {
                try {
                    adaptableObject = (IAdaptable) constructor.newInstance(basisObject);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    adaptableObject = (IAdaptable) adaptableClass.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (adaptableObject != null) {
                objectMap.put(basisObject, adaptableObject);
            }
        }
        return adaptableObject;
    }
