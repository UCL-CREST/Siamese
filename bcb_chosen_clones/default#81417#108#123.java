    public Object InvokeMethod(Object target, String methodname, Object[] args) {
        Method[] methods = target.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals(methodname)) {
                Object res = null;
                try {
                    res = methods[i].invoke(target, args);
                } catch (Exception exc) {
                    System.out.println("exception: " + exc + "\r\n");
                }
                ;
                return res;
            }
        }
        return null;
    }
