    private void copyFile(File dir, File fileToAdd) {
        try {
            byte[] readBuffer = new byte[1024];
            File file = new File(dir.getCanonicalPath() + File.separatorChar + fileToAdd.getName());
            if (file.createNewFile()) {
                FileInputStream fis = new FileInputStream(fileToAdd);
                FileOutputStream fos = new FileOutputStream(file);
                int bytesRead;
                do {
                    bytesRead = fis.read(readBuffer);
                    fos.write(readBuffer, 0, bytesRead);
                } while (bytesRead == 0);
                fos.flush();
                fos.close();
                fis.close();
            } else {
                logger.severe("unable to create file:" + file.getAbsolutePath());
            }
        } catch (IOException ioe) {
            logger.severe("unable to create file:" + ioe);
        }
    }
