    public boolean storeLogFile(String filePath) {
        try {
            String serverIp = java.util.prefs.Preferences.systemRoot().get("serverurl", "localhost");
            String portno = java.util.prefs.Preferences.systemRoot().get("portno", "8080");
            java.net.URL url = new java.net.URL("http://" + serverIp + ":" + portno + "/newgenlibctxt/LogFileHandler");
            System.out.println("http://" + serverIp + ":" + portno + "/newgenlibctxt/LogFileHandler");
            url.openConnection();
            java.io.InputStream ins = url.openStream();
            System.out.println("size = " + ins.available());
            int size = ins.available();
            BufferedOutputStream bos = null;
            BufferedInputStream bis = new BufferedInputStream(ins);
            ZipInputStream zis = new ZipInputStream(bis);
            ZipEntry entry;
            try {
                while ((entry = zis.getNextEntry()) != null) {
                    System.out.println("Extraction : " + entry);
                    int count;
                    byte data[] = new byte[2048];
                    FileOutputStream fos = new FileOutputStream("server.txt");
                    bos = new BufferedOutputStream(fos, 2048);
                    while ((count = zis.read(data, 0, 2048)) != -1) {
                        bos.write(data, 0, count);
                    }
                }
                bos.flush();
                bos.close();
            } catch (java.io.EOFException e) {
                System.out.println("unexpeted end of ZLIB input stream");
            }
            System.out.println("**************");
            ins.close();
            bis.close();
            zis.close();
            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(filePath + "/server.zip")));
            byte data[] = new byte[2048];
            int count;
            entry = new ZipEntry("server.txt");
            zos.putNextEntry(entry);
            BufferedInputStream bis1 = new BufferedInputStream(new FileInputStream("server.txt"));
            while ((count = bis1.read(data, 0, 2048)) != -1) {
                zos.write(data, 0, count);
            }
            bis1.close();
            zos.closeEntry();
            zos.close();
            System.out.println("zip file created at + " + filePath + "/server.zip");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
