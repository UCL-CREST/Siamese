    public static void htmlToPDF(String html, OutputStream outputStream) throws Exception {
        Document document = new Document(PageSize.A4);
        try {
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            writer.setPageEvent(new PageNumbersWatermark(bfChinese, "Teemlink"));
            document.open();
            StyleSheet st = new StyleSheet();
            st.loadTagStyle("body", ElementTags.FACE, "STSong-Light");
            st.loadTagStyle("body", ElementTags.ENCODING, "UniGB-UCS2-H");
            st.loadTagStyle("body", ElementTags.SIZE, "12f");
            HashMap props0 = new HashMap();
            props0.put("border", "1f");
            props0.put("width", "100%");
            props0.put("font-size", "12f");
            st.loadStyle("display_view-table", props0);
            HashMap props1 = new HashMap();
            props1.put("text-align", "left");
            props1.put("size", "12f");
            props1.put("bgcolor", "#eeeeee");
            props1.put("color", "#1268a5");
            st.loadStyle("column-head", props1);
            HashMap props2 = new HashMap();
            props2.put("text-align", "left");
            props2.put("size", "12f");
            st.loadStyle("column-td", props2);
            ArrayList p = HTMLWorker.parseToList(new StringReader(html), st);
            for (int k = 0; k < p.size(); ++k) {
                Element el = (Element) p.get(k);
                document.add(el);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
