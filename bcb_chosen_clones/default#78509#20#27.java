    void callVoidVoid() {
        try {
            final Method method = ob.getClass().getMethod(foo);
            method.invoke(ob);
        } catch (Exception e) {
            System.out.println("AzFunctionPointer_Exception: " + e);
        }
    }
