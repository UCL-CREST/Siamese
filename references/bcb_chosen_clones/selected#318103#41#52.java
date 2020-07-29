    public void sendResponse(DjdocRequest req, HttpServletResponse res) throws IOException {
        File file = (File) req.getResult();
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            IOUtils.copy(in, res.getOutputStream());
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
