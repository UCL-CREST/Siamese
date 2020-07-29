    public JythonWrapperAction(AActionBO.ActionDTO dto, URL url) throws IOException {
        super(dto);
        InputStream in = url.openStream();
        InputStreamReader rin = new InputStreamReader(in);
        BufferedReader reader = new BufferedReader(rin);
        StringBuffer s = new StringBuffer();
        String str;
        while ((str = reader.readLine()) != null) {
            s.append(str);
            s.append("\n");
        }
        in.close();
        script = s.toString();
    }
