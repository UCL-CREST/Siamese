    public static void main(String[] args) {
        final boolean[] running = new boolean[] { true };
        Thread t = new Thread(new Runnable() {

            public void run() {
                while (running[0]) {
                    System.out.println("Thread running...");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
        DelayingClassLoader dcl = new DelayingClassLoader();
        try {
            Class<?> c = dcl.loadClass("ConcLoaderClient", true);
            Object i = c.getConstructor().newInstance();
            Method m = c.getMethod("doSomething");
            m.invoke(i);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        running[0] = false;
    }
