    private List<String> getTaxaList() {
        List<String> taxa = new Vector<String>();
        String domain = m_domain.getStringValue();
        String id = "";
        if (domain.equalsIgnoreCase("Eukaryota")) id = "eukaryota";
        try {
            URL url = new URL("http://www.ebi.ac.uk/genomes/" + id + ".details.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String link = "";
            String key = "";
            String name = "";
            int counter = 0;
            String line = "";
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] st = line.split("\t");
                ena_details ena = new ena_details(st[0], st[1], st[2], st[3], st[4]);
                ENADataHolder.instance().put(ena.desc, ena);
                taxa.add(ena.desc);
            }
            reader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return taxa;
    }
