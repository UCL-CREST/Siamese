    private static void dumpToZipStream(Iterator<Lead> aCardioIterator, String date, ZipOutputStream zos) throws IOException {
        int j = 0;
        while (aCardioIterator.hasNext()) {
            Lead l = aCardioIterator.next();
            ZipEntry ze = new ZipEntry(date + j + "_" + (j % 2 == 0 ? "1" : "2"));
            j++;
            zos.putNextEntry(ze);
            short[] data = l.getData();
            String content = "";
            for (short value : data) {
                content += value + "\n";
            }
            zos.write(content.getBytes());
        }
    }
