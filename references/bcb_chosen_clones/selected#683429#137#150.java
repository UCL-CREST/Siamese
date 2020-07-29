        public void mouseClicked(MouseEvent event) {
            if (event.getClickCount() == 2) {
                File file = getSelectedFile();
                if (file.isDirectory()) {
                    setSelectionAsBaseDir();
                } else if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().open(file);
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }
                }
            }
        }
