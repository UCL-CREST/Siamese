    private void searchfiles(File tolookin, String usersid, ZipOutputStream zipoutputstream, StringBuffer attachedfiles, File dirtoignore) throws IOException {
        File[] filesindir = tolookin.listFiles();
        if (filesindir != null) {
            for (int i = 0; i < filesindir.length; i++) {
                if (filesindir[i].isDirectory() && filesindir[i].getName().indexOf(usersid) != 0 && !filesindir[i].getName().equals(".") && !filesindir[i].getName().equals("..") && !filesindir[i].equals(dirtoignore)) {
                    searchfiles(filesindir[i], usersid, zipoutputstream, attachedfiles, dirtoignore);
                } else if (filesindir[i].isDirectory() && filesindir[i].getName().indexOf(usersid) == 0) {
                    if ((System.currentTimeMillis() - filesindir[i].lastModified()) > 300000) addfiles(filesindir[i], zipoutputstream, attachedfiles);
                } else {
                    if (filesindir[i].getName().indexOf(usersid) == 0) {
                        zipoutputstream.putNextEntry(new ZipEntry(filesindir[i].getName()));
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
    }
