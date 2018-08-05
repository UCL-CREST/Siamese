    private void copyFileToDir(MyFile file, MyFile to, wlPanel panel) throws IOException {
        Utilities.print("started copying " + file.getAbsolutePath() + "\n");
        FileOutputStream fos = new FileOutputStream(new File(to.getAbsolutePath()));
        FileChannel foc = fos.getChannel();
        FileInputStream fis = new FileInputStream(new File(file.getAbsolutePath()));
        FileChannel fic = fis.getChannel();
        Date d1 = new Date();
        long amount = foc.transferFrom(fic, rest, fic.size() - rest);
        fic.close();
        foc.force(false);
        foc.close();
        Date d2 = new Date();
        long time = d2.getTime() - d1.getTime();
        double secs = time / 1000.0;
        double rate = amount / secs;
        frame.getStatusArea().append(secs + "s " + "amount: " + Utilities.humanReadable(amount) + " rate: " + Utilities.humanReadable(rate) + "/s\n", "black");
        panel.updateView();
    }
