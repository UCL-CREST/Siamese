    private static ArrayList tablesProcess(ArrayList text) {
        boolean isTable = false;
        Pattern p = Pattern.compile("^( )*(\\|([^\\|])+\\|)+");
        ArrayList listTable = new ArrayList();
        int maxColomns = 0, noStartTableLine = 0;
        for (int i = 0; i < text.size(); ++i) {
            String sCurrentLine = (String) text.get(i);
            Matcher m = p.matcher(sCurrentLine);
            boolean result = m.find();
            if (result) {
                if (!isTable) {
                    isTable = true;
                    listTable.clear();
                    noStartTableLine = i;
                    maxColomns = 0;
                }
                Matcher mTd = Pattern.compile("(([^\\|])+([\\|])+)").matcher(sCurrentLine);
                ArrayList listTr = new ArrayList();
                while (mTd.find()) {
                    String sTd = sCurrentLine.substring(mTd.start(), mTd.end());
                    sTd = (sTd.startsWith("|")) ? sTd.substring(1) : sTd;
                    sTd = (sTd.endsWith("|")) ? sTd.substring(0, sTd.length() - 1) : sTd;
                    listTr.add(tdProcess(sTd));
                }
                listTable.add(listTr);
                maxColomns = ((listTr.size()) > maxColomns) ? listTr.size() : maxColomns;
            } else {
                if (isTable) {
                    tableProcess(text, listTable, maxColomns, noStartTableLine);
                    isTable = false;
                }
                text.set(i, sCurrentLine);
            }
        }
        if (isTable) {
            tableProcess(text, listTable, maxColomns, noStartTableLine);
        }
        return text;
    }
