    public static void main(String[] args) throws Exception {
        DES des = new DES();
        StreamBlockReader reader = new StreamBlockReader(new FileInputStream("D:\\test1.txt"));
        StreamBlockWriter writer = new StreamBlockWriter(new FileOutputStream("D:\\test2.txt"));
        SingleKey key = new SingleKey(new Block(64), "");
        key = new SingleKey(new Block("1111111100000000111111110000000011111111000000001111111100000000"), "");
        Mode mode = new ECBDESMode(des);
        des.decrypt(reader, writer, key, mode);
    }
