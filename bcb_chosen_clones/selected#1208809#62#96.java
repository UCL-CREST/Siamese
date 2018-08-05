    private ArrayList<String> getFiles(String date) {
        ArrayList<String> files = new ArrayList<String>();
        String info = "";
        try {
            obtainServerFilesView.setLblProcessText(java.util.ResourceBundle.getBundle("bgpanalyzer/resources/Bundle").getString("ObtainServerFilesView.Label.Progress.Obtaining_Data"));
            URL url = new URL(URL_ROUTE_VIEWS + date + "/");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(false);
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                if (!line.equals("")) info += line + "%";
            }
            obtainServerFilesView.setLblProcessText(java.util.ResourceBundle.getBundle("bgpanalyzer/resources/Bundle").getString("ObtainServerFilesView.Label.Progress.Processing_Data"));
            info = Patterns.removeTags(info);
            StringTokenizer st = new StringTokenizer(info, "%");
            info = "";
            boolean alternador = false;
            int index = 1;
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if (!token.trim().equals("")) {
                    int pos = token.indexOf(".bz2");
                    if (pos != -1) {
                        token = token.substring(1, pos + 4);
                        files.add(token);
                    }
                }
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }
