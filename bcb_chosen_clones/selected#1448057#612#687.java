    public static boolean exportAsGAMETELLearningObject(String zipFilename, String loName, String authorName, String organization, boolean windowed, String gameFilename, String testReturnURI, String testUserID, AdventureDataControl adventureData) {
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
            HashMap<String, String> additionalParams = new HashMap<String, String>();
            if (testReturnURI != null && testUserID != null && testReturnURI.length() > 0 && testUserID.length() > 0) {
                additionalParams.put("return-uri", testReturnURI);
                additionalParams.put("user-id", testUserID);
            }
            dataSaved &= writeWebPage(tempDir.getAbsolutePath(), loName, windowed, "es.eucm.eadventure.engine.EAdventureAppletGAMETEL", additionalParams);
            File jarUnsigned = new File(tempDir.getAbsolutePath() + "/eAdventure.zip");
            FileOutputStream mergedFile = new FileOutputStream(jarUnsigned);
            ZipOutputStream os = new ZipOutputStream(mergedFile);
            String manifestText = Writer.defaultManifestFile("es.eucm.eadventure.engine.EAdventureAppletGAMETEL");
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
