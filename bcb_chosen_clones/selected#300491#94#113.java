    private boolean copyAvecProgressNIO(File sRC2, File dEST2, JProgressBar progressEnCours) throws IOException {
        boolean resultat = false;
        FileInputStream fis = new FileInputStream(sRC2);
        FileOutputStream fos = new FileOutputStream(dEST2);
        java.nio.channels.FileChannel channelSrc = fis.getChannel();
        java.nio.channels.FileChannel channelDest = fos.getChannel();
        progressEnCours.setValue(0);
        progressEnCours.setString(sRC2 + " : 0 %");
        channelSrc.transferTo(0, channelSrc.size(), channelDest);
        progressEnCours.setValue(100);
        progressEnCours.setString(sRC2 + " : 100 %");
        if (channelSrc.size() == channelDest.size()) {
            resultat = true;
        } else {
            resultat = false;
        }
        fis.close();
        fos.close();
        return (resultat);
    }
