    public void loadXML(URL flux, int status, File file) {
        try {
            SAXBuilder sbx = new SAXBuilder();
            try {
                if (file.exists()) {
                    file.delete();
                }
                if (!file.exists()) {
                    URLConnection conn = flux.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(10000);
                    InputStream is = conn.getInputStream();
                    OutputStream out = new FileOutputStream(file);
                    byte buf[] = new byte[1024];
                    int len;
                    while ((len = is.read(buf)) > 0) out.write(buf, 0, len);
                    out.close();
                    is.close();
                }
            } catch (Exception e) {
                Log.e(Constants.PROJECT_TAG, "Exeption retrieving XML", e);
            }
            try {
                document = sbx.build(new FileInputStream(file));
            } catch (Exception e) {
                Log.e(Constants.PROJECT_TAG, "xml error ", e);
            }
        } catch (Exception e) {
            Log.e(Constants.PROJECT_TAG, "TsukiQueryError", e);
        }
        if (document != null) {
            root = document.getRootElement();
            PopulateDatabase(root, status);
        }
    }
