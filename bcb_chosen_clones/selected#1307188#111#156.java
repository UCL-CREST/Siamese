    @Override
    public final void close() {
        if (this.readZipFile != null) {
            try {
                this.readZipFile.close();
                this.readZipFile = null;
            } catch (final IOException e) {
                throw new BufferedDataError(e);
            }
        }
        if (this.zos != null) {
            try {
                final ZipEntry theEntry = new ZipEntry("xl/worksheets/sheet1.xml");
                this.xmlOut.endTag();
                this.xmlOut.addAttribute("left", "0.7");
                this.xmlOut.addAttribute("right", "0.7");
                this.xmlOut.addAttribute("top", "0.75");
                this.xmlOut.addAttribute("bottom", "0.75");
                this.xmlOut.addAttribute("header", "0.3");
                this.xmlOut.addAttribute("footer", "0.3");
                this.xmlOut.beginTag("pageMargins");
                this.xmlOut.endTag();
                this.xmlOut.endTag();
                this.xmlOut.endDocument();
                final byte[] b = this.buffer.toByteArray();
                theEntry.setSize(b.length);
                theEntry.setCompressedSize(-1);
                theEntry.setMethod(ZipEntry.DEFLATED);
                this.zos.putNextEntry(theEntry);
                this.zos.write(b);
                this.zos.closeEntry();
                this.zos.close();
                this.zos = null;
            } catch (final IOException e) {
                throw new BufferedDataError(e);
            }
        }
        if (this.fos != null) {
            try {
                this.fos.close();
                this.fos = null;
            } catch (final IOException e) {
                throw new BufferedDataError(e);
            }
        }
    }
