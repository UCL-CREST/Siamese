    public synchronized void deleteDocument(final String file) throws IOException {
        SQLException ex = null;
        try {
            PreparedStatement findFileStmt = con.prepareStatement("SELECT ID AS \"ID\" FROM File_ WHERE Name = ?");
            findFileStmt.setString(1, file);
            ResultSet rs = findFileStmt.executeQuery();
            if (null != rs && rs.next()) {
                int fileId = rs.getInt("ID");
                rs.close();
                rs = null;
                PreparedStatement deleteTokensStmt = con.prepareStatement("DELETE FROM Token_ WHERE FieldID IN ( SELECT ID FROM Field_ WHERE FileID = ? )");
                deleteTokensStmt.setInt(1, fileId);
                deleteTokensStmt.executeUpdate();
                PreparedStatement deleteFieldsStmt = con.prepareStatement("DELETE FROM Field_ WHERE FileID = ?");
                deleteFieldsStmt.setInt(1, fileId);
                deleteFieldsStmt.executeUpdate();
                PreparedStatement deleteFileStmt = con.prepareStatement("DELETE FROM File_ WHERE ID = ?");
                deleteFileStmt.setInt(1, fileId);
                deleteFileStmt.executeUpdate();
                deleteFileStmt.close();
                deleteFileStmt = null;
                deleteFieldsStmt.close();
                deleteFieldsStmt = null;
                deleteTokensStmt.close();
                deleteTokensStmt = null;
            }
            findFileStmt.close();
            findFileStmt = null;
        } catch (SQLException e) {
            e.printStackTrace();
            ex = e;
            try {
                this.con.rollback();
            } catch (SQLException e2) {
            }
        } finally {
            try {
                this.con.setAutoCommit(true);
            } catch (SQLException e2) {
            }
        }
        if (null != ex) throw new IOException(ex.getMessage());
    }
