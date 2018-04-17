    public int register(String name, String password, String email, String addr, String contactNo) {
        int uid = 0;
        int result = -1;
        try {
            getCon().setAutoCommit(false);
            if (!checkUser(name, password)) {
                PreparedStatement pstmt = getCon().prepareStatement("insert into user(uname, upass, uemail, uaddr, ucontact)" + " values (?,?,?,?,?)");
                pstmt.setString(1, name);
                pstmt.setString(2, password);
                pstmt.setString(3, email);
                pstmt.setString(4, addr);
                pstmt.setString(5, contactNo);
                int num = pstmt.executeUpdate();
                if (num == 1) {
                    ResultSet rs = pstmt.getGeneratedKeys();
                    if (rs.next()) {
                        result = rs.getInt(1);
                    }
                }
            } else {
                result = -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = -1;
            try {
                System.out.println("Transaction roll back due to errors");
                getCon().rollback();
            } catch (Exception ex) {
            }
        }
        return result;
    }
