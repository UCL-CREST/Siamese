    public static void main(String[] args) throws Throwable {
        if (args.length == 0) {
            for (String s : usage) {
                System.err.println(s);
            }
            System.exit(1);
        }
        computeJpfHome(args);
        addJpfClassPath();
        if (System.getenv("VERBOSE") != null) {
            System.err.println("CLASSPATH: " + cpString);
        }
        String jpfClassName = "gov.nasa.jpf.JPF";
        String[] targetArgs = new String[args.length + 1];
        targetArgs[0] = "+jpf.basedir=" + jpfHome;
        System.arraycopy(args, 0, targetArgs, 1, args.length);
        if (System.getenv("VERBOSE") != null) {
            System.err.print("main & args: " + jpfClassName);
            for (int i = 0; i < targetArgs.length; i++) {
                System.err.print(" ");
                System.err.print(targetArgs[i]);
            }
            System.err.println();
        }
        try {
            ClassLoader cl = getClassPathClassLoader();
            Class<?> jpfClass = cl.loadClass(jpfClassName);
            Constructor<?> jpfConst = jpfClass.getConstructor(new Class[] { String[].class });
            if (jpfConst == null || !Runnable.class.isAssignableFrom(jpfClass)) {
                throw new ClassNotFoundException("Invalid/out of date " + jpfClassName + " class");
            }
            Runnable jpf = (Runnable) jpfConst.newInstance(new Object[] { targetArgs });
            jpf.run();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Make sure you have compiled JPF into build/jpf.");
            System.err.println("Try this:  java RunAnt");
            System.exit(1);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (InvocationTargetException e) {
            printTrimmedStackTrace(e.getTargetException());
            System.exit(1);
        }
    }
