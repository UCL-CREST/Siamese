    private void addFile(ZipOutputStream out, String basePath, File file) throws IOException {
        BufferedInputStream origin = null;
        FileInputStream fi = null;
        if (!basePath.trim().equals("")) {
            basePath = basePath + SEPARATOR;
        }
        try {
            byte data[] = new byte[BUFFER];
            fi = new FileInputStream(file);
            origin = new BufferedInputStream(fi, BUFFER);
            ZipEntry entry = new ZipEntry(basePath + file.getName());
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            currentMessage = "Add File: " + file;
            finishSpace = finishSpace + file.getTotalSpace();
            if (zipMonitor != null) {
                zipMonitor.printMessage();
            }
            if (printToConsoleFlag) {
                System.out.println(currentMessage);
                System.out.println("complete: " + getZipPercetage() + "%");
            }
        } finally {
            if (origin != null) {
                origin.close();
            }
            if (fi != null) {
                fi.close();
            }
        }
    }
