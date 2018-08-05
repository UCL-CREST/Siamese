    protected void onSubmit() {
        try {
            Connection conn = ((JdbcRequestCycle) getRequestCycle()).getConnection();
            String sql = "insert into entry (author, accessibility) values(?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, accessibility.getId());
            pstmt.executeUpdate();
            ResultSet insertedEntryIdRs = pstmt.getGeneratedKeys();
            insertedEntryIdRs.next();
            int insertedEntryId = insertedEntryIdRs.getInt(1);
            sql = "insert into revisions (title, entry, content, tags," + " revision_remark) values(?,?,?,?,?)";
            PreparedStatement pstmt2 = conn.prepareStatement(sql);
            pstmt2.setString(1, getTitle());
            pstmt2.setInt(2, insertedEntryId);
            pstmt2.setString(3, getContent());
            pstmt2.setString(4, getTags());
            pstmt2.setString(5, "newly added");
            int insertCount = pstmt2.executeUpdate();
            if (insertCount > 0) {
                info("Successfully added one new record.");
            } else {
                conn.rollback();
                info("Addition of one new record failed.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
