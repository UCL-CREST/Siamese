    public void export() {
        try {
            class MyFilter implements FileFilter {

                public boolean accept(File file) {
                    return (file.getName().startsWith(graphName) && file.getName().endsWith("display"));
                }
            }
            ;
            cat.debug("graphFile: " + graphFile);
            File parentDir = graphFile.getParentFile();
            if (parentDir == null) parentDir = new File(".");
            File[] displayFiles = parentDir.listFiles(new MyFilter());
            Graph graph = (new GraphWriter()).readXML(new GraphImp(GraphImp.EMPTY_GRAPH), new FileInputStream(graphFile));
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputFile));
            for (int i = 0; i < displayFiles.length; i++) {
                File file = displayFiles[i];
                out.putNextEntry(new ZipEntry(file.getName()));
                byte[] arr = new byte[(int) file.length()];
                (new FileInputStream(file)).read(arr, 0, (int) file.length());
                out.write(arr, 0, (int) file.length());
                out.closeEntry();
            }
            out.putNextEntry(new ZipEntry(graphName + ".files/"));
            relativateFiles(graphName, graph, out);
            out.putNextEntry(new ZipEntry(graphName + ".xml"));
            (new GraphWriter()).writeXML(graph, out);
            out.closeEntry();
            out.flush();
            out.close();
        } catch (IOException exc) {
            exc.printStackTrace(System.err);
        }
    }
