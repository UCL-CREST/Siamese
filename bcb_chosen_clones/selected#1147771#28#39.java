    public String[] retrieveFasta(String id) throws Exception {
        URL url = new URL("http://www.ebi.ac.uk/ena/data/view/" + id + "&display=fasta");
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String header = reader.readLine();
        StringBuffer seq = new StringBuffer();
        String line = "";
        while ((line = reader.readLine()) != null) {
            seq.append(line);
        }
        reader.close();
        return new String[] { header, seq.toString() };
    }
