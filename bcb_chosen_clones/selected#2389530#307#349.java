    protected void copyResource(File source, File destination) {
        if (!(source.equals((temRep)))) {
            if (source.isDirectory()) {
                File directory = destination;
                directory.mkdir();
                File[] files = source.listFiles();
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        File f = files[i];
                        destination = new File(directory, f.getName());
                        copyResource(f, destination);
                    }
                }
            } else {
                FileOutputStream outStream = null;
                FileInputStream inStream = null;
                if (destination.exists()) {
                    deleteResource(destination);
                }
                try {
                    destination.createNewFile();
                    inStream = new FileInputStream(source);
                    outStream = new FileOutputStream(destination);
                    byte buffer[] = new byte[512 * 1024];
                    int nb;
                    while ((nb = inStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, nb);
                    }
                } catch (java.io.FileNotFoundException f) {
                } catch (java.io.IOException e) {
                } finally {
                    try {
                        if (inStream != null) inStream.close();
                    } catch (Exception e) {
                    }
                    try {
                        if (outStream != null) outStream.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
