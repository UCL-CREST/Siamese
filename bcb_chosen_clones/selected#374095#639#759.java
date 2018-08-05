    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == openMenuItem) {
            JFileChooser fileChooser = new JFileChooser(Settings.getLastDir());
            fileChooser.addChoosableFileFilter(playlistFileFilter);
            fileChooser.addChoosableFileFilter(audioFileFilter);
            fileChooser.setMultiSelectionEnabled(false);
            if (fileChooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                FileFilter fileFilter = fileChooser.getFileFilter();
                if (fileFilter == playlistFileFilter) {
                    playlistManager.clearPlaylist();
                    acStop();
                    playlistManager.loadPlaylist(file.getPath());
                } else if (fileFilter == audioFileFilter) {
                    String fileName = file.getName().substring(0, file.getName().lastIndexOf(".")).trim();
                    PlaylistItem pli = new PlaylistItem(fileName, file.getAbsolutePath(), -1, true);
                    playlistManager.add(pli);
                    playlist.setCursor(playlist.indexOf(pli));
                }
                acOpenAndPlay();
                Settings.setLastDir(file.getParent());
            }
        } else if (source == openURLMenuItem) {
            String url = JOptionPane.showInputDialog(mainFrame, "Enter the URL to a media file on the Internet!", "Open URL", JOptionPane.QUESTION_MESSAGE);
            if (url != null && Utilities.startWithProtocol(url)) {
                boolean isPlaylistFile = false;
                for (String ext : PlaylistFileFilter.playlistExt) {
                    if (url.endsWith(ext)) {
                        isPlaylistFile = true;
                    }
                }
                if (isPlaylistFile) {
                    playlistManager.clearPlaylist();
                    playlistManager.loadPlaylist(url);
                    playlist.begin();
                } else {
                    PlaylistItem pli = new PlaylistItem(url, url, -1, false);
                    playlistManager.add(pli);
                    playlist.setCursor(playlist.indexOf(pli));
                }
                acOpenAndPlay();
            }
        } else if (source == openPlaylistMenuItem) {
            playlistManager.openPlaylist();
        } else if (source == savePlaylistMenuItem) {
            playlistManager.savePlaylistDialog();
        } else if (source == preferencesMenuItem) {
            preferencesDialog = new PreferencesDialog(mainFrame, audioPlayer);
            preferencesDialog.setVisible(true);
        } else if (source == exitMenuItem) {
            exit();
        } else if (source == playlistMenuItem) {
            switchView();
        } else if (source == playPauseMenuItem || source == playPauseButton) {
            acPlayPause();
        } else if (source == previousMenuItem || source == previousButton) {
            acPrevious();
        } else if (source == nextMenuItem || source == nextButton) {
            acNext();
        } else if (source == addFilesMenuItem) {
            playlistManager.addFilesDialog();
        } else if (source == removeItemsMenuItem) {
            playlistManager.remove();
            acOpen();
        } else if (source == clearPlaylistMenuItem) {
            playlistManager.clearPlaylist();
            acStop();
        } else if (source == moveUpItemsMenuItem) {
            playlistManager.moveUp();
        } else if (source == moveDownItemsMenuItem) {
            playlistManager.moveDown();
        } else if (source == randomizePlaylistMenuItem) {
            playlistManager.randomizePlaylist();
            acOpen();
        } else if (source == stopMenuItem || source == stopButton) {
            acStop();
        } else if (source == infoMenuItem) {
            playlistManager.showTagInfoDialog();
        } else if (source == updateMenuItem) {
            SoftwareUpdate.checkForUpdates(true, false);
            SoftwareUpdate.showCheckForUpdatesDialog();
        } else if (source == aboutMenuItem) {
            Object[] options = { LanguageBundle.getString("Button.Close") };
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    options = new Object[] { LanguageBundle.getString("Button.Close"), LanguageBundle.getString("Button.Website") };
                }
            }
            Version currentVersion = Version.getCurrentVersion();
            StringBuffer message = new StringBuffer();
            message.append("<html><b><font color='red' size='5'>" + LanguageBundle.getString("Application.title"));
            message.append("</font></b><br>" + LanguageBundle.getString("Application.description"));
            message.append("<br>Copyright © 2005-2008 The Xtreme Media Player Project");
            message.append("<br><br><b>Author and Developer: </b>" + LanguageBundle.getString("Application.author"));
            message.append("<br><b>Version: </b>" + currentVersion);
            message.append("<br><b>Release date: </b>" + currentVersion.getReleaseDate());
            message.append("<br><b>Homepage: </b>" + LanguageBundle.getString("Application.homepage"));
            message.append("<br><br><b>Java version: </b>" + System.getProperty("java.version"));
            message.append("<br><b>Java vendor: </b>" + System.getProperty("java.vendor"));
            message.append("<br><b>Java home: </b>" + System.getProperty("java.home"));
            message.append("<br><b>OS name: </b>" + System.getProperty("os.name"));
            message.append("<br><b>OS arch: </b>" + System.getProperty("os.arch"));
            message.append("<br><b>User name: </b>" + System.getProperty("user.name"));
            message.append("<br><b>User home: </b>" + System.getProperty("user.home"));
            message.append("<br><b>User dir: </b>" + System.getProperty("user.dir"));
            message.append("</html>");
            int n = JOptionPane.showOptionDialog(mainFrame, message, "About", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, new ImageIcon(Utilities.getLogoImage(128, 1.5f)), options, options[0]);
            if (n == 1 && desktop != null) {
                try {
                    URL url = new URL(LanguageBundle.getString("Application.homepage"));
                    desktop.browse(url.toURI());
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
    }
