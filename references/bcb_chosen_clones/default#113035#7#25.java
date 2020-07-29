    public static void main(String[] args) {
        MuServer s = new MuServer();
        new mucode.util.Launcher(s).launch(args, 1);
        ClassSpace shared = s.getSharedClassSpace();
        try {
            System.out.println("I need " + CLASSNAME + ".class to proceed with execution.");
            System.out.println("I'll look on my host, until it comes...");
            Thread.sleep(1000);
            while (!shared.containsClass(CLASSNAME)) {
                Thread.sleep(3000);
                System.out.println("Waiting for the class ...");
            }
            System.out.println("Class " + CLASSNAME + " is now in my shared class space.");
            Class c = shared.getClass(CLASSNAME);
            c.getMethod("perform", null).invoke(c.newInstance(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
