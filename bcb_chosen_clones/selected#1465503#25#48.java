    public static void main(String[] args) {
        System.out.println("Sub- and Superscript");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("SubSupScript.pdf"));
            document.open();
            String s = "quick brown fox jumps over the lazy dog";
            StringTokenizer st = new StringTokenizer(s, " ");
            float textrise = 6.0f;
            Chunk c;
            while (st.hasMoreTokens()) {
                c = new Chunk(st.nextToken());
                c.setTextRise(textrise);
                c.setUnderline(new Color(0xC0, 0xC0, 0xC0), 0.2f, 0.0f, 0.0f, 0.0f, PdfContentByte.LINE_CAP_BUTT);
                document.add(c);
                textrise -= 2.0f;
            }
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
