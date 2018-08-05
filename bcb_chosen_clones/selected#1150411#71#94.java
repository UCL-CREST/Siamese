    protected BufferedReader getDataReader() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            String line;
            URL url = new URL(this.catalog.getCatalogURL());
            Debug.output("Catalog URL:" + url.toString());
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            File dir = (File) SessionHandler.getServletContext().getAttribute("javax.servlet.context.tempdir");
            File temp = new File(dir, TEMP);
            Debug.output("Temp file:" + temp.toString());
            out = new PrintWriter(new BufferedWriter(new FileWriter(temp)));
            while ((line = in.readLine()) != null) {
                out.println(line);
            }
            Debug.output("Temp file size:" + temp.length());
            return new BufferedReader(new FileReader(temp));
        } catch (IOException e) {
            throw new SeismoException(e);
        } finally {
            Util.close(in);
            Util.close(out);
        }
    }
