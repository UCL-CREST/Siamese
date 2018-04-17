    public static void writeFileType(String uriFile, String outputfile, int num) {
        BufferedWriter writer = null;
        String uri = null;
        try {
            int counter = 1;
            writer = new BufferedWriter(new FileWriter(outputfile));
            BufferedReader reader = new BufferedReader(new FileReader(uriFile));
            uri = null;
            while (counter < num) {
                uri = reader.readLine();
                counter++;
            }
            while ((uri = reader.readLine()) != null) {
                try {
                    System.err.println("working on the [" + counter + "]th document.");
                    counter++;
                    URL url = new URL(uri);
                    URLConnection myConnection = url.openConnection();
                    BufferedReader myReader = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
                    String line = null;
                    boolean hasOWL = false;
                    boolean hasRDFS = false;
                    boolean hasRDF = false;
                    int linecount = 0;
                    while ((line = myReader.readLine()) != null) {
                        if (line.indexOf("http://www.w3.org/2002/07/owl") != -1) hasOWL = true; else if (line.indexOf("http://www.w3.org/2000/01/rdf-schema") != -1) hasRDFS = true; else if (line.indexOf("http://www.w3.org/1999/02/22-rdf-syntax-ns") != -1) hasRDF = true;
                        linecount++;
                        if (linecount > 100) break;
                    }
                    if (hasOWL) writer.write(uri + "\t" + OWL); else if (hasRDFS) writer.write(uri + "\t" + RDFS); else if (hasRDF) writer.write(uri + "\t" + RDF); else writer.write(uri + "\t" + UNKNOWN);
                    writer.newLine();
                    writer.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        writer.write(uri + "\t" + BROKEN);
                        writer.newLine();
                        writer.flush();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
