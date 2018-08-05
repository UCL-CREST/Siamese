    static void testManyFieldsAndMethods() {
        ManyFieldsAndMethods many = new ManyFieldsAndMethods();
        Class c = many.getClass();
        int i;
        try {
            for (i = 0; i < 4 * 256; i++) {
                if (i % 25 == 0) {
                    System.out.print(".");
                }
                if (i == 100) i += 100;
                if (i == 300) i += 200;
                if (i == 600) i += 400;
                java.lang.reflect.Method m = c.getMethod("method" + i, null);
                Integer result = (Integer) m.invoke(many, null);
                if (!assert0(result.intValue() == i, "testManyFieldsAndMethods: method" + i)) {
                    return;
                }
            }
        } catch (Throwable e) {
            System.out.println("\ntestManyFieldsAndMethods() failed: ");
            e.printStackTrace();
            nFailure += 1;
        }
        many.field678 = -1;
        assert0(many.method499() + many.method678() == 498, "testManyFieldsAndMethods: many.method499() + many.method678() == 498");
    }
