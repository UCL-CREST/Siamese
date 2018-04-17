    public static void main(String[] args) throws Exception {
        Preferences.setBatchMode(false);
        String memsetting = Preferences.readMemStringFromDisk();
        logger.info("Starting OBO-Edit using " + memsetting + " of memory specified in the OBO-Edit.vmoptions file");
        List<String> argList = new LinkedList<String>();
        argList.add("java");
        if (memsetting != null) argList.add("-Xmx" + memsetting);
        StringBuffer classpath = new StringBuffer();
        File runtimeDir = new File(Preferences.getInstallationDirectory(), "runtime");
        for (File jarFile : runtimeDir.listFiles(new ExtensionFilenameFilter("jar"))) {
            if (classpath.length() > 0) classpath.append(System.getProperty("path.separator"));
            String s = jarFile.toString().replace(System.getProperty("path.separator"), "\\" + System.getProperty("path.separator"));
            classpath.append(s);
        }
        argList.add("-classpath");
        argList.add(classpath.toString());
        argList.add("org.oboedit.launcher.OBOEdit");
        logger.info(argList);
        for (int i = 0; i < args.length; i++) argList.add(args[i]);
        String[] newargs = new String[argList.size()];
        Iterator<String> it = argList.iterator();
        for (int i = 0; it.hasNext(); i++) newargs[i] = it.next();
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(newargs);
        StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
        StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");
        errorGobbler.start();
        outputGobbler.start();
    }
