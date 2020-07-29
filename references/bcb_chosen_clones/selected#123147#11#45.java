    public static void makeBackup(File dir, String sourcedir, String destinationdir, String destinationDirEnding) {
        String[] files;
        files = dir.list();
        File checkdir = new File(destinationdir + System.getProperty("file.separator") + destinationDirEnding);
        if (!checkdir.isDirectory()) {
            checkdir.mkdir();
        }
        ;
        Date date = new Date();
        long msec = date.getTime();
        checkdir.setLastModified(msec);
        File checkFile = new File(checkdir + System.getProperty("file.separator") + "azureus.config");
        if (checkFile.exists()) {
            checkFile.setLastModified(msec);
        }
        try {
            for (int i = 0; i < files.length; i++) {
                File f = new File(dir, files[i]);
                File g = new File(files[i]);
                if (f.isDirectory()) {
                } else {
                    String destinationFile = checkdir + System.getProperty("file.separator") + g;
                    String sourceFile = sourcedir + System.getProperty("file.separator") + g;
                    FileInputStream infile = new FileInputStream(sourceFile);
                    FileOutputStream outfile = new FileOutputStream(destinationFile);
                    int c;
                    while ((c = infile.read()) != -1) outfile.write(c);
                    infile.close();
                    outfile.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
