    private void addfiles(File toadd, ZipOutputStream zipoutputstream, StringBuffer attachedfiles) throws IOException {
        File[] filesindir = toadd.listFiles();
        if (filesindir != null) {
            for (int i = 0; i < filesindir.length; i++) {
                if (filesindir[i].isDirectory() && !filesindir[i].getName().equals(".") && !filesindir[i].getName().equals("..")) {
                    addfiles(filesindir[i], zipoutputstream, attachedfiles);
                } else if (filesindir[i].isFile()) {
                    zipoutputstream.putNextEntry(new ZipEntry(toadd + "/" + filesindir[i].getName()));
                    FileInputStream fis = new FileInputStream(filesindir[i]);
                    int read = 0;
                    while ((read = fis.read()) != -1) {
                        zipoutputstream.write(read);
                    }
                    attachedfiles.append(filesindir[i] + "; ");
                    zipoutputstream.closeEntry();
                }
            }
        }
    }
