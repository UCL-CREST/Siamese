    public static void main(String[] cmdline) {
        try {
            Class[] argTypes = new Class[100];
            Class objectClass = Class.forName("java.lang.Object");
            for (int i = 0; i < 100; i++) {
                argTypes[i] = objectClass;
            }
            Method m = Class.forName("StackWaste").getMethod("dummy", argTypes);
            Object[] args = new Object[100];
            for (int i = 0; i < 10000; i++) {
                m.invoke(null, args);
            }
            System.out.println("Passed.");
        } catch (ClassNotFoundException e) {
            System.out.println("CLASS NOT FOUND");
        } catch (NoSuchMethodException e) {
            System.out.println("NO SUCH METHOD");
        } catch (IllegalAccessException e) {
            System.out.println("ILLEGAL ACCESS");
        } catch (InvocationTargetException e) {
            System.out.println("INVOCATION TARGET EXCEPTION");
        }
    }
