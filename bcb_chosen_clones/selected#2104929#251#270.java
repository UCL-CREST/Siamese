    private void openFolder(String locationProperty) {
        String file = TemplateNXProps.getInstance().getStringProperty(locationProperty) + File.separator + String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        System.err.println(file);
        File f = new File(file);
        f.mkdirs();
        if (Desktop.isDesktopSupported()) {
            Desktop d = Desktop.getDesktop();
            if (d.isSupported(Desktop.Action.BROWSE)) {
                try {
                    d.browse(f.getCanonicalFile().toURI());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Cette action n'est pas supportée par votre système d'exploitation.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Votre système d'exploitation n'est pas supporté.");
        }
    }
