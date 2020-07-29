    private boolean copyFile(File file) throws Exception {
        destination = new File(ServiceLocator.getSqliteDir(), file.getName());
        logger.debug("Writing to: " + destination);
        if (destination.exists()) {
            Frame.showMessage(ServiceLocator.getText("Error.file.exists") + file.getName(), null);
            logger.debug("File already exists: " + file);
            return false;
        }
        destination.createNewFile();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(file);
            out = new FileOutputStream(destination);
            int read = 0;
            byte[] buffer = new byte[2048];
            while ((read = in.read(buffer)) > 0) {
                out.write(buffer, 0, read);
            }
        } finally {
            if (in != null) in.close();
            if (out != null) out.close();
        }
        return true;
    }
