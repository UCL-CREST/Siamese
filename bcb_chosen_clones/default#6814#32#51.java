    public void run(int[] array) {
        Method meth;
        int needle = 1;
        try {
            meth = loadedClass.getMethod("run", int[].class, int.class);
            meth.invoke(loadedClass.newInstance(), array, needle);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
