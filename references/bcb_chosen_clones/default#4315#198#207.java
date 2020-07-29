    public static void main(String[] args) throws Throwable {
        for (Field f : README.class.getDeclaredFields()) {
            final String name = f.getName();
            Class<?> c = (Class<?>) f.get(null);
            Method m = c.getDeclaredMethod("main", new Class<?>[] { String[].class });
            System.out.println("[begin : " + name + " ]");
            m.invoke(null, new Object[] { null });
            System.out.println("[end   : " + name + " ]\n");
        }
    }
