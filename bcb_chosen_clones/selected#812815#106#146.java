    public void extractProfile(String parentDir, String fileName, String profileName) {
        try {
            byte[] buf = new byte[1024];
            ZipInputStream zipinputstream = null;
            ZipEntry zipentry;
            if (createProfileDirectory(profileName, parentDir)) {
                debug("the profile directory created .starting the profile extraction");
                String profilePath = parentDir + File.separator + fileName;
                zipinputstream = new ZipInputStream(new FileInputStream(profilePath));
                zipentry = zipinputstream.getNextEntry();
                while (zipentry != null) {
                    String entryName = zipentry.getName();
                    int n;
                    FileOutputStream fileoutputstream;
                    File newFile = new File(entryName);
                    String directory = newFile.getParent();
                    if (directory == null) {
                        if (newFile.isDirectory()) break;
                    }
                    fileoutputstream = new FileOutputStream(parentDir + File.separator + profileName + File.separator + newFile.getName());
                    while ((n = zipinputstream.read(buf, 0, 1024)) > -1) fileoutputstream.write(buf, 0, n);
                    fileoutputstream.close();
                    zipinputstream.closeEntry();
                    zipentry = zipinputstream.getNextEntry();
                }
                zipinputstream.close();
                debug("deleting the profile.zip file");
                File newFile = new File(profilePath);
                if (newFile.delete()) {
                    debug("the " + "[" + profilePath + "]" + " deleted successfully");
                } else {
                    debug("profile" + "[" + profilePath + "]" + "deletion fail");
                    throw new IllegalArgumentException("Error: deletion error!");
                }
            } else {
                debug("error creating the profile directory");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
