    public static void main(String[] args) {
        Properties pt = System.getProperties();
        Enumeration<?> e = pt.propertyNames();
        String key = null;
        while (e.hasMoreElements()) {
            key = (String) e.nextElement();
            System.out.println(key + " = " + pt.getProperty(key));
        }
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("notepad I:\\SOFTWARE\\GVimPortable\\test2.txt");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        System.out.println(p.getInputStream().toString());
        System.out.println(p.getOutputStream().toString());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        p.destroy();
    }
