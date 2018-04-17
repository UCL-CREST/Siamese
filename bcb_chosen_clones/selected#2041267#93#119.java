    private List<String[]> retrieveData(String query) {
        List<String[]> data = new Vector<String[]>();
        query = query.replaceAll("\\s", "+");
        String q = "http://www.uniprot.org/uniprot/?query=" + query + "&format=tab&columns=id,protein%20names,organism";
        try {
            URL url = new URL(q);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = "";
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] st = line.split("\t");
                String[] d = new String[] { st[0], st[1], st[2] };
                data.add(d);
            }
            reader.close();
            if (data.size() == 0) {
                JOptionPane.showMessageDialog(this, "No data found for query");
            }
        } catch (MalformedURLException e) {
            System.err.println("Query " + q + " caused exception: ");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Query " + q + " caused exception: ");
            e.printStackTrace();
        }
        return data;
    }
