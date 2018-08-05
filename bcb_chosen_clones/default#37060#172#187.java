    public byte[] deleteAllArchives(HTTPurl urlData, OutputStream outStream) throws Exception {
        File outFile = new File(new DllWrapper().getAllUserPath() + "archive");
        if (outFile.exists() == false) outFile.mkdirs();
        File[] files = outFile.listFiles();
        Arrays.sort(files);
        for (int x = files.length - 1; files != null && x >= 0; x--) {
            File archiveFile = files[x];
            if (archiveFile.isDirectory() == false && archiveFile.getName().startsWith("Schedule-")) {
                archiveFile.delete();
            }
        }
        StringBuffer buff = new StringBuffer(256);
        buff.append("HTTP/1.0 302 Moved Temporarily\n");
        buff.append("Location: /servlet/ArchiveDataRes?action=showArchive\n\n");
        return buff.toString().getBytes();
    }
