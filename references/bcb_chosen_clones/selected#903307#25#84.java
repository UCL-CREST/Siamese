    public static final void buildZipBall(ZipMap zipMap, String outputFilepath) throws Exception {
        String outFilename = outputFilepath;
        if (!outFilename.endsWith(".zip")) {
            outFilename += ".zip";
        }
        Messenger.printMsg(Messenger.TRACE, "Build ZIP file <" + outputFilepath + ">");
        ZipOutputStream out;
        out = new ZipOutputStream(new FileOutputStream(outFilename));
        long file_size = 0;
        boolean full = false;
        for (String dir : zipMap.keySet()) {
            Set<ZipEntryRef> zers = zipMap.get(dir);
            TreeSet<String> storedRef = new TreeSet<String>();
            for (ZipEntryRef zer : zers) {
                String fileitem = zer.getUri();
                byte[] buf = new byte[1024];
                FileInputStream in;
                File f;
                if ((f = new File(fileitem)).exists()) {
                    in = new FileInputStream(fileitem);
                    file_size += f.length() / 1000000;
                } else if ((f = new File(fileitem + ".gz")).exists()) {
                    in = new FileInputStream(fileitem + ".gz");
                    file_size += f.length() / 1000000;
                } else {
                    Messenger.printMsg(Messenger.ERROR, "File <" + fileitem + "> or <" + fileitem + ".gz> not found");
                    continue;
                }
                Messenger.printMsg(Messenger.TRACE, "Compress " + fileitem);
                String str = dir + "/" + zer.getName();
                ZipEntry ze = new ZipEntry(str);
                if (!storedRef.contains(str)) {
                    out.putNextEntry(ze);
                    storedRef.add(str);
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.closeEntry();
                }
                if (file_size > MAX_SIZE) {
                    String msg = "The size of the zipped file exceed " + MAX_SIZE + "Mb: zipball truncated";
                    Messenger.printMsg(Messenger.WARNING, msg);
                    full = true;
                    Messenger.printMsg(Messenger.TRACE, "Compress truncated.txt");
                    ze = new ZipEntry("truncated.txt");
                    out.putNextEntry(ze);
                    out.write(msg.getBytes(), 0, msg.length());
                    out.closeEntry();
                    break;
                }
            }
            if (full) {
                break;
            }
        }
        out.closeEntry();
        out.close();
    }
