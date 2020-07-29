    public static void delete_class() {
        File basedir = new File(System.getProperty("user.dir"));
        File[] alldirs = getalldirs(basedir);
        Vector classfiles = new Vector();
        for (int i = 0; i < alldirs.length; i++) {
            File[] f = alldirs[i].listFiles();
            for (int j = 0; j < f.length; j++) {
                if (f[j].isDirectory()) {
                    continue;
                }
                String s = f[j].toString();
                if (s.endsWith("SourceUtil.class")) {
                    continue;
                }
                if (s.endsWith("Ask.class")) {
                    continue;
                }
                if (s.endsWith(".class")) {
                    classfiles.addElement(f[j]);
                    System.out.println("Adding:" + s + " for DELETE!");
                }
            }
        }
        for (int m = 0; m < classfiles.size(); m++) {
            File to_del = (File) classfiles.elementAt(m);
            try {
                System.out.println("DELETING: " + to_del.toString());
                to_del.delete();
                System.out.println("->deleted");
            } catch (Exception e) {
                System.out.println("Failed to delete:" + to_del.toString());
            }
        }
    }
