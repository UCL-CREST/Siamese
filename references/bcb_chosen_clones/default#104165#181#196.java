    public byte[] deleteAllArchives(HTTPurl urlData) throws Exception {
        File outFile = new File(new DllWrapper().getAllUserPath() + "archive");
        if (outFile.exists() == false) outFile.mkdirs();
        File[] files = outFile.listFiles();
        Arrays.sort(files);
        for (int x = files.length - 1; files != null && x >= 0; x--) {
            File archiveFile = files[x];
            if (archiveFile.isDirectory() == false && archiveFile.getName().startsWith("Task-")) {
                archiveFile.delete();
            }
        }
        StringBuffer buff = new StringBuffer(256);
        buff.append("HTTP/1.0 302 Moved Temporarily\n");
        buff.append("Location: /servlet/TaskManagementDataRes?action=05\n\n");
        return buff.toString().getBytes();
    }
