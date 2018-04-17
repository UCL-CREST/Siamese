    public String grabId(String itemName) throws Exception {
        StringBuffer modified = new StringBuffer(itemName);
        for (int i = 0; i <= modified.length() - 1; i++) {
            char ichar = modified.charAt(i);
            if (ichar == ' ') modified = modified.replace(i, i + 1, "+");
        }
        itemName = modified.toString();
        try {
            URL url = new URL(searchURL + itemName);
            InputStream urlStream = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlStream, "UTF-8"));
            while (reader.ready()) {
                String htmlLine = reader.readLine();
                int indexOfSearchStart = htmlLine.indexOf(searchForItemId);
                if (indexOfSearchStart != -1) {
                    int idStart = htmlLine.indexOf("=", indexOfSearchStart);
                    idStart++;
                    int idEnd = htmlLine.indexOf("'", idStart);
                    id = htmlLine.substring(idStart, idEnd);
                }
            }
            if (id == "") return null; else return id;
        } catch (Exception ex) {
            System.out.println("Exception in lookup: " + ex);
            throw (ex);
        }
    }
