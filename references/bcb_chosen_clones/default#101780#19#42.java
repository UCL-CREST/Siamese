    public void start(Gedcom gedcom) {
        List<String> args = new ArrayList<String>();
        for (int i = 0; i < OPTIONS.length; i++) args.add(OPTIONS[i]);
        File reports = ReportLoader.getReportDirectory();
        args.add("-d");
        args.add(reports.getAbsolutePath());
        int sources = findSources(reports, args);
        if (sources == 0) {
            println(translate("nosources", reports));
            return;
        }
        Object rc = null;
        try {
            Object javac = Class.forName("com.sun.tools.javac.Main").newInstance();
            rc = javac.getClass().getMethod("compile", new Class[] { new String[0].getClass(), PrintWriter.class }).invoke(javac, new Object[] { args.toArray(new String[args.size()]), getOut() });
        } catch (Throwable t) {
            println(translate("javac.jre", System.getProperty("java.home")));
            return;
        }
        if (Integer.valueOf(0).equals(rc)) println(translate("javac.success", new Object[] { "" + sources, reports })); else {
            println("---");
            println(translate("javac.error"));
        }
    }
