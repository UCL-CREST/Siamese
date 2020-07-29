    public void ouvrirEditeur() {
        if (ConfigNat.getCurrentConfig().getUseNatEditor()) {
            if (ConfigNat.getCurrentConfig().isReverseTrans()) {
                gestErreur.afficheMessage(texts.getText("copy"), Nat.LOG_DEBUG);
                FileToolKit.copyFile(jtfNoir.getText(), EditeurTan.tmpXHTML);
                EditeurTan et = new EditeurTan(null, jtfBraille.getText(), nat);
                et.setExtendedState(Frame.MAXIMIZED_BOTH);
                et.setState(MAXIMIZED_BOTH);
                et.setVisible(true);
            } else {
                afficheFichier(jtfBraille.getText());
            }
        } else if (ConfigNat.getCurrentConfig().getUseDefaultEditor()) {
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.OPEN)) {
                    try {
                        desktop.open(new File(jtfBraille.getText()));
                    } catch (IOException e) {
                        gestErreur.afficheMessage(texts.getText("buginout") + jtfBraille.getText(), Nat.LOG_SILENCIEUX);
                    }
                } else {
                    gestErreur.afficheMessage(texts.getText("editornotfound"), Nat.LOG_SILENCIEUX);
                }
            } else {
                gestErreur.afficheMessage(texts.getText("desktopnotsupported"), Nat.LOG_SILENCIEUX);
            }
        } else {
            File fsortie = new File(jtfBraille.getText());
            String[] cmd = new String[2];
            try {
                cmd[0] = ConfigNat.getCurrentConfig().getEditeur();
                cmd[1] = fsortie.getCanonicalPath();
                Runtime.getRuntime().exec(cmd);
            } catch (IOException ioe) {
                gestErreur.afficheMessage(texts.getText("buginout2") + cmd[0] + texts.getText("with") + cmd[1], Nat.LOG_SILENCIEUX);
            }
        }
    }
