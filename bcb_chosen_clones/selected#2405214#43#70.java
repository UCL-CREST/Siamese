    public String readBaseLib() throws Exception {
        if (_BASE_LIB_JS == null) {
            StringBuffer js = new StringBuffer();
            try {
                URL url = AbstractRunner.class.getResource(_BASELIB_FILENAME);
                if (url != null) {
                    InputStream is = url.openStream();
                    InputStreamReader reader = new InputStreamReader(is);
                    BufferedReader bfReader = new BufferedReader(reader);
                    String tmp = null;
                    do {
                        tmp = bfReader.readLine();
                        if (tmp != null) {
                            js.append(tmp).append('\n');
                        }
                    } while (tmp != null);
                    bfReader.close();
                    reader.close();
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
            _BASE_LIB_JS = js.toString();
        }
        return _BASE_LIB_JS;
    }
