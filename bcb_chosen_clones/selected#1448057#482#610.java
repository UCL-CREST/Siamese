    public static boolean exportAsLAMSLearningObject(String zipFilename, String loName, String authorName, String organization, boolean windowed, String gameFilename, AdventureDataControl adventureData) {
        boolean dataSaved = true;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            TransformerFactory tFactory = TransformerFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = null;
            Transformer transformer = null;
            OutputStream fout = null;
            OutputStreamWriter writeFile = null;
            File tempDir = new File(Controller.createTempDirectory().getAbsolutePath());
            for (File tempFile : tempDir.listFiles()) {
                if (tempFile.isDirectory()) tempFile.deleteAll();
                tempFile.delete();
            }
            dataSaved &= writeWebPage(tempDir.getAbsolutePath(), loName, windowed, "es.eucm.eadventure.engine.EAdventureAppletLAMS ");
            File jarUnsigned = new File(tempDir.getAbsolutePath() + "/eAdventure.zip");
            FileOutputStream mergedFile = new FileOutputStream(jarUnsigned);
            ZipOutputStream os = new ZipOutputStream(mergedFile);
            String manifestText = Writer.defaultManifestFile("es.eucm.eadventure.engine.EAdventureAppletLAMS");
            ZipEntry manifestEntry = new ZipEntry("META-INF/MANIFEST.MF");
            ZipEntry manifestEntry2 = new ZipEntry("META-INF/services/javax.xml.parsers.SAXParserFactory");
            ZipEntry manifestEntry3 = new ZipEntry("META-INF/services/javax.xml.parsers.DocumentBuilderFactory");
            os.putNextEntry(manifestEntry);
            os.write(manifestText.getBytes());
            os.putNextEntry(manifestEntry2);
            os.putNextEntry(manifestEntry3);
            os.closeEntry();
            File.mergeZipAndDirToJar("web/eAdventure_temp.jar", gameFilename, os);
            addNeededLibrariesToJar(os, Controller.getInstance());
            os.close();
            dataSaved &= jarUnsigned.renameTo(new File(tempDir.getAbsolutePath() + "/" + loName + "_unsigned.jar"));
            dataSaved = JARSigner.signJar(authorName, organization, tempDir.getAbsolutePath() + "/" + loName + "_unsigned.jar", tempDir.getAbsolutePath() + "/" + loName + ".jar");
            new File(tempDir.getAbsolutePath() + "/" + loName + "_unsigned.jar").delete();
            db = dbf.newDocumentBuilder();
            doc = db.newDocument();
            Element manifest = null;
            manifest = doc.createElement("manifest");
            manifest.setAttribute("identifier", "imsaccmdv1p0_manifest");
            manifest.setAttribute("xmlns", "http://www.imsglobal.org/xsd/imscp_v1p1");
            manifest.setAttribute("xmlns:imsmd", "http://www.imsglobal.org/xsd/imsmd_v1p2");
            manifest.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            manifest.setAttribute("xsi:schemaLocation", "http://www.imsglobal.org/xsd/imscp_v1p1 imscp_v1p1.xsd http://www.imsglobal.org/xsd/imsmd_v1p2 imsmd_v1p2p4.xsd");
            manifest.setAttribute("version", "IMS CP 1.1.3");
            Element organizations = doc.createElement("organizations");
            organizations.setAttribute("default", ORGANIZATION_IDENTIFIER);
            Element organizationEl = doc.createElement("organization");
            organizationEl.setAttribute("identifier", ORGANIZATION_IDENTIFIER);
            Node organizationTitleNode = doc.createElement("title");
            organizationTitleNode.setTextContent(ORGANIZATION_TITLE);
            organizationEl.appendChild(organizationTitleNode);
            Element itemEl = doc.createElement("item");
            itemEl.setAttribute("identifier", ITEM_IDENTIFIER);
            itemEl.setAttribute("identifierref", RESOURCE_IDENTIFIER);
            itemEl.setAttribute("isvisible", "1");
            itemEl.setAttribute("parameters", "");
            Node itemTitleNode = doc.createElement("title");
            itemTitleNode.setTextContent(adventureData.getTitle());
            itemEl.appendChild(itemTitleNode);
            organizationEl.appendChild(itemEl);
            organizations.appendChild(organizationEl);
            manifest.appendChild(organizations);
            Node resources = doc.createElement("resources");
            Element resource = doc.createElement("resource");
            resource.setAttribute("identifier", RESOURCE_IDENTIFIER);
            resource.setAttribute("type", "webcontent");
            resource.setAttribute("href", loName + ".html");
            Node metaData = doc.createElement("metadata");
            Node lomNode = LOMDOMWriter.buildLOMDOM(adventureData.getLomController(), false);
            doc.adoptNode(lomNode);
            metaData.appendChild(lomNode);
            resource.appendChild(metaData);
            Element file = doc.createElement("file");
            file.setAttribute("href", loName + ".html");
            resource.appendChild(file);
            Element file5 = doc.createElement("file");
            file5.setAttribute("href", loName + ".jar");
            resource.appendChild(file5);
            Element file4 = doc.createElement("file");
            file4.setAttribute("href", "splashScreen.gif");
            resource.appendChild(file4);
            resources.appendChild(resource);
            manifest.appendChild(resources);
            indentDOM(manifest, 0);
            doc.adoptNode(manifest);
            doc.appendChild(manifest);
            transformer = tFactory.newTransformer();
            fout = new FileOutputStream(tempDir.getAbsolutePath() + "/imsmanifest.xml");
            writeFile = new OutputStreamWriter(fout, "UTF-8");
            transformer.transform(new DOMSource(doc), new StreamResult(writeFile));
            writeFile.close();
            fout.close();
            File splashScreen = new File("web/splashScreen.gif");
            if (windowed) {
                splashScreen = new File("web/splashScreen_red.gif");
            }
            splashScreen.copyTo(new File(tempDir.getAbsolutePath() + "/splashScreen.gif"));
            db = dbf.newDocumentBuilder();
            doc = db.newDocument();
            Element param = ExpectedGameIODOMWriter.buildExpectedInputs(doc, getAdaptationRules(adventureData.getAdventureData().getChapters()), getAssessmemtRules(adventureData.getAdventureData().getChapters()));
            indentDOM(param, 0);
            doc.adoptNode(param);
            doc.appendChild(param);
            transformer = tFactory.newTransformer();
            fout = new FileOutputStream(tempDir.getAbsolutePath() + "/ead-parameters.xml");
            writeFile = new OutputStreamWriter(fout, "UTF-8");
            transformer.transform(new DOMSource(doc), new StreamResult(writeFile));
            writeFile.close();
            fout.close();
            File.zipDirectory(tempDir.getAbsolutePath() + "/", zipFilename);
        } catch (IOException exception) {
            Controller.getInstance().showErrorDialog(TC.get("Error.Title"), TC.get("Error.WriteData"));
            ReportDialog.GenerateErrorReport(exception, true, TC.get("Error.WriteData"));
            dataSaved = false;
        } catch (ParserConfigurationException exception) {
            Controller.getInstance().showErrorDialog(TC.get("Error.Title"), TC.get("Error.WriteData"));
            ReportDialog.GenerateErrorReport(exception, true, TC.get("Error.WriteData"));
            dataSaved = false;
        } catch (TransformerConfigurationException exception) {
            Controller.getInstance().showErrorDialog(TC.get("Error.Title"), TC.get("Error.WriteData"));
            ReportDialog.GenerateErrorReport(exception, true, TC.get("Error.WriteData"));
            dataSaved = false;
        } catch (TransformerException exception) {
            Controller.getInstance().showErrorDialog(TC.get("Error.Title"), TC.get("Error.WriteData"));
            ReportDialog.GenerateErrorReport(exception, true, TC.get("Error.WriteData"));
            dataSaved = false;
        }
        return dataSaved;
    }
