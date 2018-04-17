    protected void showDownloadFolder() {
        if (Desktop.isDesktopSupported()) {
            try {
                if (JPTrayIcon.isWindows()) Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL \"" + fileDownloads.getAbsolutePath() + "\""); else Desktop.getDesktop().browse(getFileURI(fileDownloads.getAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else JOptionPane.showMessageDialog(this, Messages.getString("FileWindow.DirectoryOpenNotSupported").replace("$PATH$", fileDownloads.getAbsolutePath()));
    }
