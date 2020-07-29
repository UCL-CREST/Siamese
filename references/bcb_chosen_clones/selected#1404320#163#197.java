    public void write(URL output, String model, String mainResourceClass) throws InfoUnitIOException {
        InfoUnitXMLData iur = new InfoUnitXMLData(STRUCTURE_RDF);
        rdf = iur.load("rdf");
        rdfResource = rdf.ft("resource");
        rdfParseType = rdf.ft("parse type");
        try {
            PrintWriter outw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(output.getFile()), "UTF-8"));
            URL urlModel = new URL(model);
            BufferedReader inr = new BufferedReader(new InputStreamReader(urlModel.openStream()));
            String finalTag = "</" + rdf.ft("main") + ">";
            String line = inr.readLine();
            while (line != null && !line.equalsIgnoreCase(finalTag)) {
                outw.println(line);
                line = inr.readLine();
            }
            inr.close();
            InfoNode nodeType = infoRoot.path(rdf.ft("constraint"));
            String type = null;
            if (nodeType != null) {
                type = nodeType.getValue().toString();
                try {
                    infoRoot.removeChildNode(nodeType);
                } catch (InvalidChildInfoNode error) {
                }
            } else if (mainResourceClass != null) type = mainResourceClass; else type = rdf.ft("description");
            outw.println("   <" + type + " " + rdf.ft("about") + "=\"" + ((infoNamespaces == null) ? infoRoot.getLabel() : infoNamespaces.convertEntity(infoRoot.getLabel().toString())) + "\">");
            Set<InfoNode> nl = infoRoot.getChildren();
            writeNodeList(nl, outw, 5);
            outw.println("   </" + type + ">");
            if (line != null) outw.println(finalTag);
            outw.close();
        } catch (IOException error) {
            throw new InfoUnitIOException(error.getMessage());
        }
    }
