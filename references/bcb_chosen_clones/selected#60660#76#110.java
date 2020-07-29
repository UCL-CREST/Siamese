    private File createArchive() throws IOException {
        DateFormat dateFormat;
        File inFolder, outFolder;
        ZipOutputStream out;
        BufferedInputStream in;
        byte[] data;
        String files[];
        String archiveName;
        setProgress(0);
        inFolder = new File(localDirectory);
        outFolder = null;
        if (inFolder.exists()) {
            dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            archiveName = inFolder.getName() + "(" + dateFormat.format(new Date()) + ").zip";
            msg.addMsg("Creating archive: " + archiveName + " ...");
            outFolder = new File(localDirectory + "\\" + archiveName);
            out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outFolder)));
            data = new byte[1024];
            files = inFolder.list(new FileExtensionFilter(properties.getProperty("ACCEPTED_EXTENSIONS").toLowerCase().split(",")));
            for (int i = 0; i < files.length; i++) {
                in = new BufferedInputStream(new FileInputStream(inFolder.getPath() + "//" + files[i]), 1024);
                out.putNextEntry(new ZipEntry(files[i]));
                int count;
                while ((count = in.read(data, 0, 1024)) != -1) {
                    out.write(data, 0, count);
                }
                out.closeEntry();
                setProgress(Math.min((i * 100) / files.length, 100));
            }
            out.flush();
            out.close();
            msg.addMsg("Archive created.");
        }
        return outFolder;
    }
