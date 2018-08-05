    static void testConstructorNewInstance() {
        try {
            Class c = Class.forName("LocalClass");
            Constructor cons = c.getConstructor(new Class[0]);
            System.err.println("Cons LocalClass succeeded unexpectedly");
        } catch (NoSuchMethodException nsme) {
            System.out.println("Cons LocalClass failed as expected");
        } catch (Exception ex) {
            System.err.println("Cons LocalClass failed strangely");
            ex.printStackTrace();
        }
        try {
            Class c = Class.forName("LocalClass2");
            Constructor cons = c.getConstructor((Class[]) null);
            Object obj = cons.newInstance();
            System.out.println("Cons LocalClass2 succeeded");
        } catch (Exception ex) {
            System.err.println("Cons LocalClass2 failed");
            ex.printStackTrace();
        }
        try {
            Class c = Class.forName("otherpackage.PackageAccess");
            Constructor cons = c.getConstructor(new Class[0]);
            System.err.println("ERROR: Cons PackageAccess succeeded unexpectedly");
        } catch (NoSuchMethodException nsme) {
            System.out.println("Cons got expected PackageAccess complaint");
        } catch (Exception ex) {
            System.err.println("Cons got unexpected PackageAccess failure");
            ex.printStackTrace();
        }
        try {
            Class c = Class.forName("MaybeAbstract");
            Constructor cons = c.getConstructor(new Class[0]);
            Object obj = cons.newInstance();
            System.err.println("ERROR: Cons MaybeAbstract succeeded unexpectedly");
        } catch (InstantiationException ie) {
            System.out.println("Cons got expected InstantationException");
        } catch (Exception ex) {
            System.err.println("Cons got unexpected MaybeAbstract failure");
            ex.printStackTrace();
        }
    }
