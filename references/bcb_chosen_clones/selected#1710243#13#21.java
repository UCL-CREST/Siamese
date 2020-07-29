    public static void main(String[] args) throws Exception {
        TripleDES tdes = new TripleDES();
        StreamBlockReader reader = new StreamBlockReader(new FileInputStream("D:\\testTDESENC.txt"));
        StreamBlockWriter writer = new StreamBlockWriter(new FileOutputStream("D:\\testTDESDEC.txt"));
        SingleKey key = new SingleKey(new Block(128), "");
        key = new SingleKey(new Block("01011101110000101001100111001011101000001110111101001001101101101101100000011101100100110000101100001110000001111101001101001101"), "");
        Mode mode = new ECBTripleDESMode(tdes);
        tdes.decrypt(reader, writer, key, mode);
    }
