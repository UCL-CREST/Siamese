    boolean saveWithName() {
        String fin;
        String din;
        if (Globals.useNativeFileDialogs) {
            FileDialog fd = new FileDialog(this, Globals.messages.getString("SaveName"), FileDialog.SAVE);
            fd.setDirectory(openFileDirectory);
            fd.setFilenameFilter(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    return (name.toLowerCase().endsWith(".fcd"));
                }
            });
            fd.setVisible(true);
            fin = fd.getFile();
            din = fd.getDirectory();
        } else {
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(new javax.swing.filechooser.FileFilter() {

                public boolean accept(File f) {
                    return (f.getName().toLowerCase().endsWith(".fcd") || f.isDirectory());
                }

                public String getDescription() {
                    return "FidoCadJ (.fcd)";
                }
            });
            fc.setCurrentDirectory(new File(openFileDirectory));
            fc.setDialogTitle(Globals.messages.getString("SaveName"));
            if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return false;
            fin = fc.getSelectedFile().getName();
            din = fc.getSelectedFile().getParentFile().getPath();
        }
        if (fin != null) {
            CC.P.openFileName = Globals.createCompleteFileName(din, fin);
            CC.P.openFileName = Globals.adjustExtension(CC.P.openFileName, Globals.DEFAULT_EXTENSION);
            if (runsAsApplication) prefs.put("OPEN_DIR", din);
            openFileDirectory = din;
            return save();
        } else {
            return false;
        }
    }
