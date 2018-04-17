    public static Object invokeConstructor(Class cl, Class[] classes, Object[] params) {
        Object back = null;
        try {
            Constructor constructor = cl.getConstructor(classes);
            back = constructor.newInstance(params);
        } catch (NoSuchMethodException e_nsm) {
            StringBuffer sb = new StringBuffer();
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    sb.append(" - ");
                    sb.append(classes[i]);
                    sb.append(" ");
                    sb.append(params[i]);
                }
            }
            logger.error(cl + " has no constructor with specified params " + sb.toString());
            throw new NullPointerException(e_nsm.toString());
        } catch (SecurityException e_s) {
            logger.error(cl + " constructor rose a security problem");
        } catch (InstantiationException e_i) {
            logger.error("construction of " + cl + " generated an instanciation exception:" + " either an interface or an abstract class");
        } catch (IllegalArgumentException e_ia) {
            logger.error(cl + " has not a good type for  params");
        } catch (IllegalAccessException e_iacc) {
            logger.error(cl + " constructor is not accessible");
        } catch (InvocationTargetException e_it) {
            logger.error(cl + " generated an exception : " + e_it.getTargetException());
            e_it.getTargetException().printStackTrace();
        }
        return back;
    }
