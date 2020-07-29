    public static void importDB(String input, String output) {
        try {
            Class.forName("org.sqlite.JDBC");
            String fileName = output + File.separator + G.databaseName;
            File dataBase = new File(fileName);
            if (!dataBase.exists()) {
                G.conn = DriverManager.getConnection("jdbc:sqlite:" + fileName);
                createTablesDB();
            } else G.conn = DriverManager.getConnection("jdbc:sqlite:" + fileName);
            long tiempoInicio = System.currentTimeMillis();
            String directoryPath = input + File.separator;
            File myDirectory = new File(directoryPath);
            String[] list = myDirectory.list();
            File fileXML = new File(input + File.separator + G.imagesName);
            if (!fileXML.exists()) {
                JOptionPane.showMessageDialog(null, "No se encuentra el fichero XML", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                SAXBuilder builder = new SAXBuilder(false);
                Document docXML = builder.build(fileXML);
                Element root = docXML.getRootElement();
                List images = root.getChildren("image");
                Iterator j = images.iterator();
                List<Element> globalLanguages = root.getChild("languages").getChildren("language");
                Iterator<Element> langsI = globalLanguages.iterator();
                HashMap<String, Integer> languageIDs = new HashMap<String, Integer>();
                HashMap<String, Integer> typeIDs = new HashMap<String, Integer>();
                Element e;
                int i = 0;
                int contTypes = 0;
                int contImages = 0;
                while (langsI.hasNext()) {
                    e = langsI.next();
                    languageIDs.put(e.getText(), i);
                    PreparedStatement stmt = G.conn.prepareStatement("INSERT OR IGNORE INTO language (id,name) VALUES (?,?)");
                    stmt.setInt(1, i);
                    stmt.setString(2, e.getText());
                    stmt.executeUpdate();
                    stmt.close();
                    i++;
                }
                G.conn.setAutoCommit(false);
                while (j.hasNext()) {
                    Element image = (Element) j.next();
                    String id = image.getAttributeValue("id");
                    List languages = image.getChildren("language");
                    Iterator k = languages.iterator();
                    if (exists(list, id)) {
                        String pathSrc = directoryPath.concat(id);
                        String pathDst = output + File.separator + id.substring(0, 1).toUpperCase() + File.separator;
                        String folder = output + File.separator + id.substring(0, 1).toUpperCase();
                        String pathDstTmp = pathDst.concat(id);
                        String idTmp = id;
                        File testFile = new File(pathDstTmp);
                        int cont = 1;
                        while (testFile.exists()) {
                            idTmp = id.substring(0, id.lastIndexOf('.')) + '_' + cont + id.substring(id.lastIndexOf('.'), id.length());
                            pathDstTmp = pathDst + idTmp;
                            testFile = new File(pathDstTmp);
                            cont++;
                        }
                        pathDst = pathDstTmp;
                        id = idTmp;
                        File newDirectoryFolder = new File(folder);
                        if (!newDirectoryFolder.exists()) {
                            newDirectoryFolder.mkdirs();
                        }
                        try {
                            FileChannel srcChannel = new FileInputStream(pathSrc).getChannel();
                            FileChannel dstChannel = new FileOutputStream(pathDst).getChannel();
                            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
                            srcChannel.close();
                            dstChannel.close();
                        } catch (IOException exc) {
                            System.out.println(exc.toString());
                        }
                        while (k.hasNext()) {
                            Element languageElement = (Element) k.next();
                            String language = languageElement.getAttributeValue("id");
                            List words = languageElement.getChildren("word");
                            Iterator l = words.iterator();
                            while (l.hasNext()) {
                                Element wordElement = (Element) l.next();
                                String type = wordElement.getAttributeValue("type");
                                if (!typeIDs.containsKey(type)) {
                                    typeIDs.put(type, contTypes);
                                    PreparedStatement stmt = G.conn.prepareStatement("INSERT OR IGNORE INTO type (id,name) VALUES (?,?)");
                                    stmt.setInt(1, contTypes);
                                    stmt.setString(2, type);
                                    stmt.executeUpdate();
                                    stmt.close();
                                    contTypes++;
                                }
                                PreparedStatement stmt = G.conn.prepareStatement("INSERT OR IGNORE INTO main (word, idL, idT, name, nameNN) VALUES (?,?,?,?,?)");
                                stmt.setString(1, wordElement.getText().toLowerCase());
                                stmt.setInt(2, languageIDs.get(language));
                                stmt.setInt(3, typeIDs.get(type));
                                stmt.setString(4, id);
                                stmt.setString(5, id);
                                stmt.executeUpdate();
                                stmt.close();
                                if (contImages == 5000) {
                                    G.conn.commit();
                                    contImages = 0;
                                } else contImages++;
                            }
                        }
                    } else {
                    }
                }
                G.conn.setAutoCommit(true);
                G.conn.close();
                long totalTiempo = System.currentTimeMillis() - tiempoInicio;
                System.out.println("El tiempo total es :" + totalTiempo / 1000 + " segundos");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
