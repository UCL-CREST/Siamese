    public void xtest1() throws Exception {
        InputStream input = new FileInputStream("C:/Documentos/j931_01.pdf");
        InputStream tmp = new ITextManager().cut(input, 3, 8);
        FileOutputStream output = new FileOutputStream("C:/temp/split.pdf");
        IOUtils.copy(tmp, output);
        input.close();
        tmp.close();
        output.close();
    }
