    public static void toExcel(JTable table, boolean swapRowColumn, String basename) {
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
            if (table.getColumnCount() > 250) swapRowColumn = true;
            PrintWriter pw = new PrintWriter(filename);
            String html = csa.util.HTMLHelper.toHTML(table, swapRowColumn);
            pw.println(html);
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
