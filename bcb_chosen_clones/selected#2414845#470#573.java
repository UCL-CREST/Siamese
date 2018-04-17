    public static void writeZip(OutputStream os, Hashtable fileList, InputStream cic, String newClassName, boolean instOnly) {
        if (cic != null) {
            Main.log.println("adding class file ");
            try {
                ClassRenamer.copyClass(cic, os, newClassName);
            } catch (IOException e) {
                Main.log.println("Can not write zip : " + e);
                return;
            }
        }
        ZipOutputStream myZip = new ZipOutputStream(os);
        myZip.setLevel(9);
        try {
            Main.log.println("add installer properties");
            ZipEntry ze = new ZipEntry("installer.properties");
            myZip.putNextEntry(ze);
            saveProperties(myZip);
            myZip.closeEntry();
        } catch (IOException e) {
            Main.log.println("Can not write zip : " + e);
            return;
        }
        Vector values = new Vector(fileList.values());
        java.util.Collections.sort(values);
        Enumeration en = values.elements();
        while (en.hasMoreElements()) {
            FileRec rec = (FileRec) en.nextElement();
            MessageDigest sha = null;
            if (rec.getFileSize() < 0) {
                Main.log.println(">>>>\nOoops :  can not find " + rec.getSourceName() + "\n>>>> skip this file");
                continue;
            }
            try {
                sha = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                System.err.println("can't get instance of an SHA-1 Digest Object");
                sha = null;
            }
            byte[] buffer = new byte[4096];
            if (instOnly && rec.getFromURL()) {
                try {
                    Main.log.println("Calculate message digest for " + rec.getSourceName());
                    FileInputStream is = new FileInputStream(rec.getSourceName());
                    long size = 0;
                    int bytes = 0;
                    while ((bytes = is.read(buffer)) > 0) {
                        if (sha != null) sha.update(buffer, 0, bytes);
                        size += bytes;
                    }
                    is.close();
                    rec.setSize(size);
                    byte[] hash = sha.digest();
                    rec.setDigest(hash);
                } catch (IOException e) {
                    Main.log.println("Can not calculate message digest : " + e);
                }
                continue;
            }
            Main.log.println("adding " + rec.getSourceName());
            ZipEntry ze;
            if (rec.getLocation().equals("_top_")) {
                ze = new ZipEntry(rec.getTargetName());
            } else {
                ze = new ZipEntry(rec.getLocation() + "/" + rec.getTargetName());
            }
            try {
                FileInputStream is = new FileInputStream(rec.getSourceName());
                myZip.putNextEntry(ze);
                long size = 0;
                int bytes = 0;
                while ((bytes = is.read(buffer)) > 0) {
                    if (sha != null) sha.update(buffer, 0, bytes);
                    myZip.write(buffer, 0, bytes);
                    size += bytes;
                }
                is.close();
                myZip.closeEntry();
                rec.setSize(size);
                byte[] hash = sha.digest();
                rec.setDigest(hash);
            } catch (IOException e) {
                Main.log.println("Can not write zip : " + e);
            }
        }
        Main.log.println("adding filelist");
        try {
            ZipEntry ze = new ZipEntry("installer.filelist");
            myZip.putNextEntry(ze);
            PrintStream ps = new PrintStream(myZip);
            en = values.elements();
            while (en.hasMoreElements()) {
                FileRec rec = (FileRec) en.nextElement();
                ps.println(rec.asListEntry());
            }
            myZip.closeEntry();
        } catch (IOException e) {
            Main.log.println("Can not write filelist : " + e);
        }
        try {
            myZip.close();
        } catch (IOException e) {
            Main.log.println("Can not close zip : " + e);
        }
    }
