    public void writeHeader(String fileName, String host, ArrayList ffd, char decChar) {
        final String header1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<!DOCTYPE office:document-content" + " PUBLIC \"-//OpenOffice.org//DTD OfficeDocument 1.0//EN\" \"office.dtd\" >\n" + "<office:document-content xmlns:office=\"http://openoffice.org/2000/office\"" + " xmlns:style=\"http://openoffice.org/2000/style\" xmlns:text=\"http://openoffice.org/2000/text\"" + " xmlns:table=\"http://openoffice.org/2000/table\" xmlns:draw=\"http://openoffice.org/2000/drawing\"" + " xmlns:fo=\"http://www.w3.org/1999/XSL/Format\" xmlns:xlink=\"http://www.w3.org/1999/xlink\"" + " xmlns:number=\"http://openoffice.org/2000/datastyle\" xmlns:svg=\"http://www.w3.org/2000/svg\"" + " xmlns:chart=\"http://openoffice.org/2000/chart\" xmlns:dr3d=\"http://openoffice.org/2000/dr3d\"" + " xmlns:math=\"http://www.w3.org/1998/Math/MathML\" xmlns:form=\"http://openoffice.org/2000/form\"" + " xmlns:script=\"http://openoffice.org/2000/script\" office:class=\"spreadsheet\" office:version=\"1.0\">\n" + " <office:script/>\n" + " <office:font-decls>\n" + "  <style:font-decl style:name=\"Arial Unicode MS\" fo:font-family=\"&apos;Arial Unicode MS&apos;\" " + "style:font-pitch=\"variable\"/>\n" + "  <style:font-decl style:name=\"HG Mincho Light J\" fo:font-family=\"&apos;HG Mincho Light J&apos;\" " + "style:font-pitch=\"variable\"/>\n" + "  <style:font-decl style:name=\"Albany\" fo:font-family=\"Albany\" style:font-family-generic=\"swiss\" " + "style:font-pitch=\"variable\"/> \n" + " </office:font-decls>\n" + " <office:automatic-styles>\n" + "  <style:style style:name=\"co1\" style:family=\"table-column\">\n" + "   <style:properties fo:break-before=\"auto\" style:column-width=\"0.8925inch\"/>\n" + "  </style:style>\n" + "  <style:style style:name=\"ro1\" style:family=\"table-row\">\n" + "   <style:properties fo:break-before=\"auto\"/>\n" + "  </style:style>\n" + "  <style:style style:name=\"ta1\" style:family=\"table\" style:master-page-name=\"Default\">\n" + "   <style:properties table:display=\"true\"/>\n" + "  </style:style>\n";
        final String header2 = " </office:automatic-styles>\n" + " <office:body>\n" + "  <table:table table:name=\"Sheet1\" table:style-name=\"ta1\">\n" + "   <table:table-column table:style-name=\"co1\" table:number-columns-repeated=\"251\" table:default-cell-style-name=\"Default\"/>\n";
        ZipEntry zipentry = new ZipEntry("content.xml");
        zipentry.setTime(System.currentTimeMillis());
        try {
            fout.putNextEntry(zipentry);
            fout.write(header1.getBytes());
            FileFieldDef f;
            int n100 = 100;
            int ce = 0;
            String s = "";
            for (int k = 0; k < ffd.size(); k++, ce++) {
                f = (FileFieldDef) ffd.get(k);
                if (f.isWriteField()) {
                    if (f.getFieldType() == 'P' || f.getFieldType() == 'S') {
                        s = "  <number:number-style style:name=\"N" + (n100 + ce) + "\" style:family=\"data-style\" number:title=\"User-defined\">\n" + "   <number:number number:decimal-places=\"" + f.getPrecision() + "\" number:min-integer-digits=\"1\"/>\n" + "  </number:number-style>\n" + "  <style:style style:name=\"ce" + ce + "\" style:family=\"table-cell\"" + " style:parent-style-name=\"Default\" style:data-style-name=\"N" + +(n100 + ce) + "\"/>\n";
                        fout.write(s.getBytes());
                    }
                }
            }
            fout.write(header2.getBytes());
            row++;
            int column = 1;
            fout.write("   <table:table-row table:style-name=\"ro1\">\n".getBytes());
            for (int x = 0; x < ffd.size(); x++) {
                f = (FileFieldDef) ffd.get(x);
                if (f.isWriteField()) {
                    fout.write("    <table:table-cell>\n".getBytes());
                    fout.write("     <text:p>".getBytes());
                    fout.write(f.getFieldName().getBytes());
                    fout.write("</text:p>\n".getBytes());
                    fout.write("    </table:table-cell>\n".getBytes());
                }
            }
            fout.write("   </table:table-row>\n".getBytes());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
