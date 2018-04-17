        private void tryExecuteSystemCommand(FilesystemTreeGraphicWrapper tree, MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON2) {
                try {
                    if (Desktop.isDesktopSupported() && tree.getSelectedNode().getItem() instanceof FileInfo) {
                        FileInfo info = (FileInfo) tree.getSelectedNode().getItem();
                        Desktop.getDesktop().open(info.getFile());
                    }
                } catch (IOException ex) {
                    Logger.getLogger(DirectorySpacePanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
