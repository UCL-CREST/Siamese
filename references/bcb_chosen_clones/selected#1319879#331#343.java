        public void openFile(File file) {
            int val = JOptionPane.showConfirmDialog(null, "Would You Like To View The File Right Now?", "View File", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            try {
                if (val == JOptionPane.YES_OPTION) {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(file);
                    } else {
                    }
                }
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(null, "Failed to Open File", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
