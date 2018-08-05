    public void decode(File input) throws DecoderException {
        try {
            YEncFile yencFile = new YEncFile(input);
            StringBuffer outputName = new StringBuffer(outputDirName);
            outputName.append(File.separator);
            outputName.append(yencFile.getHeader().getName());
            RandomAccessFile output = new RandomAccessFile(outputName.toString(), "rw");
            long pos = yencFile.getDataBegin();
            yencFile.getInput().seek(pos);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (pos < yencFile.getDataEnd()) {
                baos.write(yencFile.getInput().read());
                pos++;
            }
            byte[] buff = decoder.decode(baos.toByteArray());
            output.write(buff);
            output.close();
            CRC32 crc32 = new CRC32();
            crc32.update(buff);
            if (!yencFile.getTrailer().getCrc32().equals(Long.toHexString(crc32.getValue()).toUpperCase())) throw new DecoderException("Error in CRC32 check.");
        } catch (YEncException ye) {
            throw new DecoderException("Input file is not a YEnc file or contains error : " + ye.getMessage());
        } catch (FileNotFoundException fnfe) {
            throw new DecoderException("Enable to create output file : " + fnfe.getMessage());
        } catch (IOException ioe) {
            throw new DecoderException("Enable to read input file : " + ioe.getMessage());
        }
    }
