    public void launchHelpFile() {
        try {
            File pdfFile = new File(WPlanner.installDir + "WPlanner Help File.pdf");
            if (pdfFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    JOptionPane.showMessageDialog(this, "AWT Desktop not supported - Unable to open WPlanner Help File.pdf");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Unable to find WPlanner Help File.pdf");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
