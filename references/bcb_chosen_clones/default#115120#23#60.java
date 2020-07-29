    public CustomAlgorithm(String name, String javaFileName, String customType) {
        super(name, javaFileName);
        this.customType = customType;
        try {
            System.out.println("Start Compiling custom Algorithm");
            JCompiler.compile(this.getClassName(), getCurrentDir() + "");
            System.out.println("End Compiling custom Algorithm");
            try {
                URL classUrl;
                classUrl = new URL("file://" + getCurrentDir() + "/customAlgorithms/");
                System.out.println("cluassurl: " + classUrl.getPath());
                URL[] classUrls = { classUrl };
                URLClassLoader ucl = new URLClassLoader(classUrls);
                loadedClass = ucl.loadClass(this.getClassName());
                int[] array = new int[] { 3, 2, 1 };
                try {
                    Method meth = loadedClass.getMethod("run", int[].class);
                    meth.invoke(loadedClass.newInstance(), array);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
