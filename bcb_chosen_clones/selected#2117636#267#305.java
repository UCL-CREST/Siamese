    public Object instantiate(String className, ObjectName loaderName, Object[] args, String[] parameters) throws InstanceNotFoundException, ReflectionException, MBeanException {
        if (className == null || className.trim().equals("")) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Class name cannot be null or empty"));
        }
        if (loaderName != null && loaderName.isPattern()) {
            throw new RuntimeOperationsException(new IllegalArgumentException("ObjectName for the ClassLoader cannot be a pattern ObjectName"));
        }
        if (args == null) {
            args = EMPTY_ARGS;
        }
        if (parameters == null) {
            parameters = EMPTY_PARAMS_STRING;
        }
        Class cls = findClass(loaderName, className);
        Class[] params = m_introspector.convertMethodParameters(cls.getClassLoader(), parameters);
        Object mbean = null;
        try {
            Constructor ctor = cls.getConstructor(params);
            mbean = ctor.newInstance(args);
        } catch (NoSuchMethodException x) {
            throw new ReflectionException(x);
        } catch (InstantiationException x) {
            throw new ReflectionException(x);
        } catch (IllegalAccessException x) {
            throw new ReflectionException(x);
        } catch (IllegalArgumentException x) {
            throw new RuntimeOperationsException(x);
        } catch (InvocationTargetException x) {
            Throwable t = x.getTargetException();
            if (t instanceof Error) {
                throw new RuntimeErrorException((Error) t);
            } else if (t instanceof RuntimeException) {
                throw new RuntimeMBeanException((RuntimeException) t);
            } else {
                throw new MBeanException((Exception) t);
            }
        }
        return mbean;
    }
