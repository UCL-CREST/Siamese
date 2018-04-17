    public void extractFrinika() throws Exception {
        FileInputStream fis = new FileInputStream(frinikaFile);
        progressBar.setIndeterminate(true);
        ZipInputStream zis = new ZipInputStream(fis);
        ZipEntry ze = zis.getNextEntry();
        while (ze != null) {
            showMessage("Extracting: " + ze.getName());
            File file = new File(installDirName + "/" + ze.getName());
            if (ze.isDirectory()) file.mkdir(); else {
                FileOutputStream fos = new FileOutputStream(file);
                byte[] b = new byte[BUFSIZE];
                int c;
                while ((c = zis.read(b)) != -1) fos.write(b, 0, c);
                fos.close();
            }
            ze = zis.getNextEntry();
        }
    }
