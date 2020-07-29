    private int addIDs(PeakListRow row, String name) {
        {
            BufferedReader in = null;
            try {
                String urlName = "http://gmd.mpimp-golm.mpg.de/search.aspx?query=" + name;
                URL url = new URL(urlName);
                in = new BufferedReader(new InputStreamReader(url.openStream()));
                String inputLine, score = "0";
                while ((inputLine = in.readLine()) != null) {
                    String metaboliteID = "";
                    if (inputLine.contains("href=\"Metabolites/")) {
                        String[] dataScore = inputLine.split("</td><td>");
                        score = dataScore[0].substring(dataScore[0].indexOf("<td>") + 4);
                        metaboliteID = inputLine.substring(inputLine.indexOf("href=\"Metabolites/") + 18, inputLine.indexOf("aspx\">") + 4);
                        urlName = "http://gmd.mpimp-golm.mpg.de/Metabolites/" + metaboliteID;
                        inputLine = in.readLine();
                        inputLine = in.readLine();
                        String[] data = inputLine.split("</td><td>");
                        String molecularWeight = data[data.length - 1].replaceAll("&nbsp;", "");
                        row.setVar(GCGCColumnName.MOLWEIGHT.getSetFunctionName(), molecularWeight);
                        break;
                    } else if (inputLine.contains("href=\"Analytes/")) {
                        String[] dataScore = inputLine.split("</td><td>");
                        score = dataScore[0].substring(dataScore[0].indexOf("<td>") + 4);
                        metaboliteID = inputLine.substring(inputLine.indexOf("href=\"Analytes/") + 15, inputLine.indexOf("aspx\">") + 4);
                        urlName = "http://gmd.mpimp-golm.mpg.de/Analytes/" + metaboliteID;
                        inputLine = in.readLine();
                        inputLine = in.readLine();
                        String[] data = inputLine.split("</td><td>");
                        String molecularWeight = data[data.length - 1].replaceAll("&nbsp;", "");
                        row.setVar(GCGCColumnName.MOLWEIGHT.getSetFunctionName(), molecularWeight);
                        break;
                    } else if (inputLine.contains("href=\"ReferenceSubstances/")) {
                        String[] dataScore = inputLine.split("</td><td>");
                        score = dataScore[0].substring(dataScore[0].indexOf("<td>") + 4);
                        metaboliteID = inputLine.substring(inputLine.indexOf("href=\"ReferenceSubstances/") + 26, inputLine.indexOf("aspx\">") + 4);
                        urlName = "http://gmd.mpimp-golm.mpg.de/ReferenceSubstances/" + metaboliteID;
                        inputLine = in.readLine();
                        inputLine = in.readLine();
                        String[] data = inputLine.split("</td><td>");
                        String molecularWeight = data[data.length - 1].replaceAll("&nbsp;", "");
                        row.setVar(GCGCColumnName.MOLWEIGHT.getSetFunctionName(), molecularWeight);
                        break;
                    }
                }
                in.close();
                urlName = searchMetabolite(urlName);
                if (urlName != null && urlName.contains(".aspx")) {
                    url = new URL(urlName);
                    in = new BufferedReader(new InputStreamReader(url.openStream()));
                    while ((inputLine = in.readLine()) != null) {
                        if (inputLine.contains("<meta http-equiv=\"keywords\" content=")) {
                            String line = inputLine.substring(inputLine.indexOf("<meta http-equiv=\"keywords\" content=") + 37, inputLine.indexOf("\" /></head>"));
                            String[] names = line.split(", ");
                            for (String id : names) {
                                if (id.contains("PubChem")) {
                                    id = id.substring(id.indexOf("PubChem") + 8);
                                    String pubChem = (String) row.getVar(GCGCColumnName.PUBCHEM.getGetFunctionName());
                                    if (pubChem.length() == 0) {
                                        pubChem += id;
                                    } else {
                                        pubChem += ", " + id;
                                    }
                                    row.setVar(GCGCColumnName.PUBCHEM.getSetFunctionName(), pubChem);
                                } else if (id.contains("ChEBI")) {
                                    id = id.substring(id.indexOf("ChEBI:") + 6);
                                    row.setVar(GCGCColumnName.ChEBI.getSetFunctionName(), id);
                                } else if (id.contains("KEGG")) {
                                    id = id.substring(id.indexOf("KEGG:") + 6);
                                    row.setVar(GCGCColumnName.KEGG.getSetFunctionName(), id);
                                } else if (id.contains("CAS")) {
                                    id = id.substring(id.indexOf("CAS:") + 5);
                                    row.setVar(GCGCColumnName.CAS2.getSetFunctionName(), id);
                                } else if (id.contains("ChemSpider") || id.contains("MAPMAN") || id.contains("Beilstein:")) {
                                } else {
                                    String synonym = (String) row.getVar(GCGCColumnName.SYNONYM.getGetFunctionName());
                                    if (synonym.length() == 0) {
                                        synonym += id;
                                    } else {
                                        synonym += " // " + id;
                                    }
                                    synonym = synonym.replaceAll("&amp;#39;", "'");
                                    row.setVar(GCGCColumnName.SYNONYM.getSetFunctionName(), synonym);
                                }
                            }
                            break;
                        }
                    }
                    in.close();
                }
                return Integer.parseInt(score);
            } catch (IOException ex) {
                Logger.getLogger(GetGolmIDsTask.class.getName()).log(Level.SEVERE, null, ex);
                return 0;
            }
        }
    }
