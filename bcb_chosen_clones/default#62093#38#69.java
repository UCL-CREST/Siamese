    public WAVFile(String filename) throws Exception {
        file = filename;
        byte[] buffer = new byte[1024];
        FileInputStream fin = new FileInputStream(filename);
        int amount = fin.read(buffer);
        System.out.println("First chunk: " + amount + " bytes");
        riff = getIntASCII(buffer, 0);
        totallength = getInt(buffer, 4);
        wave = getIntASCII(buffer, 8);
        fmt = getIntASCII(buffer, FORMAT_START);
        fmt_length = getInt(buffer, FORMAT_START + 4);
        always_01 = getShort(buffer, FORMAT_START + 8);
        channels = getShort(buffer, FORMAT_START + 10);
        rate = getInt(buffer, FORMAT_START + 12);
        bytes_per_second = getInt(buffer, FORMAT_START + 16);
        bytes_per_sample = getShort(buffer, FORMAT_START + 20);
        bits_per_sample = getShort(buffer, FORMAT_START + 22);
        int DATA_START = FORMAT_START + 8 + fmt_length;
        data = getIntASCII(buffer, DATA_START);
        datalength = getInt(buffer, DATA_START + 4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(buffer, DATA_START + 8, buffer.length - (DATA_START + 8));
        amount = fin.read(buffer);
        while (amount != -1) {
            baos.write(buffer, 0, amount);
            amount = fin.read(buffer);
        }
        buffer = baos.toByteArray();
        processAudioData(buffer, 0, bytes_per_sample);
        int mult = rate / 11025;
        if ((mult * 11025) != rate) throw new RuntimeException("Rate must be multiple of 11025");
    }
