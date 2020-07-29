    public static void translateTableAttributes(String baseDir, String tableName, NameSpaceDefinition nsDefinition) throws Exception {
        setVosiNS(baseDir, "table_att", nsDefinition);
        String filename = baseDir + "table_att.xsl";
        Scanner s = new Scanner(new File(filename));
        PrintWriter fw = new PrintWriter(new File(baseDir + tableName + "_att.xsl"));
        while (s.hasNextLine()) {
            fw.println(s.nextLine().replaceAll("TABLENAME", tableName));
        }
        s.close();
        fw.close();
        applyStyle(baseDir + "tables.xml", baseDir + tableName + "_att.json", baseDir + tableName + "_att.xsl");
    }
