    private Class<?> findClassDalvik(String name) throws ClassNotFoundException {
        if (mDexFile == null) {
            synchronized (FancyLoader.class) {
                Constructor ctor;
                try {
                    ctor = mDexClass.getConstructor(new Class[] { String.class });
                } catch (NoSuchMethodException nsme) {
                    throw new ClassNotFoundException("getConstructor failed", nsme);
                }
                try {
                    mDexFile = ctor.newInstance(DEX_FILE);
                } catch (InstantiationException ie) {
                    throw new ClassNotFoundException("newInstance failed", ie);
                } catch (IllegalAccessException iae) {
                    throw new ClassNotFoundException("newInstance failed", iae);
                } catch (InvocationTargetException ite) {
                    throw new ClassNotFoundException("newInstance failed", ite);
                }
            }
        }
        Method meth;
        try {
            meth = mDexClass.getMethod("loadClass", new Class[] { String.class, ClassLoader.class });
        } catch (NoSuchMethodException nsme) {
            throw new ClassNotFoundException("getMethod failed", nsme);
        }
        try {
            meth.invoke(mDexFile, name, this);
        } catch (IllegalAccessException iae) {
            throw new ClassNotFoundException("loadClass failed", iae);
        } catch (InvocationTargetException ite) {
            throw new ClassNotFoundException("loadClass failed", ite.getCause());
        }
        return null;
    }
