    private Face(String font) throws IOException {
        characterWidths = new double[256];
        StringBuffer sb = new StringBuffer();
        sb.append('/');
        sb.append(Constants.FONTS_DIR);
        sb.append('/');
        sb.append(font);
        sb.append(Constants.CHAR_WIDTHS_SUFFIX);
        String path = sb.toString();
        URL url = getClass().getResource(path);
        InputStream is = url.openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        int pos = 0;
        String width = br.readLine();
        while (width != null && pos < 256) {
            characterWidths[pos] = Double.parseDouble(width);
            pos++;
            width = br.readLine();
        }
    }
