    private static void createIndex(JarOutputStream jos, List oldEntries, Map movedMap) throws IOException {
        StringWriter writer = new StringWriter();
        writer.write(VERSION_HEADER);
        writer.write("\r\n");
        for (int counter = 0; counter < oldEntries.size(); counter++) {
            String name = (String) oldEntries.get(counter);
            writer.write(REMOVE_COMMAND);
            writer.write(" ");
            writeEscapedString(writer, name);
            writer.write("\r\n");
        }
        Iterator names = movedMap.keySet().iterator();
        if (names != null) {
            while (names.hasNext()) {
                String newName = (String) names.next();
                String oldName = (String) movedMap.get(newName);
                writer.write(MOVE_COMMAND);
                writer.write(" ");
                writeEscapedString(writer, oldName);
                writer.write(" ");
                writeEscapedString(writer, newName);
                writer.write("\r\n");
            }
        }
        ZipEntry je = new ZipEntry(INDEX_NAME);
        byte[] bytes = writer.toString().getBytes("UTF-8");
        writer.close();
        jos.putNextEntry(je);
        jos.write(bytes, 0, bytes.length);
    }
