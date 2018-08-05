    public void saveProject(File file) throws IOException {
        if (mixerSerializer == null) {
            System.out.println(" Creating serialization for the mixer ");
            mixerSerializer = new TootMixerSerializer(this);
        }
        int usecompression = compression_level;
        if (usecompression == 2) {
            File tempfile = File.createTempFile("lzma", "temp");
            FileInputStream fis = null;
            try {
                FileOutputStream fos = new FileOutputStream(tempfile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                try {
                    oos.writeObject(this);
                } finally {
                    oos.close();
                    fos.close();
                }
                fis = new FileInputStream(tempfile);
                byte[] magic = new byte[4];
                magic[0] = (byte) 0x4c;
                magic[1] = (byte) 0x5a;
                magic[2] = (byte) 0x4d;
                magic[3] = (byte) 0x61;
                FileOutputStream outStream = new FileOutputStream(file);
                outStream.write(magic);
                InputStream inStream = fis;
                boolean eos = false;
                SevenZip.Compression.LZMA.Encoder encoder = new SevenZip.Compression.LZMA.Encoder();
                encoder.SetAlgorithm(2);
                encoder.SetDictionarySize(1 << 23);
                encoder.SeNumFastBytes(128);
                encoder.SetMatchFinder(1);
                encoder.SetLcLpPb(3, 0, 2);
                encoder.SetEndMarkerMode(eos);
                encoder.WriteCoderProperties(outStream);
                long fileSize = tempfile.length();
                for (int i = 0; i < 8; i++) {
                    outStream.write((int) (fileSize >>> (8 * i)) & 0xFF);
                }
                encoder.Code(inStream, outStream, -1, -1, null);
                outStream.close();
            } finally {
                if (fis != null) {
                    fis.close();
                }
                tempfile.delete();
            }
        } else if (usecompression == 1) {
            FileOutputStream fos = new FileOutputStream(file);
            ZipOutputStream zos = new ZipOutputStream(fos);
            zos.putNextEntry(new ZipEntry(file.getName()));
            BufferedOutputStream bos = new BufferedOutputStream(zos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            bos.flush();
            zos.closeEntry();
            zos.finish();
            fos.close();
        } else {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
            fos.close();
        }
        projectFile = file;
        editHistoryContainer.updateSavedPosition();
    }
