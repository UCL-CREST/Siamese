        private long getNextHighValue() throws Exception {
            Connection con = null;
            PreparedStatement psGetHighValue = null;
            PreparedStatement psUpdateHighValue = null;
            ResultSet rs = null;
            long high = -1L;
            int isolation = -1;
            DBOperation dbo = factory.createDBOperation(POOL_NAME);
            try {
                con = dbo.getConnection();
                psGetHighValue = con.prepareStatement(strGetHighValue);
                psGetHighValue.setString(1, this.name);
                for (rs = psGetHighValue.executeQuery(); rs.next(); ) {
                    high = rs.getLong("high");
                }
                psUpdateHighValue = con.prepareStatement(strUpdateHighValue);
                psUpdateHighValue.setLong(1, high + 1L);
                psUpdateHighValue.setString(2, this.name);
                psUpdateHighValue.executeUpdate();
            } catch (SQLException e) {
                if (con != null) {
                    con.rollback();
                }
                throw e;
            } finally {
                if (psUpdateHighValue != null) {
                    psUpdateHighValue.close();
                }
                close(dbo, psGetHighValue, rs);
            }
            return high;
        }
