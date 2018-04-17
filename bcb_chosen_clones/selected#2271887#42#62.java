    protected void readURL(URL url) {
        InputStream istream = null;
        InputStreamReader isr = null;
        BufferedReader in = null;
        try {
            istream = url.openStream();
            isr = new InputStreamReader(istream);
            in = new BufferedReader(isr);
            String line = in.readLine();
            while (null != line) {
                System.out.println(line);
                line = in.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtilities.close(in);
            IOUtilities.close(isr);
            IOUtilities.close(istream);
        }
    }
