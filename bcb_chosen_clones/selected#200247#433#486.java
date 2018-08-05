        public void saveFile(File file) throws IOException {
            boolean savingSameFile = false;
            SelectableEntry root = creator.getFileSelectTask().getRootEntry();
            if (root instanceof ZipFileEntry && file.exists()) {
                String sourceName = ((ZipFileEntry) root).getSourceFile().getName();
                if (sourceName.equals(file.getAbsolutePath())) {
                    savingSameFile = true;
                }
            }
            if (savingSameFile) {
                file = File.createTempFile("zip", ".tmp");
            }
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file));
            updateMetadata();
            StringBuffer fileMapBuffer = new StringBuffer("<METS:fileSec><METS:fileGrp>");
            StringBuffer structMapBuffer = new StringBuffer("<METS:structMap>");
            walkTree(zos, fileMapBuffer, structMapBuffer, "", root);
            structMapBuffer.append("</METS:structMap>");
            fileMapBuffer.append("</METS:fileGrp></METS:fileSec>");
            StringBuffer xmlBuffer = new StringBuffer(HEADER);
            xmlBuffer.append(fileMapBuffer);
            xmlBuffer.append(structMapBuffer);
            xmlBuffer.append(FOOTER);
            ZipEntry entry = new ZipEntry("METS.xml");
            entry.setTime(System.currentTimeMillis());
            zos.putNextEntry(entry);
            StringReader xmlReader = new StringReader(xmlBuffer.toString());
            int byteRead;
            while ((byteRead = xmlReader.read()) != -1) {
                zos.write(byteRead);
            }
            zos.closeEntry();
            entry = new ZipEntry("crules.xml");
            entry.setTime(System.currentTimeMillis());
            zos.putNextEntry(entry);
            xmlReader = new StringReader(creator.getConversionRulesTask().getRules().toXML());
            while ((byteRead = xmlReader.read()) != -1) {
                zos.write(byteRead);
            }
            zos.closeEntry();
            zos.close();
            if (savingSameFile) {
                ((ZipFileEntry) root).getSourceFile().close();
                try {
                    JFileChooser fileChooser = creator.getFileChooser();
                    fileChooser.getSelectedFile().delete();
                    file.renameTo(fileChooser.getSelectedFile());
                    creator.getFileSelectTask().getOpenZipFileAction().openZipFile(fileChooser.getSelectedFile());
                } catch (Exception e) {
                    Utility.showExceptionDialog(creator, e);
                }
            }
            JOptionPane.showMessageDialog(creator, "ZIP File successfully written");
        }
