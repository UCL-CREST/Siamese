    private Algorithm getAlg(DataSearcher[] data, Rater r) throws ClassNotFoundException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Algorithm alg = null;
        Class c = Class.forName(algorithmName);
        Constructor[] a = c.getConstructors();
        Object par[] = new Object[5];
        par[0] = data;
        par[1] = r;
        par[2] = new SimpleColumnFinder();
        par[3] = namespace;
        par[4] = new Integer(count);
        alg = (Algorithm) a[0].newInstance(par);
        return alg;
    }
