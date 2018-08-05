    private static void insertFiles(Connection con, File file) throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader(file));
        String line = bf.readLine();
        while (line != null) {
            if (!line.startsWith(" ") && !line.startsWith("#")) {
                try {
                    System.out.println("Exec: " + line);
                    PreparedStatement prep = con.prepareStatement(line);
                    prep.executeUpdate();
                    prep.close();
                    con.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        con.rollback();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            line = bf.readLine();
        }
        bf.close();
    }
