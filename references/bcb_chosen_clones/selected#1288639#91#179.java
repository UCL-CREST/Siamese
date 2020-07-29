    private void download() throws IOException, ENTUnauthenticatedUserException {
        ENTDownloader entd = ENTDownloader.getInstance();
        boolean isThereDirectories = isThereDirectories();
        boolean isMultiple = downList.size() > 1;
        boolean isDownloadAll = (isMultiple && downList.size() == entd.getDirectoryContent().size());
        if (downList == null || downList.isEmpty()) return;
        if (isMultiple || isThereDirectories) {
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            downloadFrame.setOpenWhenFinishedText("Ouvrir le dossier à la fin du téléchargement");
        } else {
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            downloadFrame.setOpenWhenFinishedText("Ouvrir le fichier à la fin du téléchargement");
            if (!isThereDirectories) {
                fileChooser.setSelectedFile(new File(fileChooser.getSelectedFile(), downList.get(0).getName()));
            }
        }
        LookAndFeel lookfeel = null;
        if (!System.getProperty("os.name").contains("Linux")) {
            lookfeel = javax.swing.UIManager.getLookAndFeel();
            try {
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(fileChooser);
            } catch (Exception e) {
            }
        }
        int saveResult = fileChooser.showSaveDialog(parent);
        if (lookfeel != null) {
            try {
                javax.swing.UIManager.setLookAndFeel(lookfeel);
            } catch (Exception e) {
            }
        }
        if (saveResult != JFileChooser.APPROVE_OPTION) return;
        File saveas = fileChooser.getSelectedFile();
        if (!isThereDirectories) {
            long totalsize = 0;
            int nbFiles = 0;
            for (Iterator<FS_Element> file = downList.iterator(); file.hasNext(); ) {
                totalsize += ((FS_File) file.next()).getSize();
                nbFiles++;
            }
            downloadFrame.setTotalInfos(nbFiles, totalsize);
        } else {
            downloadFrame.setTotalInfos(DownloadFrame.UNKNOWN, DownloadFrame.UNKNOWN);
        }
        downloadFrame.setTotalDownloaded(0, 0);
        downloadFrame.setOpenWhenFinishedVisible(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE) && Desktop.getDesktop().isSupported(java.awt.Desktop.Action.OPEN));
        downloadFrame.setVisible(true);
        Broadcaster.addDownloadedBytesListener(this);
        Broadcaster.addStartDownloadListener(this);
        Broadcaster.addEndDownloadListener(this);
        Broadcaster.addFileAlreadyExistsListener(this);
        totalSizeDownloaded = nbFilesDownloaded = 0;
        String savePath = saveas.getPath();
        if (isMultiple && !savePath.substring(savePath.length() - 1).equals(System.getProperty("file.separator"))) {
            savePath += System.getProperty("file.separator");
        }
        if (isDownloadAll) {
            entd.getAllFiles(savePath, -1);
        } else {
            for (Iterator<FS_Element> it = downList.iterator(); it.hasNext(); ) {
                FS_Element el = it.next();
                if (el.isFile()) {
                    entd.getFile(el.getName(), savePath);
                } else {
                    try {
                        entd.changeDirectory(el.getName());
                        entd.getAllFiles(new File(saveas, el.getName()).getPath(), -1);
                        entd.changeDirectory("..");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        downloadFrame.setVisible(false);
        if (downloadFrame.openWhenFinished()) {
            try {
                if (isMultiple || isThereDirectories) {
                    java.awt.Desktop.getDesktop().browse(saveas.toURI());
                } else {
                    java.awt.Desktop.getDesktop().open(saveas);
                }
            } catch (Exception e) {
                System.err.println("Impossible d'ouvrir " + saveas.getPath());
            }
        }
        dispose();
    }
