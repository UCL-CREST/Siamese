    private boolean enregistreToi() {
        PrintWriter lEcrivain;
        String laDest = "./img_types/" + sonImage;
        if (!new File("./img_types").exists()) {
            new File("./img_types").mkdirs();
        }
        try {
            FileChannel leFicSource = new FileInputStream(sonFichier).getChannel();
            FileChannel leFicDest = new FileOutputStream(laDest).getChannel();
            leFicSource.transferTo(0, leFicSource.size(), leFicDest);
            leFicSource.close();
            leFicDest.close();
            lEcrivain = new PrintWriter(new FileWriter(new File("bundll/types.jay"), true));
            lEcrivain.println(sonNom);
            lEcrivain.println(sonImage);
            if (sonOptionRadio1.isSelected()) {
                lEcrivain.println("0:?");
            }
            if (sonOptionRadio2.isSelected()) {
                lEcrivain.println("1:" + JOptionPane.showInputDialog(null, "Vous avez choisis de rendre ce terrain difficile � franchir.\nVeuillez en indiquer la raison.", "Demande de pr�cision", JOptionPane.INFORMATION_MESSAGE));
            }
            if (sonOptionRadio3.isSelected()) {
                lEcrivain.println("2:?");
            }
            lEcrivain.close();
            return true;
        } catch (Exception lException) {
            return false;
        }
    }
