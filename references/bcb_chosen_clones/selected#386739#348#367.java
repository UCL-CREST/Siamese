    public void zip(String zipFile) {
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
            out.putNextEntry(new ZipEntry((new File(currentFile)).getName()));
            String newline = System.getProperty("line.separator");
            Element map = getDocument().getDefaultRootElement();
            for (int i = 0; i < map.getElementCount(); i++) {
                Element line = map.getElement(i);
                int start = line.getStartOffset();
                byte[] buf = (getText(start, line.getEndOffset() - start - 1) + newline).getBytes();
                out.write(buf, 0, buf.length);
            }
            out.closeEntry();
            out.close();
        } catch (IOException ioe) {
            showError("Error has occured while ziping");
        } catch (BadLocationException ble) {
            showError("Error has occured while ziping");
        }
    }
