    public static boolean makeBackup(File dir, String sourcedir, String destinationdir, String destinationDirEnding, boolean autoInitialized) {
        boolean success = false;
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
        try {
            for (int i = 0; i < files.length; i++) {
                File f = new File(dir, files[i]);
                File g = new File(files[i]);
                if (f.isDirectory()) {
                } else if (f.getName().endsWith("saving")) {
                } else {
                    if (f.canRead()) {
                        String destinationFile = checkdir + System.getProperty("file.separator") + g;
                        String sourceFile = sourcedir + System.getProperty("file.separator") + g;
                        FileInputStream infile = new FileInputStream(sourceFile);
                        FileOutputStream outfile = new FileOutputStream(destinationFile);
                        int c;
                        while ((c = infile.read()) != -1) outfile.write(c);
                        infile.close();
                        outfile.close();
                    } else {
                        System.out.println(f.getName() + " is LOCKED!");
                        while (!f.canRead()) {
                        }
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
            }
            success = true;
        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        }
        if (autoInitialized) {
            Display display = View.getDisplay();
            if (display != null || !display.isDisposed()) {
                View.getDisplay().syncExec(new Runnable() {

                    public void run() {
                        Tab4.redrawBackupTable();
                    }
                });
            }
            return success;
        } else {
            View.getDisplay().syncExec(new Runnable() {

                public void run() {
                    StatusBoxUtils.mainStatusAdd(" Backup Complete", 1);
                    View.getPluginInterface().getPluginconfig().setPluginParameter("Azcvsupdater_last_backup", Time.getCurrentTime(View.getPluginInterface().getPluginconfig().getPluginBooleanParameter("MilitaryTime")));
                    Tab4.lastBackupTime = View.getPluginInterface().getPluginconfig().getPluginStringParameter("Azcvsupdater_last_backup");
                    if (Tab4.lastbackupValue != null || !Tab4.lastbackupValue.isDisposed()) {
                        Tab4.lastbackupValue.setText("Last backup: " + Tab4.lastBackupTime);
                    }
                    Tab4.redrawBackupTable();
                    Tab6Utils.refreshLists();
                }
            });
            return success;
        }
    }
