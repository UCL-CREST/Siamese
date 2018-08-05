    public static void Zimmer_hinzu() {
        String text = GUI_main.jText_Admin_Zimmernummer.getText();
        text = text.trim();
        text = text.replaceAll(";", ",");
        ArrayList List = new ArrayList();
        boolean error = false;
        Pattern p1 = Pattern.compile("[0-9]+");
        Pattern p2 = Pattern.compile("[0-9]+[-][0-9]+");
        Matcher m1 = p1.matcher(text);
        Matcher m2 = p2.matcher(text);
        while (m1.find()) {
            List.add(Integer.valueOf(text.substring(m1.start(), m1.end())));
        }
        while (m2.find()) {
            String sub = text.substring(m2.start(), m2.end());
            Matcher m3 = p1.matcher(sub);
            ArrayList MatcherList = new ArrayList();
            while (m3.find()) {
                MatcherList.add(Integer.valueOf(sub.substring(m3.start(), m3.end())));
            }
            for (int i = (Integer) MatcherList.get(0); i != (Integer) MatcherList.get(1); i++) {
                if (!List.contains(i)) List.add(i);
            }
        }
        if (!error) {
            for (int i = 0; i < List.size(); i++) {
                int j = (Integer) List.get(i);
                Zimmer neuesZ = new Zimmer(j, false);
                neuesZ.setKategorie(Integer.valueOf(String.valueOf(Zimmer.Kategorie_IDs.get(GUI_main.jCombo_Admin_Zimmer_Kategorie.getSelectedIndex()))));
                neuesZ.setEtage(Integer.valueOf(GUI_main.jText_Admin_Etage.getText()));
                if (GUI_main.jCheckBox_Admin_Zimmer_gesperrt.isSelected()) {
                    neuesZ.setGesperrt(1);
                } else neuesZ.setGesperrt(0);
                neuesZ.insertAtDB();
            }
            Zimmer.refreshData();
        }
    }
