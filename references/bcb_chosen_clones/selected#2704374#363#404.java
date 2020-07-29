    protected void generateDiffResults(File outFile) throws IOException {
        FileOutputStream outStream = new FileOutputStream(outFile);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outStream, outputCharset));
        intlWrite(out, HTML_STRICT_DOCTYPE + "<html><head><title>${Report.Title}</title>\n" + "<meta http-equiv=\"Content-Type\"" + " content=\"text/html; charset=" + outputCharset + "\">\n" + "<style type=\"text/css\">\n");
        out.write(LOCDiff.getCssText());
        writeCustomStyleInfo(out);
        out.write("</style></head>\n" + "<body>\n" + "<div>\n");
        if (addedTable.length() > 0) {
            out.write("<table border>");
            intlWrite(out, getAddedTableHeader());
            out.write(addedTable.toString());
            out.write("</table><br><br>");
        }
        if (modifiedTable.length() > 0) {
            out.write("<table border>");
            intlWrite(out, getModifiedTableHeader());
            out.write(modifiedTable.toString());
            out.write("</table><br><br>");
        }
        if (deletedTable.length() > 0) {
            out.write("<table border>");
            intlWrite(out, getDeletedTableHeader());
            out.write(deletedTable.toString());
            out.write("</table><br><br>");
        }
        if (unchangedTable.length() > 0) {
            out.write("<table border>");
            intlWrite(out, getUnchangedTableHeader());
            out.write(unchangedTable.toString());
            out.write("</table><br><br>");
        }
        writeSummaryTable(out);
        out.write("</div>");
        redlinesOut.close();
        out.flush();
        InputStream redlines = new FileInputStream(redlinesTempFile);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = redlines.read(buffer)) != -1) outStream.write(buffer, 0, bytesRead);
        outStream.write("</BODY></HTML>".getBytes());
        outStream.close();
    }
