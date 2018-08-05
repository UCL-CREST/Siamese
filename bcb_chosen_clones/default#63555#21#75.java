    public boolean create() {
        DirectoryManager d = new DirectoryManager(dirSource);
        Vector<String> filenames = new Vector<String>();
        d.getAllMyFileNames(filenames, true, false);
        byte[] buf = new byte[1024];
        try {
            ZipOutputStream out = null;
            BufferedOutputStream outStream = null;
            SevenZip.Compression.LZMA.Encoder encoder = null;
            boolean eos = false;
            if (!useLZMA) {
                out = new ZipOutputStream(new FileOutputStream(zip));
            } else {
                outStream = new BufferedOutputStream(new FileOutputStream(new File(zip)));
                encoder = new SevenZip.Compression.LZMA.Encoder();
                encoder.SetAlgorithm(2);
                encoder.SetDictionarySize(1 << 21);
                encoder.SeNumFastBytes(128);
                encoder.SetMatchFinder(1);
                encoder.SetLcLpPb(3, 0, 2);
                encoder.SetEndMarkerMode(eos);
                encoder.WriteCoderProperties(outStream);
            }
            for (int i = 0; i < filenames.size(); i++) {
                myMaster.writeOnLog("processing : " + filenames.elementAt(i) + " to create " + filenames.elementAt(i).replace(d.getDirectory(), ""));
                if (!useLZMA) {
                    FileInputStream in = new FileInputStream(filenames.elementAt(i));
                    out.putNextEntry(new ZipEntry(filenames.elementAt(i).replace(d.getDirectory(), "")));
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.closeEntry();
                    in.close();
                } else {
                    File inFile = new File(filenames.elementAt(i));
                    BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(new File(filenames.elementAt(i))));
                    long fileSize;
                    if (eos) fileSize = -1; else fileSize = inFile.length();
                    for (int j = 0; j < 8; j++) outStream.write((int) (fileSize >>> (8 * j)) & 0xFF);
                    encoder.Code(inStream, outStream, -1, -1, null);
                    inStream.close();
                }
                myMaster.setProgress(1);
            }
            if (!useLZMA) out.close(); else {
                outStream.flush();
                outStream.close();
            }
        } catch (IOException e) {
            myMaster.writeOnLog("error while creating zip file : " + e);
            return false;
        }
        return true;
    }
