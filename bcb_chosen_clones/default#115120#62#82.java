    @Override
    public void run(int[] array) {
        Method meth;
        try {
            meth = loadedClass.getMethod("run", int[].class);
            System.out.println("invoke method");
            meth.invoke(loadedClass.newInstance(), array);
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
