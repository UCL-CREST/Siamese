    private int renumberOrderBy(long tableID) throws SnapInException {
        int count = 0;
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = getDataSource().getConnection();
            con.setAutoCommit(false);
            stmt = con.createStatement();
            StringBuffer query = new StringBuffer();
            query.append("SELECT ").append(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_ID).append(" FROM ").append(DatabaseConstants.TableName_JV_FIELDBEHAVIOR).append(" WHERE ").append(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_TABLEID).append(" = ").append(tableID).append(" ORDER BY ").append(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_ORDERBY);
            Vector rowIDVector = new Vector();
            rs = stmt.executeQuery(query.toString());
            while (rs.next()) {
                count++;
                rowIDVector.add(rs.getLong(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_ID) + "");
            }
            StringBuffer updateString = new StringBuffer();
            updateString.append("UPDATE ").append(DatabaseConstants.TableName_JV_FIELDBEHAVIOR).append(" SET ").append(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_ORDERBY).append(" = ? WHERE ").append(DatabaseConstants.TableFieldName_JV_FIELDBEHAVIOR_ID).append(" = ?");
            PreparedStatement pstmt = con.prepareStatement(updateString.toString());
            int orderByValue = ORDERBY_BY_DELTA_VALUE;
            Enumeration en = rowIDVector.elements();
            while (en.hasMoreElements()) {
                pstmt.setInt(1, orderByValue);
                pstmt.setString(2, en.nextElement().toString());
                orderByValue += ORDERBY_BY_DELTA_VALUE;
                pstmt.executeUpdate();
            }
            con.setAutoCommit(true);
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (java.sql.SQLException e) {
            if (con == null) {
                logger.error("java.sql.SQLException", e);
            } else {
                try {
                    logger.error("Transaction is being rolled back.");
                    con.rollback();
                    con.setAutoCommit(true);
                } catch (java.sql.SQLException e2) {
                    logger.error("java.sql.SQLException", e2);
                }
            }
        } catch (Exception e) {
            logger.error("Error occured during RenumberOrderBy", e);
        } finally {
            getDataSourceHelper().releaseResources(con, stmt, rs);
        }
        return count;
    }
