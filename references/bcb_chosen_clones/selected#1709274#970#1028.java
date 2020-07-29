    private void exporterConfig() {
        JFileChooser jfc = new JFileChooser();
        jfc.setCurrentDirectory(new File(ConfigNat.getUserConfigFolder()));
        jfc.addChoosableFileFilter(new FiltreFichier(new String[] { "nca" }, "archive configuration nat (*.nca)"));
        jfc.setDialogTitle("Exporter la configuration courante");
        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String fich = jfc.getSelectedFile().getCanonicalPath();
                if (!fich.endsWith(".nca")) {
                    fich += ".nca";
                }
                String fConf = ConfigNat.getCurrentConfig().getFichierConf();
                String fAbr = ConfigNat.getCurrentConfig().getRulesFrG2Perso();
                String fCoup = ConfigNat.getCurrentConfig().getDicoCoup();
                String fListeNoir = DialogueIntegral.listeFich;
                FileToolKit.saveStrToFile(ConfigNat.getSvnVersion() + "", FICH_VERSION);
                ArrayList<String> fileArchives = new ArrayList<String>();
                fileArchives.add(FICH_VERSION);
                fileArchives.add(fConf);
                if (new File(fListeNoir).exists()) {
                    fileArchives.add(fListeNoir);
                } else {
                    fileArchives.add("");
                }
                if (!fAbr.equals(ConfigNat.getInstallFolder() + "xsl/dicts/fr-g2.xml")) {
                    fileArchives.add(fAbr);
                } else {
                    fileArchives.add("");
                }
                if (!fCoup.equals(ConfigNat.getDicoCoupDefaut())) {
                    fileArchives.add(fCoup);
                } else {
                    fileArchives.add("");
                }
                byte[] buf = new byte[1024];
                ZipOutputStream out = new ZipOutputStream(new FileOutputStream(fich));
                int i = 1;
                for (String s : fileArchives) {
                    if (!s.equals("")) {
                        FileInputStream in = new FileInputStream(s);
                        out.putNextEntry(new ZipEntry(i + "_" + new File(s).getName()));
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        out.closeEntry();
                        in.close();
                    }
                    i++;
                }
                out.close();
            } catch (IOException e) {
                gestErreur.afficheMessage("Erreur lors de lecture/écriture lors de l'exportation", Nat.LOG_SILENCIEUX);
                if (Nat.LOG_DEBUG == ConfigNat.getCurrentConfig().getNiveauLog()) {
                    e.printStackTrace();
                }
            }
        }
    }
