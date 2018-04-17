    public void run() {
        if (!Desktop.isDesktopSupported()) {
            message = Globals.lang("Error creating email");
            return;
        }
        BasePanel panel = frame.basePanel();
        if (panel == null) return;
        if (panel.getSelectedEntries().length == 0) {
            message = Globals.lang("No entries selected") + ".";
            return;
        }
        StringWriter sw = new StringWriter();
        BibtexEntry[] bes = panel.getSelectedEntries();
        LatexFieldFormatter ff = new LatexFieldFormatter();
        ArrayList<String> attachments = new ArrayList<String>();
        HashSet<File> directories = new HashSet<File>();
        boolean openFolders = JabRefPreferences.getInstance().getBoolean("openFoldersOfAttachedFiles");
        String osName = System.getProperty("os.name");
        boolean isWindows = osName.startsWith("Windows");
        for (BibtexEntry entry : bes) {
            try {
                entry.write(sw, ff, true);
                FileListTableModel tm = new FileListTableModel();
                tm.setContent(entry.getField("file"));
                for (int i = 0; i < tm.getRowCount(); i++) {
                    FileListEntry flEntry = tm.getEntry(i);
                    File f = Util.expandFilename(flEntry.getLink(), frame.basePanel().metaData().getFileDirectory(GUIGlobals.FILE_FIELD));
                    if (f != null) {
                        attachments.add(f.getPath());
                        if (openFolders) {
                            if (isWindows) {
                                String command = "explorer.exe /select,\"".concat(f.getAbsolutePath().concat("\""));
                                Runtime.getRuntime().exec(command);
                            } else {
                                directories.add(f.getParentFile());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                message = Globals.lang("Error creating email");
                return;
            }
        }
        String mailTo = "?Body=".concat(sw.getBuffer().toString());
        mailTo = mailTo.concat("&Subject=");
        mailTo = mailTo.concat(JabRefPreferences.getInstance().get(JabRefPreferences.EMAIL_SUBJECT));
        for (String path : attachments) {
            mailTo = mailTo.concat("&Attachment=\"").concat(path);
            mailTo = mailTo.concat("\"");
        }
        URI uriMailTo = null;
        try {
            uriMailTo = new URI("mailto", mailTo, null);
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
            message = Globals.lang("Error creating email");
            return;
        }
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.mail(uriMailTo);
        } catch (IOException e) {
            e.printStackTrace();
            message = Globals.lang("Error creating email");
            return;
        }
        if (openFolders) {
            for (File d : directories) {
                try {
                    desktop.open(d);
                } catch (IOException e) {
                    e.printStackTrace();
                    message = String.format("%s: %s", Globals.lang("Could not open directory"), d.getAbsolutePath());
                    return;
                }
            }
        }
        message = String.format("%s: %d", Globals.lang("Entries added to an email"), bes.length);
    }
