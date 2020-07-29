    public static void main(final String[] args) throws Exception {
        System.out.println("Running");
        BeanGenerator cg = new BeanGenerator();
        PrintWriter pw = new PrintWriter(System.out, true);
        byte[] b = cg.generate(pw);
        Class c = cg.defineClass("pkg.Bean", b, 0, b.length);
        Object bean = c.newInstance();
        final Class c2[] = {};
        final Method getF = c.getMethod("getF", c2);
        Class[] classes1 = { int.class };
        Method setF = c.getMethod("setF", classes1);
        Object[] args1 = { 999 };
        setF.invoke(bean, args1);
        final int val = ((Integer) getF.invoke(bean)).intValue();
        System.out.println("Running =>" + val);
    }
