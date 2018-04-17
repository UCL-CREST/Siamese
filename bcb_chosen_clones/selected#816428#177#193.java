    public void extractResourceToFile(String resourcePath, File dest) {
        InputStream in = getClass().getResourceAsStream(resourcePath);
        try {
            FileOutputStream out = FileUtils.openOutputStream(dest);
            try {
                IOUtils.copy(in, out);
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
