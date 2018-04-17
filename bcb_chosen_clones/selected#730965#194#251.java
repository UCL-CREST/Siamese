    public void writeToFile(String filePath, boolean deleteBefore) {
        Transformer t;
        ByteArrayOutputStream documentContentsOs;
        ByteArrayOutputStream customPropsOs;
        try {
            t = TransformerFactory.newInstance().newTransformer();
            documentContentsOs = new ByteArrayOutputStream();
            customPropsOs = new ByteArrayOutputStream();
            t.transform(new DOMSource(getContentsDom()), new StreamResult(documentContentsOs));
            t.transform(new DOMSource(getCustomDocPropertiesDom()), new StreamResult(customPropsOs));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        File output = new File(filePath);
        if (output.exists()) {
            output.delete();
        }
        ZipOutputStream officeFileOutFile;
        try {
            officeFileOutFile = new ZipOutputStream(new FileOutputStream(filePath));
        } catch (FileNotFoundException e) {
            throw new VRuntimeException(e);
        }
        try {
            Enumeration<? extends ZipEntry> entriesIter = getOfficeFile().entries();
            while (entriesIter.hasMoreElements()) {
                ZipEntry entry = entriesIter.nextElement();
                _log.info("writeToFile", new Object[] { "Writing entry to output officeFile: ", entry.getName() });
                if (entry.getName().equals(OfficeDoc.Part.PartDocument.getName())) {
                    byte[] data = documentContentsOs.toByteArray();
                    officeFileOutFile.putNextEntry(new ZipEntry(entry.getName()));
                    officeFileOutFile.write(data, 0, data.length);
                    officeFileOutFile.closeEntry();
                    documentContentsOs.close();
                } else if (entry.getName().equals(OfficeDoc.Part.PartPropertiesCustom.getName())) {
                    byte[] data = customPropsOs.toByteArray();
                    officeFileOutFile.putNextEntry(new ZipEntry(entry.getName()));
                    officeFileOutFile.write(data, 0, data.length);
                    officeFileOutFile.closeEntry();
                    customPropsOs.close();
                } else {
                    InputStream incoming = getOfficeFile().getInputStream(entry);
                    byte[] data = BinaryResource.loadResource(incoming);
                    officeFileOutFile.putNextEntry(new ZipEntry(entry.getName()));
                    officeFileOutFile.write(data, 0, data.length);
                    officeFileOutFile.closeEntry();
                }
            }
        } catch (Exception e) {
            throw new VRuntimeException(e);
        } finally {
            try {
                officeFileOutFile.close();
            } catch (IOException e) {
                throw new VRuntimeException(e);
            }
        }
    }
