    public static void createSearchDataBase() {
        String proDir = ConfigHolder.CONF.getProjectDir() + File.separator;
        ArrayList<String> argList = new ArrayList<String>();
        argList.add("-db");
        argList.add(getTmpDir() + File.separator + "JavaHelpSearch");
        argList.add("-sourcepath");
        argList.add(proDir);
        String[] fileList = CreateMapAction.getMappedFiles();
        for (int i = 0; i < fileList.length; i++) argList.add(fileList[i]);
        try {
            String[] strsArgCompile = new String[argList.size()];
            strsArgCompile = argList.toArray(new String[0]);
            JHelpDevFrame.getAJHelpDevToolFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
            indexer.compile(strsArgCompile);
            File newDir = new File(ConfigHolder.CONF.getProjectDir() + File.separator + "JavaHelpSearch");
            if (!newDir.isDirectory()) newDir.mkdir();
            String[] filesInSearchDir = new File(getTmpDir() + File.separator + "JavaHelpSearch").list();
            for (int i = 0; i < filesInSearchDir.length; i++) {
                HelperMethods.copyFileTo(newDir + "/" + filesInSearchDir[i], getTmpDir() + File.separator + "JavaHelpSearch/" + filesInSearchDir[i]);
            }
            System.out.println("Search database created with com.sun.java.help.search.Indexer.");
        } catch (Exception se) {
            System.out.println("Creation of search database failed.");
            se.printStackTrace();
            return;
        } finally {
            JHelpDevFrame.getAJHelpDevToolFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
