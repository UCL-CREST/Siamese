    public static void main(String[] args) {
        Class k = invokethrow.class;
        try {
            Class[] noargs = new Class[0];
            Method m = k.getMethod("doit", noargs);
            m.invoke(null, null);
        } catch (InvocationTargetException x1) {
            System.out.println(x1.getTargetException().getMessage());
        } catch (UnsupportedOperationException _) {
            System.out.println("hi!");
        } catch (Throwable _) {
        }
    }
