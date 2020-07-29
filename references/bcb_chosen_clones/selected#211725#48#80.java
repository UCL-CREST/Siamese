    public void testGetTagData() {
        FileInputStream theStream = null;
        try {
            theStream = new FileInputStream("test_data/testID3_01_Winamp5.04Tag.id3");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail("input file Not found");
        }
        Id3v2Tag test;
        try {
            test = new Id3v2Tag(theStream.getChannel());
            System.out.println(test.getFrame("TIT2"));
            FileOutputStream theFile = null;
            try {
                theFile = new FileOutputStream("test_data/test_01_JunitID3.id3");
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                fail("output file Not found");
            }
            try {
                test.getTagData().writeTo(theFile);
                theStream = new FileInputStream("test_data/testID3_01_Winamp5.04Tag_withoutpadding.id3");
                byte[] shouldByte = new byte[theStream.available()], isByte = test.getTagData().toByteArray();
                theStream.read(shouldByte);
                UnitTestHelper.compareByteArray(shouldByte, isByte);
            } catch (IOException e) {
                e.printStackTrace();
                fail("IO Exception when wirting to file");
            }
        } catch (InstantiationException e2) {
            e2.printStackTrace();
        }
    }
