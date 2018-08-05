    private void pack() {
        String szImageDir = m_szBasePath + "Images";
        File fImageDir = new File(szImageDir);
        fImageDir.mkdirs();
        String ljIcon = System.getProperty("user.home");
        ljIcon += System.getProperty("file.separator") + "MochaJournal" + System.getProperty("file.separator") + m_szUsername + System.getProperty("file.separator") + "Cache";
        File fUserDir = new File(ljIcon);
        File[] fIcons = fUserDir.listFiles();
        int iSize = fIcons.length;
        for (int i = 0; i < iSize; i++) {
            try {
                File fOutput = new File(fImageDir, fIcons[i].getName());
                if (!fOutput.exists()) {
                    fOutput.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(fOutput);
                    FileInputStream fIn = new FileInputStream(fIcons[i]);
                    while (fIn.available() > 0) fOut.write(fIn.read());
                }
            } catch (IOException e) {
                System.err.println(e);
            }
        }
        try {
            FileOutputStream fOut;
            InputStream fLJIcon = getClass().getResourceAsStream("/org/homedns/krolain/MochaJournal/Images/userinfo.gif");
            File fLJOut = new File(fImageDir, "user.gif");
            if (!fLJOut.exists()) {
                fOut = new FileOutputStream(fLJOut);
                while (fLJIcon.available() > 0) fOut.write(fLJIcon.read());
            }
            fLJIcon = getClass().getResourceAsStream("/org/homedns/krolain/MochaJournal/Images/communitynfo.gif");
            fLJOut = new File(fImageDir, "comm.gif");
            if (!fLJOut.exists()) {
                fOut = new FileOutputStream(fLJOut);
                while (fLJIcon.available() > 0) fOut.write(fLJIcon.read());
            }
            fLJIcon = getClass().getResourceAsStream("/org/homedns/krolain/MochaJournal/Images/icon_private.gif");
            fLJOut = new File(fImageDir, "icon_private.gif");
            if (!fLJOut.exists()) {
                fOut = new FileOutputStream(fLJOut);
                while (fLJIcon.available() > 0) fOut.write(fLJIcon.read());
            }
            fLJIcon = getClass().getResourceAsStream("/org/homedns/krolain/MochaJournal/Images/icon_protected.gif");
            fLJOut = new File(fImageDir, "icon_protected.gif");
            if (!fLJOut.exists()) {
                fOut = new FileOutputStream(fLJOut);
                while (fLJIcon.available() > 0) fOut.write(fLJIcon.read());
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
