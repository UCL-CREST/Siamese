    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(" *** DDL (creates) and DML (inserts) script importer from DB ***");
            System.out.println(" You must specify name of the file with script importing data");
            System.out.println(" Fisrt rows of this file must be:");
            System.out.println(" 1) JDBC driver class for your DBMS");
            System.out.println(" 2) URL for your database instance");
            System.out.println(" 3) user in that database (with sufficient priviliges)");
            System.out.println(" 4) password of that user");
            System.out.println(" Next rows can have:");
            System.out.println("   '}' before table to create,");
            System.out.println("   '{' before schema to create tables in,");
            System.out.println("   ')' before table to insert into,");
            System.out.println("   '(' before schema to insert into tables in.");
            System.out.println(" '!' before row means that it is a comment.");
            System.out.println(" If some exception is occured, all script is rolled back.");
            System.out.println(" 2nd command line argument is name of output file;");
            System.out.println("   if its extension is *.sql, its format is standard SQL");
            System.out.println("   otherwize format is short one, understanded by SQLScript tool");
            System.out.println(" Connection information remains unchanged in the last format");
            System.out.println("   but in the first one it takes form 'connect user/password@URL'");
            System.out.println("   where URL can be formed with different rools for different DBMSs");
            System.out.println(" If file (with short format header) already exists and you specify");
            System.out.println("   3rd command line argument -db, we generate objects in the database");
            System.out.println("   (known from the file header; must differ from 1st DB) but not in file");
            System.out.println(" Note: when importing to a file of short format, line separators");
            System.out.println("    in VARCHARS will be lost; LOBs will be empty for any file");
            System.exit(0);
        }
        try {
            String[] info = new String[4];
            BufferedReader reader = new BufferedReader(new FileReader(new File(args[0])));
            Writer writer = null;
            Connection outConnection = null;
            try {
                for (int i = 0; i < info.length; i++) info[i] = reader.readLine();
                try {
                    Class.forName(info[0]);
                    Connection connection = DriverManager.getConnection(info[1], info[2], info[3]);
                    int format = args[1].toLowerCase().endsWith("sql") ? SQL_FORMAT : SHORT_FORMAT;
                    File file = new File(args[1]);
                    if (format == SHORT_FORMAT) {
                        if (file.exists() && args.length > 2 && args[2].equalsIgnoreCase("-db")) {
                            String[] outInfo = new String[info.length];
                            BufferedReader outReader = new BufferedReader(new FileReader(file));
                            for (int i = 0; i < outInfo.length; i++) outInfo[i] = reader.readLine();
                            outReader.close();
                            if (!(outInfo[1].equals(info[1]) && outInfo[2].equals(info[2]))) {
                                Class.forName(info[0]);
                                outConnection = DriverManager.getConnection(outInfo[1], outInfo[2], outInfo[3]);
                                format = SQL_FORMAT;
                            }
                        }
                    }
                    if (outConnection == null) writer = new BufferedWriter(new FileWriter(file));
                    SQLImporter script = new SQLImporter(outConnection, connection);
                    script.setFormat(format);
                    if (format == SQL_FORMAT) {
                        writer.write("connect " + info[2] + "/" + info[3] + "@" + script.getDatabaseURL(info[1]) + script.statementTerminator);
                    } else {
                        for (int i = 0; i < info.length; i++) writer.write(info[i] + lineSep);
                        writer.write(lineSep);
                    }
                    try {
                        System.out.println(script.executeScript(reader, writer) + " operations with tables has been generated during import");
                    } catch (SQLException e4) {
                        reader.close();
                        if (writer != null) writer.close(); else outConnection.close();
                        System.out.println(" Script generation error: " + e4);
                    }
                    connection.close();
                } catch (Exception e3) {
                    reader.close();
                    if (writer != null) writer.close();
                    System.out.println(" Connection error: " + e3);
                }
            } catch (IOException e2) {
                System.out.println("Error in file " + args[0]);
            }
        } catch (FileNotFoundException e1) {
            System.out.println("File " + args[0] + " not found");
        }
    }
