                @Override
                public void run() {
                    while (true) {
                        StringBuilder buffer = new StringBuilder(128);
                        buffer.append("insert into DOMAIN (                         ").append(LS);
                        buffer.append("    DOMAIN_ID, TOP_DOMAIN_ID, DOMAIN_HREF,   ").append(LS);
                        buffer.append("    DOMAIN_RANK, DOMAIN_TYPE, DOMAIN_STATUS, ").append(LS);
                        buffer.append("    DOMAIN_ICO_CREATED, DOMAIN_CDATE         ").append(LS);
                        buffer.append(") values (                   ").append(LS);
                        buffer.append("    null ,null, ?,").append(LS);
                        buffer.append("    1, 2, 1,                 ").append(LS);
                        buffer.append("    0, now()                 ").append(LS);
                        buffer.append(")                            ").append(LS);
                        String sqlInsert = buffer.toString();
                        boolean isAutoCommit = false;
                        int i = 0;
                        Connection conn = null;
                        PreparedStatement pstmt = null;
                        ResultSet rs = null;
                        try {
                            conn = ConnHelper.getConnection();
                            conn.setAutoCommit(isAutoCommit);
                            pstmt = conn.prepareStatement(sqlInsert);
                            for (i = 0; i < 10; i++) {
                                String lock = "" + ((int) (Math.random() * 100000000)) % 100;
                                pstmt.setString(1, lock);
                                pstmt.executeUpdate();
                            }
                            if (!isAutoCommit) conn.commit();
                            rs = pstmt.executeQuery("select max(DOMAIN_ID) from DOMAIN");
                            if (rs.next()) {
                                String str = System.currentTimeMillis() + " " + rs.getLong(1);
                            }
                        } catch (Exception e) {
                            try {
                                if (!isAutoCommit) conn.rollback();
                            } catch (SQLException ex) {
                                ex.printStackTrace(System.out);
                            }
                            String msg = System.currentTimeMillis() + " " + Thread.currentThread().getName() + " - " + i + " " + e.getMessage() + LS;
                            FileIO.writeToFile("D:/DEAD_LOCK.txt", msg, true, "GBK");
                        } finally {
                            ConnHelper.close(conn, pstmt, rs);
                        }
                    }
                }
