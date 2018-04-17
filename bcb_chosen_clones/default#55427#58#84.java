    public static void main(String args[]) {
        boolean proceed = true;
        gjp_coreAPI capi = new gjp_coreAPI();
        ArrayList al = new ArrayList();
        al.add("save");
        al.add("xpath");
        al.add("map");
        Class testClass[] = new Class[1];
        testClass[0] = al.getClass();
        try {
            Object api[] = new Object[1];
            api[0] = al;
            System.out.println(al);
            String methodName = (String) al.get(0);
            System.out.println(methodName);
            Method meth = capi.getClass().getMethod(methodName, testClass);
            Object o = meth.invoke(capi, api);
            proceed = ((Boolean) o).booleanValue();
            System.out.println(proceed);
        } catch (NoSuchMethodException nsme) {
            nsme.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        } catch (InvocationTargetException ite) {
            ite.printStackTrace();
        }
    }
