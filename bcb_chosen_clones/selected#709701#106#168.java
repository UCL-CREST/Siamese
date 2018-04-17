        public ActualTask(TEditor editor, TIGDataBase dataBase, String directoryPath) {
            File myDirectory = new File(directoryPath);
            int i;
            Vector images = new Vector();
            images = dataBase.allImageSearch();
            lengthOfTask = images.size() * 2;
            String directory = directoryPath + "Images" + myDirectory.separator;
            File newDirectoryFolder = new File(directory);
            newDirectoryFolder.mkdirs();
            try {
                DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
                doc = domBuilder.newDocument();
            } catch (Exception exc) {
                System.out.println(exc.getMessage());
                System.out.println(exc.toString());
            }
            Element dbElement = doc.createElement("dataBase");
            for (i = 0; ((i < images.size()) && !stop); i++) {
                current = i;
                String element = (String) images.elementAt(i);
                String pathSrc = "Images" + File.separator + element.substring(0, 1).toUpperCase() + File.separator + element;
                String name = pathSrc.substring(pathSrc.lastIndexOf(myDirectory.separator) + 1, pathSrc.length());
                String pathDst = directory + name;
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
                Vector keyWords = new Vector();
                keyWords = dataBase.asociatedConceptSearch((String) images.elementAt(i));
                Element imageElement = doc.createElement("image");
                Element imageNameElement = doc.createElement("name");
                imageNameElement.appendChild(doc.createTextNode(name));
                imageElement.appendChild(imageNameElement);
                for (int j = 0; j < keyWords.size(); j++) {
                    Element keyWordElement = doc.createElement("keyWord");
                    keyWordElement.appendChild(doc.createTextNode((String) keyWords.elementAt(j)));
                    imageElement.appendChild(keyWordElement);
                }
                dbElement.appendChild(imageElement);
            }
            try {
                doc.appendChild(dbElement);
                File dst = new File(directory.concat("Images"));
                BufferedWriter bufferWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dst), "UTF-8"));
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(bufferWriter);
                transformer.transform(source, result);
                bufferWriter.close();
            } catch (Exception exc) {
                System.out.println(exc.getMessage());
                System.out.println(exc.toString());
            }
            current = lengthOfTask;
        }
