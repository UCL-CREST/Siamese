    public void backupXML() {
        try {
            TimeStamp timeStamp = new TimeStamp();
            String fnameIn = this.fnameXML();
            String pathBackup = this.pathXML + "\\Backup\\";
            String fnameOut = fnameIn.substring(fnameIn.indexOf(this.fname), fnameIn.length());
            fnameOut = fnameOut.substring(0, fnameOut.indexOf("xml"));
            fnameOut = pathBackup + fnameOut + timeStamp.now("yyyyMMdd-kkmmss") + ".xml";
            System.out.println("fnameIn: " + fnameIn);
            System.out.println("fnameOut: " + fnameOut);
            FileChannel in = new FileInputStream(fnameIn).getChannel();
            FileChannel out = new FileOutputStream(fnameOut).getChannel();
            in.transferTo(0, in.size(), out);
        } catch (Exception e) {
            central.inform("ORM.backupXML: " + e.toString());
        }
    }
