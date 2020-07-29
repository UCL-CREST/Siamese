    private void publishToWeb(String sourcefile, String type) {
        String diff = sourcefile.substring(DOCROOT_EXT.length());
        String webFolder = DOCROOT_WEB + "/" + diff;
        if (type.equals("DELETE")) {
            File f = new File(webFolder);
            if (f != null) f.delete();
        } else if (type.equals("COPY")) {
            File sFile = new File(sourcefile);
            if (sFile.isDirectory()) {
                sFile.mkdir();
                for (File oneFile : sFile.listFiles()) {
                    String sourceDest = oneFile.getAbsolutePath();
                    String dest = DOCROOT_WEB + sourceDest.substring(DOCROOT_EXT.length());
                    File destF = new File(dest);
                    copyFile(oneFile, destF);
                }
            } else {
                File destF = new File(DOCROOT_WEB + sourcefile.substring(DOCROOT_EXT.length()));
                copyFile(sFile, destF);
            }
        }
    }
