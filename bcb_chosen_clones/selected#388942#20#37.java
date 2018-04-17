    public void preProcessFileList(ProcessorInfo processorInfo, Subject peerSubject) throws Exception {
        System.out.println(" [ PreZipFilter ] Subject: " + peerSubject);
        for (int i = 0; i < processorInfo.fileList.length; i++) {
            byte[] buf = new byte[1024];
            final String outFilename = processorInfo.fileList[i] + ".zip";
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
            FileInputStream in = new FileInputStream(processorInfo.fileList[i]);
            out.putNextEntry(new ZipEntry(processorInfo.fileList[i]));
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.closeEntry();
            in.close();
            out.close();
            processorInfo.fileList[i] = outFilename;
        }
    }
