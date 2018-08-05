    @SuppressWarnings("unchecked")
    public static MultipleInheritanceProxy create(ClassLoader loader, boolean assignDefaultDelegates, Class<?>... classes) {
        LinkedList<Class> temp = new LinkedList<Class>();
        Map<Class, Class> classMap = null;
        if (assignDefaultDelegates) {
            classMap = new HashMap<Class, Class>();
        }
        for (Class c : classes) {
            if (!c.isInterface()) {
                for (Class i : c.getInterfaces()) {
                    if (assignDefaultDelegates) {
                        if (!classMap.containsKey(i)) {
                            try {
                                Constructor emptyConstructor = c.getConstructor(emptyClassArr);
                                classMap.put(i, c);
                            } catch (NoSuchMethodException ex) {
                            }
                        }
                    }
                    temp.add(i);
                }
            } else temp.add(c);
        }
        Class<?>[] interfaces = new Class<?>[temp.size()];
        for (int i = 0; i < temp.size(); i++) interfaces[i] = temp.get(i);
        MultipleInheritanceProxy out = createFromInterfaces(ClassLoader.getSystemClassLoader(), interfaces);
        if (assignDefaultDelegates) {
            Object[] emptyObjectArr = new Object[0];
            for (Class i : interfaces) {
                Class con = classMap.get(i);
                if (con != null) {
                    try {
                        Constructor emptyConstructor = con.getConstructor(emptyClassArr);
                        Object o = emptyConstructor.newInstance(emptyObjectArr);
                        out.setDelegate(i, o);
                    } catch (Exception ex) {
                    }
                }
            }
        }
        return out;
    }
