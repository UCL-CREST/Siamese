    private void displayDiffResults() throws IOException {
        File outFile = File.createTempFile("diff", ".htm");
        outFile.deleteOnExit();
        FileOutputStream outStream = new FileOutputStream(outFile);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outStream));
        out.write("<html><head><title>LOC Differences</title>\n" + SCRIPT + "</head>\n" + "<body bgcolor='#ffffff'>\n" + "<div onMouseOver=\"window.defaultStatus='Metrics'\">\n");
        if (addedTable.length() > 0) {
            out.write("<table border><tr><th>Files Added:</th>" + "<th>Add</th><th>Type</th></tr>");
            out.write(addedTable.toString());
            out.write("</table><br><br>");
        }
        if (modifiedTable.length() > 0) {
            out.write("<table border><tr><th>Files Modified:</th>" + "<th>Base</th><th>Del</th><th>Mod</th><th>Add</th>" + "<th>Total</th><th>Type</th></tr>");
            out.write(modifiedTable.toString());
            out.write("</table><br><br>");
        }
        if (deletedTable.length() > 0) {
            out.write("<table border><tr><th>Files Deleted:</th>" + "<th>Del</th><th>Type</th></tr>");
            out.write(deletedTable.toString());
            out.write("</table><br><br>");
        }
        out.write("<table name=METRICS BORDER>\n");
        if (modifiedTable.length() > 0 || deletedTable.length() > 0) {
            out.write("<tr><td>Base:&nbsp;</td><td>");
            out.write(Long.toString(base));
            out.write("</td></tr>\n<tr><td>Deleted:&nbsp;</td><td>");
            out.write(Long.toString(deleted));
            out.write("</td></tr>\n<tr><td>Modified:&nbsp;</td><td>");
            out.write(Long.toString(modified));
            out.write("</td></tr>\n<tr><td>Added:&nbsp;</td><td>");
            out.write(Long.toString(added));
            out.write("</td></tr>\n<tr><td>New & Changed:&nbsp;</td><td>");
            out.write(Long.toString(added + modified));
            out.write("</td></tr>\n");
        }
        out.write("<tr><td>Total:&nbsp;</td><td>");
        out.write(Long.toString(total));
        out.write("</td></tr>\n</table></div>");
        redlinesOut.close();
        out.flush();
        InputStream redlines = new FileInputStream(redlinesTempFile);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = redlines.read(buffer)) != -1) outStream.write(buffer, 0, bytesRead);
        outStream.write("</BODY></HTML>".getBytes());
        outStream.close();
        Browser.launch(outFile.toURL().toString());
    }
