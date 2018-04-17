    static Object createRJavaLoader(String rhome, String[] cp, boolean addJRI) {
        String rJavaRoot = null;
        if (rs_libs != null && rs_libs.length() > 0) rJavaRoot = findInPath(rs_libs, "rJava", false);
        if (rJavaRoot == null) rJavaRoot = u2w(rhome + "/library/rJava");
        if (!(new File(rJavaRoot)).exists()) {
            lastError = "Unable to find rJava";
            return null;
        }
        File f = new File(u2w(rJavaRoot + "/java/boot"));
        if (!f.exists()) {
            lastError = "rJava too old";
            return null;
        }
        String rJavaHome = u2w(rJavaRoot);
        File lf = null;
        if (rs_arch != null && rs_arch.length() > 0) lf = new File(u2w(rJavaRoot + "/libs" + rs_arch));
        if (lf == null || !lf.exists()) lf = new File(u2w(rJavaRoot + "/libs/" + arch()));
        if (!lf.exists()) lf = new File(u2w(rJavaRoot + "/libs"));
        String rJavaLibs = lf.toString();
        JRIClassLoader mcl = JRIClassLoader.getMainLoader();
        mcl.addClassPath(f.toString());
        try {
            Class rjlclass = mcl.findAndLinkClass("RJavaClassLoader");
            Constructor c = rjlclass.getConstructor(new Class[] { String.class, String.class });
            Object rjcl = c.newInstance(new Object[] { rJavaHome, rJavaLibs });
            System.out.println("RJavaClassLoader: " + rjcl);
            if (addJRI) {
                if (cp == null || cp.length == 0) cp = new String[] { u2w(rJavaRoot + "/jri/JRI.jar") }; else {
                    String[] ncp = new String[cp.length + 1];
                    System.arraycopy(cp, 0, ncp, 1, cp.length);
                    ncp[0] = u2w(rJavaRoot + "/jri/JRI.jar");
                    cp = ncp;
                }
            }
            if (cp == null || cp.length == 0) cp = new String[] { u2w(rJavaRoot + "/java/boot") }; else {
                String[] ncp = new String[cp.length + 1];
                System.arraycopy(cp, 0, ncp, 1, cp.length);
                ncp[0] = u2w(rJavaRoot + "/java/boot");
                cp = ncp;
            }
            if (cp != null) {
                System.out.println(" - adding class paths");
                Method m = rjlclass.getMethod("addClassPath", new Class[] { String[].class });
                m.invoke(rjcl, new Object[] { cp });
            }
            return rjcl;
        } catch (Exception rtx) {
            System.err.println("ERROR: Unable to create new RJavaClassLoader in JRIBootstrap! (" + rtx + ")");
            rtx.printStackTrace();
            System.exit(2);
        }
        return null;
    }
