    @Override
    public void run() {
        Float vact = new Float(Utilidades.actualVersion);
        FutureTask<Void> calli;
        String ver = Utilidades.getLatestVersion();
        Logger.getLogger(VersionLauncher.class.getName()).log(Level.INFO, "{0} {1}", new Object[] { java.util.ResourceBundle.getBundle("Bundle").getString("VersionLauncher.version.text"), ver });
        if (ver != null) {
            Float vlast = new Float(ver);
            if (vlast.floatValue() > vact.floatValue()) {
                calli = new FutureTask<Void>(new CallableImpl(vlast));
                SwingUtilities.invokeLater(calli);
                int val = -1;
                try {
                    calli.get();
                } catch (InterruptedException ex) {
                    Logger.getLogger(VersionLauncher.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(VersionLauncher.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (val == JOptionPane.OK_OPTION) {
                    try {
                        URI uri = new URI("http://sourceforge.net/projects/jmusicmanager");
                        if (Desktop.isDesktopSupported()) {
                            try {
                                Desktop.getDesktop().browse(uri);
                            } catch (IOException ex) {
                                Logger.getLogger(VersionLauncher.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } catch (URISyntaxException ex) {
                        Logger.getLogger(VersionLauncher.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                if (mostrarFallo) {
                    calli = new FutureTask<Void>(new CallableImpl(java.util.ResourceBundle.getBundle("Bundle").getString("NewJFrame.Updates.Fail")));
                    SwingUtilities.invokeLater(calli);
                }
            }
        } else {
            SwingUtilities.invokeLater(new FutureTask<Void>(new CallableImpl(java.util.ResourceBundle.getBundle("Bundle").getString("NewJFrame.Updates.FailConnect"))));
        }
    }
