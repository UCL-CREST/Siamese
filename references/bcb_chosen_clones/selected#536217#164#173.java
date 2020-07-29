    private void publishMap(LWMap map) throws IOException {
        File savedMap = PublishUtil.saveMap(map);
        InputStream istream = new BufferedInputStream(new FileInputStream(savedMap));
        OutputStream ostream = new BufferedOutputStream(new FileOutputStream(ActionUtil.selectFile("ConceptMap", "vue")));
        int fileLength = (int) savedMap.length();
        byte bytes[] = new byte[fileLength];
        while (istream.read(bytes, 0, fileLength) != -1) ostream.write(bytes, 0, fileLength);
        istream.close();
        ostream.close();
    }
