    private void download(String fileName) {
        String filePath = Activator.showSaveDialog(fileName, new String[] { ".xls" });
        if (filePath != null) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = this.getClass().getResourceAsStream("/" + fileName);
                out = new FileOutputStream(filePath);
                IOUtils.copy(in, out);
            } catch (IOException ioe) {
                Activator.showExceptionDialog(ioe);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e1) {
                        Activator.showExceptionDialog(e1);
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e1) {
                        Activator.showExceptionDialog(e1);
                    }
                }
            }
        }
    }
