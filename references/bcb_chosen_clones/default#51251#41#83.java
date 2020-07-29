    public void covertFile(File file) throws IOException {
        if (!file.isFile()) {
            return;
        }
        Reader reader = null;
        OutputStream os = null;
        File newfile = null;
        String filename = file.getName();
        boolean succeed = false;
        try {
            newfile = new File(file.getParentFile(), filename + ".bak");
            reader = new InputStreamReader(new FileInputStream(file), fromEncoding);
            os = new FileOutputStream(newfile);
            IOUtils.copy(reader, os, toEncoding);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Encoding error for file [" + file.getAbsolutePath() + "]");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            file.delete();
            succeed = newfile.renameTo(file);
        } catch (Exception e) {
            throw new IOException("Clear bak error for file [" + file.getAbsolutePath() + "]");
        }
        if (succeed) {
            System.out.println("Changed encoding for file [" + file.getAbsolutePath() + "]");
        }
    }
