    public static void openFile(Component parentComponent, String fileName) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            File file = new File(fileName);
            try {
                desktop.open(file);
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(parentComponent, ioe.getMessage(), Messages.getString("VcMainFrame.msgTitleError"), JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(parentComponent, e.getMessage(), Messages.getString("VcMainFrame.msgTitleError"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }
