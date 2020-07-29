    public void print(PrintWriter out) {
        out.println("<?xml version=\"1.0\"?>\n" + "<?xml-stylesheet type=\"text/xsl\" href=\"http://www.urbigene.com/foaf/foaf2html.xsl\" ?>\n" + "<rdf:RDF \n" + "xml:lang=\"en\" \n" + "xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"  \n" + "xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\" \n" + "xmlns=\"http://xmlns.com/foaf/0.1/\" \n" + "xmlns:foaf=\"http://xmlns.com/foaf/0.1/\" \n" + "xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n");
        out.println("<!-- generated with SciFoaf http://www.urbigene.com/foaf -->");
        if (this.mainAuthor == null && this.authors.getAuthorCount() > 0) {
            this.mainAuthor = this.authors.getAuthorAt(0);
        }
        if (this.mainAuthor != null) {
            out.println("<foaf:PersonalProfileDocument rdf:about=\"\">\n" + "\t<foaf:primaryTopic rdf:nodeID=\"" + this.mainAuthor.getID() + "\"/>\n" + "\t<foaf:maker rdf:resource=\"mailto:plindenbaum@yahoo.fr\"/>\n" + "\t<dc:title>FOAF for " + XMLUtilities.escape(this.mainAuthor.getName()) + "</dc:title>\n" + "\t<dc:description>\n" + "\tFriend-of-a-Friend description for " + XMLUtilities.escape(this.mainAuthor.getName()) + "\n" + "\t</dc:description>\n" + "</foaf:PersonalProfileDocument>\n\n");
        }
        for (int i = 0; i < this.laboratories.size(); ++i) {
            Laboratory lab = this.laboratories.getLabAt(i);
            out.println("<foaf:Group rdf:ID=\"laboratory_ID" + i + "\" >");
            out.println("\t<foaf:name>" + XMLUtilities.escape(lab.toString()) + "</foaf:name>");
            for (int j = 0; j < lab.getAuthorCount(); ++j) {
                out.println("\t<foaf:member rdf:resource=\"#" + lab.getAuthorAt(j).getID() + "\" />");
            }
            out.println("</foaf:Group>\n\n");
        }
        for (int i = 0; i < this.authors.size(); ++i) {
            Author author = authors.getAuthorAt(i);
            out.println("<foaf:Person rdf:ID=\"" + xmlName(author.getID()) + "\" >");
            out.println("\t<foaf:name>" + xmlName(author.getName()) + "</foaf:name>");
            out.println("\t<foaf:title>Dr</foaf:title>");
            out.println("\t<foaf:family_name>" + xmlName(author.getLastName()) + "</foaf:family_name>");
            if (author.getForeName() != null && author.getForeName().length() > 2) {
                out.println("\t<foaf:firstName>" + xmlName(author.getForeName()) + "</foaf:firstName>");
            }
            String prop = author.getProperty("foaf:mbox");
            if (prop != null) {
                String tokens[] = prop.split("[\t ]+");
                for (int j = 0; j < tokens.length; ++j) {
                    if (tokens[j].trim().length() == 0) continue;
                    if (tokens[j].equals("mailto:")) continue;
                    if (!tokens[j].startsWith("mailto:")) tokens[j] = "mailto:" + tokens[j];
                    try {
                        MessageDigest md = MessageDigest.getInstance("SHA");
                        md.update(tokens[j].getBytes());
                        byte[] digest = md.digest();
                        out.print("\t<foaf:mbox_sha1sum>");
                        for (int k = 0; k < digest.length; k++) {
                            String hex = Integer.toHexString(digest[k]);
                            if (hex.length() == 1) hex = "0" + hex;
                            hex = hex.substring(hex.length() - 2);
                            out.print(hex);
                        }
                        out.println("</foaf:mbox_sha1sum>");
                    } catch (Exception err) {
                        out.println("\t<foaf:mbox rdf:resource=\"" + tokens[j] + "\" />");
                    }
                }
            }
            prop = author.getProperty("foaf:nick");
            if (prop != null) {
                String tokens[] = prop.split("[\t ]+");
                for (int j = 0; j < tokens.length; ++j) {
                    if (tokens[j].trim().length() == 0) continue;
                    out.println("\t<foaf:surname>" + XMLUtilities.escape(tokens[j]) + "</foaf:surname>");
                }
            }
            prop = author.getProperty("foaf:homepage");
            if (prop != null) {
                String tokens[] = prop.split("[\t ]+");
                for (int j = 0; j < tokens.length; ++j) {
                    if (!tokens[j].trim().startsWith("http://")) continue;
                    if (tokens[j].trim().equals("http://")) continue;
                    out.println("\t<foaf:homepage  rdf:resource=\"" + XMLUtilities.escape(tokens[j].trim()) + "\"/>");
                }
            }
            out.println("\t<foaf:publications rdf:resource=\"http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=pubmed&amp;cmd=Search&amp;itool=pubmed_Abstract&amp;term=" + author.getTerm() + "\"/>");
            prop = author.getProperty("foaf:img");
            if (prop != null) {
                String tokens[] = prop.split("[\t ]+");
                for (int j = 0; j < tokens.length; ++j) {
                    if (!tokens[j].trim().startsWith("http://")) continue;
                    if (tokens[j].trim().equals("http://")) continue;
                    out.println("\t<foaf:depiction rdf:resource=\"" + XMLUtilities.escape(tokens[j].trim()) + "\"/>");
                }
            }
            AuthorList knows = this.whoknowwho.getKnown(author);
            for (int j = 0; j < knows.size(); ++j) {
                out.println("\t<foaf:knows rdf:resource=\"#" + xmlName(knows.getAuthorAt(j).getID()) + "\" />");
            }
            Paper publications[] = this.papers.getAuthorPublications(author).toArray();
            if (!(publications.length == 0)) {
                HashSet meshes = new HashSet();
                for (int j = 0; j < publications.length; ++j) {
                    meshes.addAll(publications[j].meshTerms);
                }
                for (Iterator itermesh = meshes.iterator(); itermesh.hasNext(); ) {
                    MeshTerm meshterm = (MeshTerm) itermesh.next();
                    out.println("\t<foaf:interest>\n" + "\t\t<rdf:Description rdf:about=\"" + meshterm.getURL() + "\">\n" + "\t\t\t<dc:title>" + XMLUtilities.escape(meshterm.toString()) + "</dc:title>\n" + "\t\t</rdf:Description>\n" + "\t</foaf:interest>");
                }
            }
            out.println("</foaf:Person>\n\n");
        }
        Paper paperarray[] = this.papers.toArray();
        for (int i = 0; i < paperarray.length; ++i) {
            out.println("<foaf:Document rdf:about=\"http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&amp;db=pubmed&amp;dopt=Abstract&amp;list_uids=" + paperarray[i].getPMID() + "\">");
            out.println("<dc:title>" + XMLUtilities.escape(paperarray[i].getTitle()) + "</dc:title>");
            for (Iterator iter = paperarray[i].authors.iterator(); iter.hasNext(); ) {
                Author author = (Author) iter.next();
                out.println("<dc:author rdf:resource=\"#" + XMLUtilities.escape(author.getID()) + "\"/>");
            }
            out.println("</foaf:Document>");
        }
        out.println("</rdf:RDF>");
    }
