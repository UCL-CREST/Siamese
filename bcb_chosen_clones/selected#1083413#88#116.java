    public static void copyOverWarFile() {
        System.out.println("Copy Over War File:");
        File dir = new File(theAppsDataDir);
        FileFilter ff = new WildcardFileFilter("*.war");
        if (dir.listFiles(ff).length == 0) {
            dir = new File(System.getProperty("user.dir") + "/war");
            if (dir.exists()) {
                File[] files = dir.listFiles(ff);
                for (File f : files) {
                    try {
                        File newFile = new File("" + theAppsDataDir + "/" + f.getName());
                        System.out.println("Creating new file \"" + f.getAbsolutePath() + "\"");
                        newFile.createNewFile();
                        InputStream fi = new FileInputStream(f);
                        OutputStream fo = new FileOutputStream(newFile);
                        IOUtils.copy(fi, fo);
                        moveUnzipAndExtract(newFile);
                    } catch (Exception ex) {
                        Logger.getLogger(AppDataDir.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } else {
            System.out.println("Found a war in the apps data dir, ignoring a fresh copy");
        }
        new JFileChooser().setCurrentDirectory(new File(theAppsDataDir));
        System.setProperty("user.dir", theAppsDataDir);
        System.out.println("User.dir : " + System.getProperty("user.dir"));
    }
