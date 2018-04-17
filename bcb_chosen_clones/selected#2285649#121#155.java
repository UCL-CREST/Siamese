    public boolean copyFile(File destinationFolder, File fromFile) {
        boolean result = false;
        String toFileName = destinationFolder.getAbsolutePath() + "/" + fromFile.getName();
        File toFile = new File(toFileName);
        FileInputStream from = null;
        FileOutputStream to = null;
        try {
            from = new FileInputStream(fromFile);
            to = new FileOutputStream(toFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = from.read(buffer)) != -1) to.write(buffer, 0, bytesRead);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (from != null) {
                try {
                    from.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                if (to != null) {
                    try {
                        to.close();
                        result = true;
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
            }
        }
        return result;
    }
