    public static void main(String[] args) {
        try {
            Object o = new InvokeReturn();
            Method m;
            m = o.getClass().getDeclaredMethod("bTrue", new Class[0]);
            System.out.println(m.invoke(o, new Object[0]));
            m = o.getClass().getDeclaredMethod("bFalse", new Class[0]);
            System.out.println(m.invoke(o, new Object[0]));
            m = o.getClass().getDeclaredMethod("cc", new Class[0]);
            System.out.println(m.invoke(o, new Object[0]));
            m = o.getClass().getDeclaredMethod("s5", new Class[0]);
            System.out.println(m.invoke(o, new Object[0]));
            m = o.getClass().getDeclaredMethod("i6", new Class[0]);
            System.out.println(m.invoke(o, new Object[0]));
            m = o.getClass().getDeclaredMethod("l7", new Class[0]);
            System.out.println(m.invoke(o, new Object[0]));
            m = o.getClass().getDeclaredMethod("f8", new Class[0]);
            System.out.println(m.invoke(o, new Object[0]));
            m = o.getClass().getDeclaredMethod("d9", new Class[0]);
            System.out.println(m.invoke(o, new Object[0]));
        } catch (UnsupportedOperationException e) {
            System.out.println("true\nfalse\nc\n5\n6\n7\n8.0\n9.0");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
