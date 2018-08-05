    public void writeBookOutput(ZipOutputStream outputStream, String cleanSGML) {
        boolean isCompressedOutput = false;
        if (outputStream != null) {
            isCompressedOutput = true;
        }
        try {
            FileWriter fw = new FileWriter(bookPath);
            if (isCompressedOutput) {
                if (witsInstance.getOutputType().equals("solbook")) {
                    SolBookWriter cWriter = new SolBookWriter(witsInstance, cleanSGML, props);
                    ZipEntry entry = new ZipEntry(bookPath.getName());
                    try {
                        outputStream.putNextEntry(entry);
                        outputStream.write(cWriter.getPartialBookBody().getBytes());
                        outputStream.closeEntry();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                if (witsInstance.getOutputType().equals("docbook")) {
                    DocBookWriter cWriter = new DocBookWriter(cleanSGML, props);
                    ZipEntry entry = new ZipEntry(bookPath.getName());
                    try {
                        outputStream.putNextEntry(entry);
                        outputStream.write(cWriter.getPartialBookBody().getBytes());
                        outputStream.closeEntry();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                if (witsInstance.getOutputType().equals("solbook")) {
                    SolBookWriter bWriter = new SolBookWriter(witsInstance, cleanSGML, props);
                    fw.write(bWriter.getPartialBookBody());
                    fw.flush();
                    fw.close();
                }
                if (witsInstance.getOutputType().equals("docbook")) {
                    DocBookWriter bWriter = new DocBookWriter(cleanSGML, props);
                    fw.write(bWriter.getPartialBookBody());
                    fw.flush();
                    fw.close();
                }
            }
        } catch (IOException e) {
            System.out.println("Error while writing to: " + bookPath + ". PLease check the path/permission.");
        }
    }
