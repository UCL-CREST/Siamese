    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("参数错误");
            return;
        }
        Vector table = new Vector();
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql:liudb", "liu", null);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT 字, 码 FROM " + args[0]);
            while (rs.next()) {
                table.addElement(new Zima(rs.getString(1), rs.getString(2)));
            }
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(args[1]));
            out.writeObject(table);
            out.close();
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getSQLState());
            return;
        } catch (FileNotFoundException f) {
            System.out.println(f.getMessage());
            return;
        } catch (IOException i) {
            System.out.println(i.getMessage());
            return;
        }
    }
