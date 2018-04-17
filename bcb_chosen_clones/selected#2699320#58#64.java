    public void test3() throws FileNotFoundException, IOException {
        Decoder decoder1 = new MP3Decoder(new FileInputStream("/home/marc/tmp/test1.mp3"));
        Decoder decoder2 = new OggDecoder(new FileInputStream("/home/marc/tmp/test1.ogg"));
        FileOutputStream out = new FileOutputStream("/home/marc/tmp/test.pipe");
        IOUtils.copy(decoder1, out);
        IOUtils.copy(decoder2, out);
    }
