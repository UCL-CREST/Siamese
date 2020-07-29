    public static Thread createModuleThread(ClassLoader cl, Class memoryLeakerClass, Class cleanerClass) {
        Thread t;
        try {
            Class cls = cl.loadClass(TestModuleThread.class.getName());
            Constructor constr = cls.getConstructor(new Class[] { Class.class, Class.class });
            t = (Thread) constr.newInstance(new Object[] { memoryLeakerClass, cleanerClass });
            t.setContextClassLoader(cl);
        } catch (ClassNotFoundException e) {
            throw new PermGenCleanerException(e);
        } catch (InstantiationException e) {
            throw new PermGenCleanerException(e);
        } catch (IllegalAccessException e) {
            throw new PermGenCleanerException(e);
        } catch (SecurityException e) {
            throw new PermGenCleanerException(e);
        } catch (IllegalArgumentException e) {
            throw new PermGenCleanerException(e);
        } catch (NoSuchMethodException e) {
            throw new PermGenCleanerException(e);
        } catch (InvocationTargetException e) {
            throw new PermGenCleanerException(e);
        }
        return t;
    }
