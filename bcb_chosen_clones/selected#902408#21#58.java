    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + LocalMachine.home + "samples/itjava.db");
            java.sql.Statement stat = conn.createStatement();
            ArrayList<String> tableNames = new ArrayList<String>();
            tableNames.add("ClassInstanceTerms");
            tableNames.add("MethodInvocationTerms");
            tableNames.add("VariableDeclarationTerms");
            tableNames.add("PropertyAssignmentTerms");
            tableNames.add("ImportTerms");
            tableNames.add("SuperTypeTerms");
            for (String tableName : tableNames) {
                stat.executeUpdate("drop table if exists " + tableName + ";");
                stat.executeUpdate("create table " + tableName + " (term varchar(30)  PRIMARY KEY DESC, numOccurrences int);");
                System.out.println(tableName + " dropped and created");
            }
            stat.executeUpdate("drop table if exists Documents;");
            stat.executeUpdate("create table Documents (fileName INTEGER PRIMARY KEY AUTOINCREMENT, url varchar(256));");
            System.out.println("Documents dropped and created");
            conn.close();
            File samplesDirectory = new File("samples");
            if (samplesDirectory.isDirectory()) {
                File[] sampleFiles = samplesDirectory.listFiles(new FilenameFilter() {

                    @Override
                    public boolean accept(File arg0, String arg1) {
                        return (arg1.endsWith(".sample")) ? true : false;
                    }
                });
                for (File file : sampleFiles) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
