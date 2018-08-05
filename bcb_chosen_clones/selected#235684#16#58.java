    public static void toExcel(Vector<JTable> tables, String basename) {
        String cName = "";
        String filename = "";
        File f = null;
        do {
            filename = "tmp\\temp_excel_" + basename + "_" + counter + ".xls";
            filename = csa.util.UtilityString.cleanFileString(filename);
            f = new File(filename);
            counter++;
        } while (f.isFile());
        System.out.println("Temp Filename used: " + filename);
        try {
            PrintWriter pw = new PrintWriter(filename);
            pw.println("<P>List Generated: " + csa.util.UtilityDate.dateToStringGermanClock(new Date()) + "</P>");
            boolean swapRowColumn = false;
            for (int t = 0; t < tables.size(); t++) {
                JTable table = tables.elementAt(t);
                if (table.getColumnCount() > 250) swapRowColumn = true;
                String html = csa.util.HTMLHelper.toHTML(table, swapRowColumn);
                pw.println("<P></P>");
                pw.println(html);
            }
            pw.flush();
            pw.close();
            String log = "";
            String result = "";
            f = new File(filename);
            if (f == null) return;
            Desktop desktop = null;
            try {
                if (Desktop.isDesktopSupported()) {
                    desktop = Desktop.getDesktop();
                    desktop.open(f);
                }
            } catch (IOException e) {
                log += System.err.toString();
                e.printStackTrace(System.err);
            }
        } catch (Throwable e) {
            System.out.println(e);
            e.printStackTrace(System.out);
        }
    }
