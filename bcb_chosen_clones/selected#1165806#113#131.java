    private void backupOriginalFile(String myFile) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_S");
        String datePortion = format.format(date);
        try {
            FileInputStream fis = new FileInputStream(myFile);
            FileOutputStream fos = new FileOutputStream(myFile + "-" + datePortion + "_UserID" + ".html");
            FileChannel fcin = fis.getChannel();
            FileChannel fcout = fos.getChannel();
            fcin.transferTo(0, fcin.size(), fcout);
            fcin.close();
            fcout.close();
            fis.close();
            fos.close();
            System.out.println("**** Backup of file made.");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
