    public void run() {
        Class target = Target.class;
        Method meth = null;
        Field field = null;
        boolean excep;
        try {
            meth = target.getMethod("myMethod", new Class[] { int.class });
            if (meth.getDeclaringClass() != target) throw new RuntimeException();
            printMethodInfo(meth);
            meth = target.getMethod("myMethod", new Class[] { float.class });
            printMethodInfo(meth);
            meth = target.getMethod("myNoargMethod", (Class[]) null);
            printMethodInfo(meth);
            meth = target.getMethod("myMethod", new Class[] { String[].class, float.class, char.class });
            printMethodInfo(meth);
            Target instance = new Target();
            Object[] argList = new Object[] { new String[] { "hi there" }, new Float(3.1415926f), new Character('Q') };
            System.out.println("Before, float is " + ((Float) argList[1]).floatValue());
            Integer boxval;
            boxval = (Integer) meth.invoke(instance, argList);
            System.out.println("Result of invoke: " + boxval.intValue());
            System.out.println("Calling no-arg void-return method");
            meth = target.getMethod("myNoargMethod", (Class[]) null);
            meth.invoke(instance, (Object[]) null);
            meth = target.getMethod("throwingMethod", (Class[]) null);
            try {
                meth.invoke(instance, (Object[]) null);
                System.out.println("GLITCH: didn't throw");
            } catch (InvocationTargetException ite) {
                System.out.println("Invoke got expected exception:");
                System.out.println(ite.getClass().getName());
                System.out.println(ite.getCause());
            } catch (Exception ex) {
                System.out.println("GLITCH: invoke got wrong exception:");
                ex.printStackTrace();
            }
            System.out.println("");
            field = target.getField("string1");
            if (field.getDeclaringClass() != target) throw new RuntimeException();
            printFieldInfo(field);
            String strVal = (String) field.get(instance);
            System.out.println("  string1 value is '" + strVal + "'");
            showStrings(instance);
            field.set(instance, new String("a new string"));
            strVal = (String) field.get(instance);
            System.out.println("  string1 value is now '" + strVal + "'");
            showStrings(instance);
            try {
                field.set(instance, new Object());
                System.out.println("WARNING: able to store Object into String");
            } catch (IllegalArgumentException iae) {
                System.out.println("  got expected illegal obj store exc");
            }
            try {
                String four;
                field = target.getField("string4");
                four = (String) field.get(instance);
                System.out.println("WARNING: able to access string4: " + four);
            } catch (IllegalAccessException iae) {
                System.out.println("  got expected access exc");
            } catch (NoSuchFieldException nsfe) {
                System.out.println("  got the other expected access exc");
            }
            try {
                String three;
                field = target.getField("string3");
                three = (String) field.get(this);
                System.out.println("WARNING: able to get string3 in wrong obj: " + three);
            } catch (IllegalArgumentException iae) {
                System.out.println("  got expected arg exc");
            }
            String four;
            field = target.getDeclaredField("string3");
            field.set(instance, null);
            long longVal;
            field = target.getField("pubLong");
            longVal = field.getLong(instance);
            System.out.println("pubLong initial value is " + Long.toHexString(longVal));
            field.setLong(instance, 0x9988776655443322L);
            longVal = field.getLong(instance);
            System.out.println("pubLong new value is " + Long.toHexString(longVal));
            field = target.getField("superInt");
            if (field.getDeclaringClass() == target) throw new RuntimeException();
            printFieldInfo(field);
            int intVal = field.getInt(instance);
            System.out.println("  superInt value is " + intVal);
            Integer boxedIntVal = (Integer) field.get(instance);
            System.out.println("  superInt boxed is " + boxedIntVal);
            field.set(instance, new Integer(20202));
            intVal = field.getInt(instance);
            System.out.println("  superInt value is now " + intVal);
            field.setShort(instance, (short) 30303);
            intVal = field.getInt(instance);
            System.out.println("  superInt value (from short) is now " + intVal);
            field.setInt(instance, 40404);
            intVal = field.getInt(instance);
            System.out.println("  superInt value is now " + intVal);
            try {
                field.set(instance, new Long(123));
                System.out.println("FAIL: expected exception not thrown");
            } catch (IllegalArgumentException iae) {
                System.out.println("  got expected long->int failure");
            }
            try {
                field.setLong(instance, 123);
                System.out.println("FAIL: expected exception not thrown");
            } catch (IllegalArgumentException iae) {
                System.out.println("  got expected long->int failure");
            }
            try {
                field.set(instance, new String("abc"));
                System.out.println("FAIL: expected exception not thrown");
            } catch (IllegalArgumentException iae) {
                System.out.println("  got expected string->int failure");
            }
            try {
                field.getShort(instance);
                System.out.println("FAIL: expected exception not thrown");
            } catch (IllegalArgumentException iae) {
                System.out.println("  got expected int->short failure");
            }
            field = target.getField("superClassInt");
            printFieldInfo(field);
            int superClassIntVal = field.getInt(instance);
            System.out.println("  superClassInt value is " + superClassIntVal);
            field = target.getField("staticDouble");
            printFieldInfo(field);
            double staticDoubleVal = field.getDouble(null);
            System.out.println("  staticDoubleVal value is " + staticDoubleVal);
            try {
                field.getLong(instance);
                System.out.println("FAIL: expected exception not thrown");
            } catch (IllegalArgumentException iae) {
                System.out.println("  got expected double->long failure");
            }
            excep = false;
            try {
                field = target.getField("aPrivateInt");
                printFieldInfo(field);
            } catch (NoSuchFieldException nsfe) {
                System.out.println("as expected: aPrivateInt not found");
                excep = true;
            }
            if (!excep) System.out.println("BUG: got aPrivateInt");
            field = target.getField("constantString");
            printFieldInfo(field);
            String val = (String) field.get(instance);
            System.out.println("  Constant test value is " + val);
            field = target.getField("cantTouchThis");
            printFieldInfo(field);
            intVal = field.getInt(instance);
            System.out.println("  cantTouchThis is " + intVal);
            try {
                field.setInt(instance, 99);
                System.out.println("ERROR: set-final succeeded\n");
            } catch (IllegalAccessException iae) {
                System.out.println("  got expected set-final failure\n");
            }
            intVal = field.getInt(instance);
            System.out.println("  cantTouchThis is now " + intVal);
            Constructor<Target> cons;
            Target targ;
            Object[] args;
            cons = target.getConstructor(new Class[] { int.class, float.class });
            args = new Object[] { new Integer(7), new Float(3.3333) };
            System.out.println("cons modifiers=" + cons.getModifiers());
            targ = cons.newInstance(args);
            targ.myMethod(17);
        } catch (Exception ex) {
            System.out.println("----- unexpected exception -----");
            ex.printStackTrace();
        }
        System.out.println("ReflectTest done!");
    }
