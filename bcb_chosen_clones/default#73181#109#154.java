    public static void add100RowsToTargetDB(int block) {
        Scanner source = null;
        Connection connection = null;
        try {
            try {
                Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                String dbUrl = "jdbc:odbc:conn";
                connection = DriverManager.getConnection(dbUrl);
                source = new Scanner(new File("D:/Priyanka/My/Data/TargetData.csv"));
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
                String query = "INSERT INTO [CompTarget].[dbo].[ReadingsT] ([time],[reads],[writes],[sysCall],[sysRead],[sysWrite],[random1],[numExecs] ,[rchar],[wchar],[reading_id]) VALUES ";
                query += "( '" + ls.next() + "', " + ls.nextInt() + ", " + ls.nextInt() + ", " + ls.nextInt() + ", " + ls.nextInt() + ", " + +ls.nextInt() + ", " + ls.nextDouble() + ", " + ls.nextDouble() + ", " + ls.nextDouble() + ", " + ls.nextDouble() + ", " + pkt + ")";
                String query2 = "INSERT INTO [CompTarget].[dbo].[PageReadingT]([pageout],[pagedout],[pagefree],[pagescan],[pageattach],[pagein],[main_reading_id],[page_reading_id]) VALUES";
                query2 += "( " + ls.nextDouble() + ", " + ls.nextDouble() + ", " + ls.nextDouble() + ", " + ls.nextDouble() + ", " + ls.nextDouble() + ", " + ls.nextDouble() + ", " + pkt + ", " + spkt + ")";
                pkt++;
                spkt++;
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
