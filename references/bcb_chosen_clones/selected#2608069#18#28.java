    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (!java.awt.Desktop.isDesktopSupported()) {
            Lang.showMesg(trayPtn.getMpwdPtn(), LangRes.P30F7A0F, "");
        }
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI(ConsEnv.MLOGSITE));
        } catch (Exception exp) {
            Logs.exception(exp);
        }
    }
