    public static void main(String[] argv) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XmlPullParser xp = new KXmlParser();
        xp.setInput(new FileInputStream(argv[0]), null);
        XmlSerializer xs = new WbxmlSerializer();
        xs.setOutput(bos, null);
        new Roundtrip(xp, xs).roundTrip();
        byte[] wbxml = bos.toByteArray();
        System.out.println("********* WBXML size: " + wbxml.length + " ***********");
        for (int i = 0; i < wbxml.length; i += 16) {
            for (int j = i; j < Math.min(i + 16, wbxml.length); j++) {
                int b = ((int) wbxml[j]) & 0x0ff;
                System.out.print(Integer.toHexString(b / 16));
                System.out.print(Integer.toHexString(b % 16));
                System.out.print(' ');
            }
            for (int j = i; j < Math.min(i + 16, wbxml.length); j++) {
                int b = wbxml[j];
                System.out.print(b >= 32 && b <= 127 ? (char) b : '?');
            }
            System.out.println();
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(wbxml);
        xp = new WbxmlParser();
        xp.setInput(bis, null);
        xs = new KXmlSerializer();
        xs.setOutput(System.out, null);
        new Roundtrip(xp, xs).roundTrip();
    }
