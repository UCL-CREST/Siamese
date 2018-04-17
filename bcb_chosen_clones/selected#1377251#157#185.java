    public void write(File file) throws Exception {
        if (getGEDCOMFile() != null) {
            size = getGEDCOMFile().length();
            if (!getGEDCOMFile().renameTo(file)) {
                BufferedInputStream in = null;
                BufferedOutputStream out = null;
                try {
                    in = new BufferedInputStream(new FileInputStream(getGEDCOMFile()));
                    out = new BufferedOutputStream(new FileOutputStream(file));
                    IOUtils.copy(in, out);
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
        } else {
            throw new FileUploadException("Cannot write uploaded file to disk!");
        }
    }
