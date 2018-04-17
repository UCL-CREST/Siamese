    @Override
    public final void prepareWrite(final int recordCount, final int inputSize, final int idealSize) {
        this.inputCount = inputSize;
        this.idealCount = idealSize;
        try {
            this.fos = new FileOutputStream(this.file);
            this.zos = new ZipOutputStream(this.fos);
            final InputStream is = ResourceInputStream.openResourceInputStream("org/encog/data/blank.xlsx");
            final ZipInputStream zis = new ZipInputStream(is);
            ZipEntry theEntry;
            while (zis.available() > 0) {
                theEntry = zis.getNextEntry();
                if ((entry != null) && !"xl/worksheets/sheet1.xml".equals(entry.getName())) {
                    final ZipEntry entry2 = new ZipEntry(theEntry);
                    entry2.setCompressedSize(-1);
                    this.zos.putNextEntry(entry2);
                    final byte[] theBuffer = new byte[(int) entry.getSize()];
                    zis.read(theBuffer);
                    this.zos.write(theBuffer);
                    this.zos.closeEntry();
                }
            }
            zis.close();
            this.buffer = new ByteArrayOutputStream();
            this.xmlOut = new WriteXML(this.buffer);
            this.xmlOut.beginDocument();
            this.xmlOut.addAttribute("xmlns", "http://schemas.openxmlformats.org/spreadsheetml/2006/main");
            this.xmlOut.addAttribute("xmlns:r", "http://schemas.openxmlformats.org/officeDocument/2006/relationships");
            this.xmlOut.beginTag("worksheet");
            final StringBuilder d = new StringBuilder();
            d.append(toColumn(this.inputCount + this.idealCount));
            d.append("" + recordCount);
            this.xmlOut.addAttribute("ref", "A1:" + d.toString());
            this.xmlOut.beginTag("dimension");
            this.xmlOut.endTag();
            this.xmlOut.beginTag("sheetViews");
            this.xmlOut.addAttribute("tabSelected", "1");
            this.xmlOut.addAttribute("workbookViewId", "0");
            this.xmlOut.beginTag("sheetView");
            this.xmlOut.endTag();
            this.xmlOut.endTag();
            this.xmlOut.addAttribute("defaultRowHeight", "15");
            this.xmlOut.beginTag("sheetFormatPtr");
            this.xmlOut.endTag();
            this.row = 1;
            this.xmlOut.beginTag("sheetData");
        } catch (final IOException ex) {
            throw new BufferedDataError(ex);
        }
    }
