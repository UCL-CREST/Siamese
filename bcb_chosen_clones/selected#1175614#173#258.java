    public static void exportDB(String input, String output) {
        try {
            Class.forName("org.sqlite.JDBC");
            String fileName = input + File.separator + G.databaseName;
            File dataBase = new File(fileName);
            if (!dataBase.exists()) {
                JOptionPane.showMessageDialog(null, "No se encuentra el fichero DB", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                G.conn = DriverManager.getConnection("jdbc:sqlite:" + fileName);
                HashMap<Integer, String> languageIDs = new HashMap<Integer, String>();
                HashMap<Integer, String> typeIDs = new HashMap<Integer, String>();
                long tiempoInicio = System.currentTimeMillis();
                Element dataBaseXML = new Element("database");
                Element languages = new Element("languages");
                Statement stat = G.conn.createStatement();
                ResultSet rs = stat.executeQuery("select * from language order by id");
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    languageIDs.put(id, name);
                    Element language = new Element("language");
                    language.setText(name);
                    languages.addContent(language);
                }
                dataBaseXML.addContent(languages);
                rs = stat.executeQuery("select * from type order by id");
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    typeIDs.put(id, name);
                }
                rs = stat.executeQuery("select distinct name from main order by name");
                while (rs.next()) {
                    String name = rs.getString("name");
                    Element image = new Element("image");
                    image.setAttribute("id", name);
                    Statement stat2 = G.conn.createStatement();
                    ResultSet rs2 = stat2.executeQuery("select distinct idL from main where name = \"" + name + "\" order by idL");
                    while (rs2.next()) {
                        int idL = rs2.getInt("idL");
                        Element language = new Element("language");
                        language.setAttribute("id", languageIDs.get(idL));
                        Statement stat3 = G.conn.createStatement();
                        ResultSet rs3 = stat3.executeQuery("select * from main where name = \"" + name + "\" and idL = " + idL + " order by idT");
                        while (rs3.next()) {
                            int idT = rs3.getInt("idT");
                            String word = rs3.getString("word");
                            Element wordE = new Element("word");
                            wordE.setAttribute("type", typeIDs.get(idT));
                            wordE.setText(word);
                            language.addContent(wordE);
                            String pathSrc = input + File.separator + name.substring(0, 1).toUpperCase() + File.separator + name;
                            String pathDst = output + File.separator + name;
                            try {
                                FileChannel srcChannel = new FileInputStream(pathSrc).getChannel();
                                FileChannel dstChannel = new FileOutputStream(pathDst).getChannel();
                                dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
                                srcChannel.close();
                                dstChannel.close();
                            } catch (IOException exc) {
                                System.out.println(exc.getMessage());
                                System.out.println(exc.toString());
                            }
                        }
                        rs3.close();
                        stat3.close();
                        image.addContent(language);
                    }
                    rs2.close();
                    stat2.close();
                    dataBaseXML.addContent(image);
                }
                rs.close();
                stat.close();
                XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
                FileOutputStream f = new FileOutputStream(output + File.separator + G.imagesName);
                out.output(dataBaseXML, f);
                f.flush();
                f.close();
                long totalTiempo = System.currentTimeMillis() - tiempoInicio;
                System.out.println("El tiempo total es :" + totalTiempo / 1000 + " segundos");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
