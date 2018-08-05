    private void storeProcessList(Date date, List<ProcessList> processListes, boolean doNotOverrideFile) {
        final int BUFFER = 2048;
        String filename = BASE_FILES_PATH + serverGroup + "/processlist/" + sdf.format(date) + ".zip";
        if (doNotOverrideFile && new File(filename).exists()) return;
        try {
            FileOutputStream dest = new FileOutputStream(filename);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            byte data[] = new byte[BUFFER];
            for (ProcessList processList : processListes) {
                ByteArrayInputStream fi = new ByteArrayInputStream(buildProcessListText(processList).getBytes());
                BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(processList.getMysqlServer().getName() + ".txt");
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
