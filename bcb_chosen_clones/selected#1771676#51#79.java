    public static void saveMap(Map map, String path) throws IOException {
        ObjectOutputStream oout = null;
        ArrayList<MapEvent> events = map.getEvents();
        FileOutputStream t = new FileOutputStream(path);
        CheckedOutputStream csum = new CheckedOutputStream(t, new Adler32());
        BufferedOutputStream bos = new BufferedOutputStream(csum);
        ZipOutputStream zipper = new ZipOutputStream(bos);
        LinkedList<String> toWrite = Parser.getMapInformation(map);
        ZipEntry mapfile = new ZipEntry("mapfile.cnf");
        zipper.putNextEntry(mapfile);
        for (int i = 0; i < toWrite.size(); i++) {
            zipper.write(toWrite.get(i).getBytes());
            zipper.write(System.getProperty("line.separator").getBytes());
        }
        zipper.closeEntry();
        for (int i = 0; i < events.size(); i++) {
            ZipEntry test = new ZipEntry(events.get(i).getFilename());
            zipper.putNextEntry(test);
            oout = new ObjectOutputStream(zipper);
            oout.writeObject(events.get(i));
            zipper.closeEntry();
        }
        if (oout != null) {
            oout.flush();
            oout.close();
        } else {
            zipper.close();
        }
    }
