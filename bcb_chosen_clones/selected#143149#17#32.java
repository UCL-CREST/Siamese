    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (!java.awt.Desktop.isDesktopSupported()) {
            Lang.showMesg(trayPtn.getMpwdPtn(), LangRes.P30F7A0F, "");
        }
        java.io.File help = new java.io.File("help", "index.html");
        if (!help.exists()) {
            Lang.showMesg(trayPtn.getMpwdPtn(), LangRes.P30F7A10, "");
            return;
        }
        try {
            java.awt.Desktop.getDesktop().browse(help.toURI());
        } catch (java.io.IOException exp) {
            Logs.exception(exp);
        }
    }
