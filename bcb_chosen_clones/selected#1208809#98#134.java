    private ArrayList<String> getYearsAndMonths() {
        String info = "";
        ArrayList<String> items = new ArrayList<String>();
        try {
            obtainServerFilesView.setLblProcessText(java.util.ResourceBundle.getBundle("bgpanalyzer/resources/Bundle").getString("ObtainServerFilesView.Label.Progress.Obtaining_Data"));
            URL url = new URL(URL_ROUTE_VIEWS);
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
                    int pos = token.indexOf("/");
                    if (pos != -1) {
                        token = token.substring(1, pos);
                        if (Patterns.hasFormatYYYYdotMM(token)) {
                            items.add(token);
                        }
                    }
                }
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }
