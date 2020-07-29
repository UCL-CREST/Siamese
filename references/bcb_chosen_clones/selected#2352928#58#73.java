    public void open(File f) {
        if (Desktop.isDesktopSupported()) {
            Desktop d = Desktop.getDesktop();
            if (d.isSupported(Desktop.Action.OPEN)) {
                try {
                    d.open(f.getCanonicalFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Cette action n'est pas supporté par votre système d'exploitation.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Votre système d'exploitation n'est pas supporté.");
        }
    }
