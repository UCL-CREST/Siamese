    public boolean write() {
        try {
            if (myFile.getParentFile() != null) myFile.getParentFile().mkdirs();
            BufferedWriter writer;
            if (isZip()) {
                ZipOutputStream out = null;
                if (myFile.exists()) {
                    List<byte[]> entries = new LinkedList<byte[]>();
                    List<ZipEntry> headers = new LinkedList<ZipEntry>();
                    ZipFile zf = new ZipFile(myFile);
                    Enumeration<? extends ZipEntry> enumeration = zf.entries();
                    while (enumeration.hasMoreElements()) {
                        ZipEntry ze = enumeration.nextElement();
                        byte[] data = new byte[(int) ze.getSize()];
                        zf.getInputStream(ze).read(data, 0, data.length);
                        headers.add(ze);
                        entries.add(data);
                    }
                    out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(myFile)));
                    ListIterator<ZipEntry> header_iter = headers.listIterator();
                    for (byte[] data : entries) {
                        ZipEntry ze = header_iter.next();
                        out.putNextEntry(new ZipEntry(ze.getName()));
                        out.write(data, 0, data.length);
                        out.closeEntry();
                    }
                } else out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(myFile)));
                out.putNextEntry(new ZipEntry(myNameInZip));
                writer = new BufferedWriter(new OutputStreamWriter(out));
            } else writer = new BufferedWriter(new FileWriter(myFile));
            for (String resultClass : myResultClasses) {
                if (resultClass != null) writer.write(resultClass + ";");
            }
            writer.write(System.getProperty("line.separator"));
            int columns = myResultClasses.length;
            for (LinkedList<String[]> page : myResults) {
                for (String[] row : page) {
                    int column = 0;
                    if (row != null) {
                        for (String result : row) {
                            if (result != null) writer.write(result + ";");
                            if (++column >= columns) {
                                writer.write(System.getProperty("line.separator"));
                                column = 0;
                            }
                        }
                    }
                }
            }
            writer.close();
        } catch (IOException exc) {
            System.err.println(exc);
            return false;
        }
        return true;
    }
