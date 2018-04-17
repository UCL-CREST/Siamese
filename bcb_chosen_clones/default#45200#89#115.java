    public static final void main(final String[] args) throws Exception {
        System.out.println("Running");
        Map<String, Type> fields = new HashMap<String, Type>();
        fields.put("f0", Type.BOOLEAN_TYPE);
        fields.put("f1", Type.INT_TYPE);
        fields.put("f2", Type.LONG_TYPE);
        fields.put("f3", Type.FLOAT_TYPE);
        fields.put("f4", Type.DOUBLE_TYPE);
        fields.put("f5", Type.getType("Ljava/lang/String;"));
        fields.put("f6", Type.getType("[I"));
        BeanGenerator3 cg = new BeanGenerator3("MyBean", fields);
        PrintWriter pw = new PrintWriter(System.out, true);
        ClassWriter cw = new ClassWriter(0);
        CheckClassAdapter ca = new CheckClassAdapter(cw);
        cg.generate(ca);
        final byte[] b = cw.toByteArray();
        Class c = cg.defineClass("MyBean", b, 0, b.length);
        Object bean = c.newInstance();
        final Class[] c2 = {};
        final java.lang.reflect.Method getF = c.getMethod("getF1", c2);
        Class[] classes1 = { int.class };
        final java.lang.reflect.Method setF = c.getMethod("setF1", classes1);
        Object[] args1 = { 999 };
        setF.invoke(bean, args1);
        final int val = ((Integer) getF.invoke(bean)).intValue();
        System.out.println("Running =>" + val);
    }
