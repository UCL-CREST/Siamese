    public static void add100RowsToSourceDB(int block) {
        Scanner source = null;
        Connection connection = null;
        try {
            try {
                Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                String dbUrl = "jdbc:odbc:conn";
                connection = DriverManager.getConnection(dbUrl);
                source = new Scanner(new File("D:/Priyanka/My/Data/SourceData.csv"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            int counter = 0;
            source.nextLine();
            int cursor = block * 100;
            while (cursor != 0) {
                cursor--;
                source.nextLine();
            }
            while (source.hasNextLine() && counter < 100) {
                Scanner ls = new Scanner(source.nextLine());
                ls.useDelimiter(",");
                String query = "INSERT INTO [CompSource].[dbo].[Reading]([hours],[lread],[lwrite],[scall],[sread],[swrite],[fork],[execCalls],[rchar],[wchar],[readingNumber]) VALUES ";
                query += "( '" + ls.next() + "', " + ls.nextInt() + ", " + ls.nextInt() + ", " + ls.nextInt() + ", " + ls.nextInt() + ", " + +ls.nextInt() + ", " + ls.nextDouble() + ", " + ls.nextDouble() + ", " + ls.nextDouble() + ", " + ls.nextDouble() + ", " + pks + ")";
                String query2 = "INSERT INTO [CompSource].[dbo].[PageReading]([pgout],[ppgout],[pgfree],[pgscan],[atch],[pgin],[readingNumber],[pageReadingNumber]) VALUES";
                query2 += "( " + ls.nextDouble() + ", " + ls.nextDouble() + ", " + ls.nextDouble() + ", " + ls.nextDouble() + ", " + ls.nextDouble() + ", " + ls.nextDouble() + ", " + pks + ", " + spks + ")";
                pks++;
                spks++;
                Statement statement = ((Connection) connection).createStatement();
                try {
                    statement.executeQuery(query);
                } catch (SQLException e) {
                }
                try {
                    Statement statement2 = ((Connection) connection).createStatement();
                    statement2.executeQuery(query2);
                } catch (SQLException e) {
                }
                counter++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
