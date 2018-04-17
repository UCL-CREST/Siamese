    private void saveProblem() {
        if (cageCollection != null) {
            if (currentSavedFile == null) currentSavedFile = selectFile(desktopDirectory, "Save Work", "Enter File Name to Save", false, wrkFilterString, "Work Files", JFileChooser.SAVE_DIALOG);
            try {
                Iterator<CageUI> it = cageCollection.iterator();
                ArrayList<Cage> al = new ArrayList<Cage>();
                while (it.hasNext()) {
                    CageUI cui = it.next();
                    Cage c = cui.getCage();
                    al.add(c);
                }
                if (currentSavedFile != null) {
                    if (!currentSavedFile.getPath().endsWith(wrkFilterString)) {
                        currentSavedFile = convertTo(currentSavedFile, "." + wrkFilterString);
                    }
                    Document doc = getXMLDoc(al);
                    XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
                    String xmlString = outputter.outputString(doc);
                    byte[] xmlBytes = null;
                    try {
                        xmlBytes = xmlString.getBytes("UTF-8");
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    }
                    ZipOutputStream zipWriter = null;
                    try {
                        zipWriter = new ZipOutputStream(new FileOutputStream(currentSavedFile));
                        zipWriter.setLevel(Deflater.DEFAULT_COMPRESSION);
                        zipWriter.putNextEntry(new ZipEntry("work.txt"));
                        zipWriter.write(xmlBytes, 0, xmlBytes.length);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (zipWriter != null) {
                                zipWriter.close();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }
