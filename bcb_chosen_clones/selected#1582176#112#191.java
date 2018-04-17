        public ActualTask(TEditor editor, TIGDataBase dataBase, String directoryPath, String myImagesBehaviour) {
            File myDirectory = new File(directoryPath);
            String[] list = myDirectory.list();
            File fileXML = new File(directoryPath + "images.xml");
            SAXBuilder builder = new SAXBuilder(false);
            try {
                Document docXML = builder.build(fileXML);
                Element root = docXML.getRootElement();
                List images = root.getChildren("image");
                Iterator j = images.iterator();
                int i = 0;
                TIGDataBase.activateTransactions();
                while (j.hasNext() && !stop && !cancel) {
                    current = i;
                    i++;
                    Element image = (Element) j.next();
                    String name = image.getAttributeValue("name");
                    List categories = image.getChildren("category");
                    Iterator k = categories.iterator();
                    if (exists(list, name)) {
                        String pathSrc = directoryPath.concat(name);
                        String pathDst = System.getProperty("user.dir") + File.separator + "images" + File.separator + name.substring(0, 1).toUpperCase() + File.separator;
                        String folder = System.getProperty("user.dir") + File.separator + "images" + File.separator + name.substring(0, 1).toUpperCase();
                        if (myImagesBehaviour.equals(TLanguage.getString("TIGImportDBDialog.REPLACE_IMAGES"))) {
                            Vector<Vector<String>> aux = TIGDataBase.imageSearchByName(name.substring(0, name.lastIndexOf('.')));
                            if (aux.size() != 0) {
                                int idImage = TIGDataBase.imageKeySearchName(name.substring(0, name.lastIndexOf('.')));
                                TIGDataBase.deleteAsociatedOfImage(idImage);
                            }
                            pathDst = pathDst.concat(name);
                        }
                        if (myImagesBehaviour.equals(TLanguage.getString("TIGImportDBDialog.ADD_IMAGES"))) {
                            Vector aux = new Vector();
                            aux = TIGDataBase.imageSearchByName(name.substring(0, name.lastIndexOf('.')));
                            int fileCount = 0;
                            if (aux.size() != 0) {
                                while (aux.size() != 0) {
                                    fileCount++;
                                    aux = TIGDataBase.imageSearchByName(name.substring(0, name.lastIndexOf('.')) + "_" + fileCount);
                                }
                                pathDst = pathDst + name.substring(0, name.lastIndexOf('.')) + '_' + fileCount + name.substring(name.lastIndexOf('.'), name.length());
                                name = name.substring(0, name.lastIndexOf('.')) + '_' + fileCount + name.substring(name.lastIndexOf('.'), name.length());
                            } else {
                                pathDst = pathDst.concat(name);
                            }
                        }
                        String pathThumbnail = (pathDst.substring(0, pathDst.lastIndexOf("."))).concat("_th.jpg");
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
                            System.out.println(exc.getMessage());
                            System.out.println(exc.toString());
                        }
                        TIGDataBase.insertImageDB(name.substring(0, name.lastIndexOf('.')), name);
                        int idImage = TIGDataBase.imageKeySearchName(name.substring(0, name.lastIndexOf('.')));
                        while (k.hasNext()) {
                            Element category = (Element) k.next();
                            int idCategory = TIGDataBase.insertConceptDB(category.getValue());
                            TIGDataBase.insertAsociatedDB(idCategory, idImage);
                        }
                    } else {
                        errorImages = errorImages + System.getProperty("line.separator") + name;
                    }
                }
                TIGDataBase.executeQueries();
                current = lengthOfTask;
            } catch (JDOMException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
