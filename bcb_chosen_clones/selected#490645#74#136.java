    public SukuData exportBackup(String path, String dbName) {
        SukuData dat = new SukuData();
        String root = "genealog";
        this.dbName = dbName;
        if (path == null || path.lastIndexOf(".") < 1) {
            dat.resu = "output filename missing";
            return dat;
        }
        images = new Vector<MinimumImage>();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element rootElement = document.createElement(root);
            document.appendChild(rootElement);
            rootElement.setAttribute("finfamily", AntVersion.antVersion);
            zipPath = path.substring(0, path.lastIndexOf("."));
            ByteArrayOutputStream bbos = new ByteArrayOutputStream();
            ZipOutputStream zip = new ZipOutputStream(bbos);
            String fileName = zipPath + "/" + this.dbName + ".xml";
            createOwnerElement(document, rootElement, dbName + "_files");
            createUnitsElement(document, rootElement);
            createRelationsElement(document, rootElement);
            createConversionsElement(document, rootElement);
            createTypesElement(document, rootElement);
            createViewsElement(document, rootElement);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(bos);
            transformer.transform(source, result);
            ZipEntry entry = new ZipEntry(fileName);
            zip.putNextEntry(entry);
            zip.write(bos.toByteArray());
            zip.closeEntry();
            double dbSize = images.size();
            for (int i = 0; i < images.size(); i++) {
                double prossa = i / dbSize;
                int prose = (int) (prossa * 100);
                setRunnerValue("" + prose + ";" + images.get(i).getPath());
                entry = new ZipEntry(zipPath + "/" + images.get(i).getPath());
                zip.putNextEntry(entry);
                zip.write(images.get(i).imageData);
                zip.closeEntry();
            }
            zip.close();
            dat.buffer = bbos.toByteArray();
        } catch (ParserConfigurationException e) {
            dat.resu = e.getMessage();
            e.printStackTrace();
        } catch (TransformerException e) {
            dat.resu = e.getMessage();
            e.printStackTrace();
        } catch (SQLException e) {
            dat.resu = e.getMessage();
            e.printStackTrace();
        } catch (IOException e) {
            dat.resu = e.getMessage();
            e.printStackTrace();
        }
        return dat;
    }
