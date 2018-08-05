    protected <T> List<T> getMultipleStringProp(Class<T> clazz, String propKey) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String lst = getString(propKey);
        if (lst == null) {
            return null;
        }
        Constructor<T> constr = clazz.getConstructor(new Class<?>[] { String.class });
        StringTokenizer tok = new StringTokenizer(lst, File.pathSeparator);
        List<T> ret = new ArrayList<T>(tok.countTokens());
        while (tok.hasMoreTokens()) {
            ret.add(constr.newInstance(new Object[] { tok.nextToken() }));
        }
        return ret;
    }
