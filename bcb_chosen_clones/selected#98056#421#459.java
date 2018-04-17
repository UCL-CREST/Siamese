    public void setTableBraille(String tableBraille, boolean sys) {
        fiConf.setProperty(OptNames.fi_braille_table, tableBraille);
        fiConf.setProperty(OptNames.fi_is_sys_braille_table, Boolean.toString(sys));
        FileChannel in = null;
        FileChannel out = null;
        try {
            String fichTable;
            if (!(tableBraille.endsWith(".ent"))) {
                tableBraille = tableBraille + ".ent";
            }
            if (sys) {
                fichTable = ConfigNat.getInstallFolder() + "xsl/tablesBraille/" + tableBraille;
            } else {
                fichTable = ConfigNat.getUserBrailleTableFolder() + tableBraille;
            }
            in = new FileInputStream(fichTable).getChannel();
            out = new FileOutputStream(getUserBrailleTableFolder() + "Brltab.ent").getChannel();
            in.transferTo(0, in.size(), out);
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String fichTable;
            if (sys) {
                fichTable = ConfigNat.getInstallFolder() + "/xsl/tablesEmbosseuse/" + tableBraille;
            } else {
                fichTable = ConfigNat.getUserEmbossTableFolder() + "/" + tableBraille;
            }
            in = new FileInputStream(fichTable).getChannel();
            out = new FileOutputStream(ConfigNat.getUserTempFolder() + "Table_pour_chaines.ent").getChannel();
            in.transferTo(0, in.size(), out);
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
