    public static void main(String args[]) throws Exception {
        Class c = Class.forName("tClass");
        System.out.println(c);
        try {
            Class c_not_found = Class.forName("NotAClassSoThrowAnExceptionPlease");
        } catch (ClassNotFoundException e) {
            System.out.println("caught ClassNotFoundException");
        }
        if (c.isArray()) System.out.println(c + " is an array????"); else System.out.println(c + " is not an array...good");
        Constructor ctors[] = c.getConstructors();
        Arrays.sort(ctors, new Comparator() {

            public int compare(Object x, Object y) {
                return x.toString().compareTo(y.toString());
            }
        });
        System.out.println(c + " has " + ctors.length + " visible constructors");
        for (int i = 0; i < ctors.length; ++i) System.out.println("   " + i + ": " + ctors[i]);
        Constructor declaredCtors[] = c.getDeclaredConstructors();
        Arrays.sort(declaredCtors, new Comparator() {

            public int compare(Object x, Object y) {
                return x.toString().compareTo(y.toString());
            }
        });
        System.out.println(c + " has " + declaredCtors.length + " declared constructors");
        for (int i = 0; i < declaredCtors.length; ++i) System.out.println("   " + i + ": " + declaredCtors[i]);
        Method methods[] = c.getMethods();
        Method hello = null;
        Method iello = null;
        Method lello = null;
        Method jello = null;
        Method vello = null;
        Method declaredMethods[] = c.getDeclaredMethods();
        Arrays.sort(declaredMethods, new Comparator() {

            public int compare(Object x, Object y) {
                return x.toString().compareTo(y.toString());
            }
        });
        System.out.println(c + " has a total number of methods: " + methods.length);
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals("hello")) hello = methods[i];
            if (methods[i].getName().equals("iello")) iello = methods[i];
            if (methods[i].getName().equals("lello")) lello = methods[i];
            if (methods[i].getName().equals("jello")) jello = methods[i];
            if (methods[i].getName().equals("vello")) vello = methods[i];
        }
        System.out.println(" Number of declared methods: " + declaredMethods.length);
        for (int i = 0; i < declaredMethods.length; i++) System.out.println(declaredMethods[i]);
        if (hello == null) {
            System.out.println("tClass.hello not found!");
            System.exit(-1);
        } else {
            System.out.println("================= READY TO CALL: " + hello);
        }
        int n_calls = 3;
        while (n_calls-- > 0) {
            String hello_args[] = { "I Say Hello to You!" };
            String result = (String) hello.invoke(null, hello_args);
            System.out.println(result);
        }
        if (iello == null) {
            System.out.println("tClass.iello not found!");
            System.exit(-1);
        } else {
            System.out.println("================= READY TO CALL: " + iello);
        }
        n_calls = 3;
        while (n_calls-- > 0) {
            Object iello_args[] = { "I Say Iello to You!", new Integer(99) };
            Integer result = (Integer) iello.invoke(null, iello_args);
            System.out.println("Does this>" + result + "< look like 99?");
        }
        if (lello == null) {
            System.out.println("tClass.lello not found!");
            System.exit(-1);
        } else {
            System.out.println("================= READY TO CALL: " + lello);
        }
        n_calls = 3;
        while (n_calls-- > 0) {
            Object lello_args[] = { "I Say Lello to You!", new Long(99) };
            Long result = (Long) lello.invoke(null, lello_args);
            System.out.println("Does this>" + result + "< look like 99?");
        }
        if (jello == null) {
            System.out.println("tClass.jello not found!");
            System.exit(-1);
        } else {
            System.out.println("================= READY TO CALL: " + jello);
        }
        n_calls = 3;
        while (n_calls-- > 0) {
            Object jello_args[] = { new Integer(99), "I Say Jello to You!", new Integer(95), new Integer(94) };
            Integer result = (Integer) jello.invoke(null, jello_args);
            System.out.println("Does this>" + result + "< look like 99?");
        }
        tClass tc = new tClass("Hi!");
        String initargs[] = { "I'm dynamic!" };
        tClass tc_dyn = (tClass) ctors[0].newInstance(initargs);
        if (vello == null) {
            System.out.println("tClass.vello not found!");
            System.exit(-1);
        } else {
            System.out.println("================= READY TO CALL: " + vello);
        }
        n_calls = 3;
        while (n_calls-- > 0) {
            String vello_args[] = { "I Say Vello to You!" };
            String result = (String) vello.invoke(tc_dyn, vello_args);
            System.out.println(result);
        }
    }
