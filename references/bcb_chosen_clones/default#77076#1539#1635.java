    private boolean doStudentCreditUpdate(Double dblCAmnt, String stuID) throws Exception {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        Boolean blOk = false;
        String strMessage = "";
        try {
            conn = dbMan.getPOSConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            String host = getHostName();
            String stuId = student.getStudentNumber();
            String building = settings.get(DBSettings.MAIN_BUILDING);
            String cashier = dbMan.getPOSUser();
            if (hasStudentCredit()) {
                stmt = conn.createStatement();
                if (stmt.executeUpdate("UPDATE " + strPOSPrefix + "studentcredit set credit_amount = credit_amount + " + round2Places(dblCAmnt) + " WHERE credit_active = '1' and credit_studentid = '" + stuId + "'") == 1) {
                    stmt.close();
                    stmt = conn.createStatement();
                    if (stmt.executeUpdate("UPDATE " + strPOSPrefix + "studentcredit set credit_lastused = NOW() where credit_active = '1' and credit_studentid = '" + stuId + "'") == 1) {
                        stmt.close();
                        stmt = conn.createStatement();
                        if (stmt.executeUpdate("INSERT into " + strPOSPrefix + "studentcredit_log ( scl_studentid, scl_action, scl_datetime ) values( '" + stuId + "', '" + round2Places(dblCAmnt) + "', NOW() )") == 1) {
                            stmt.close();
                            blOk = true;
                        } else {
                            strMessage = "Unable to update student credit log.";
                            blOk = false;
                        }
                    } else {
                        strMessage = "Unable to update student credit account.";
                        blOk = false;
                    }
                } else {
                    strMessage = "Unable to update student credit account.";
                    blOk = false;
                }
            } else {
                stmt = conn.createStatement();
                if (stmt.executeUpdate("insert into " + strPOSPrefix + "studentcredit (credit_amount,credit_active,credit_studentid,credit_lastused) values('" + round2Places(dblCAmnt) + "','1','" + stuId + "', NOW())") == 1) {
                    stmt.close();
                    stmt = conn.createStatement();
                    if (stmt.executeUpdate("insert into " + strPOSPrefix + "studentcredit_log ( scl_studentid, scl_action, scl_datetime ) values( '" + stuId + "', '" + round2Places(dblCAmnt) + "', NOW() )") == 1) {
                        stmt.close();
                        blOk = true;
                    } else {
                        strMessage = "Unable to update student credit log.";
                        blOk = false;
                    }
                } else {
                    strMessage = "Unable to create new student credit account.";
                    blOk = false;
                }
            }
            if (blOk) {
                stmt = conn.createStatement();
                if (stmt.executeUpdate("insert into " + strPOSPrefix + "creditTrans ( ctStudentNumber, ctCreditAction, ctBuilding, ctRegister, ctUser, ctDateTime ) values( '" + stuId + "', '" + round2Places(dblCAmnt) + "', '" + building + "', '" + host + "', '" + cashier + "', NOW() )") == 1) {
                    stmt.close();
                    blOk = true;
                } else blOk = false;
            }
            if (blOk) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                throw new Exception("Error detected during credit adjustment!  " + strMessage);
            }
        } catch (Exception exp) {
            try {
                conn.rollback();
            } catch (SQLException sqlEx2) {
                System.err.println("Rollback failed: " + sqlEx2.getMessage());
                return false;
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException sqlEx) {
                        rs = null;
                    }
                    if (stmt != null) {
                        try {
                            stmt.close();
                        } catch (SQLException sqlEx) {
                            stmt = null;
                        } catch (Exception e) {
                            System.err.println("Exception: " + e.getMessage());
                            System.err.println(e);
                        }
                    }
                }
            }
            exp.printStackTrace();
            throw new Exception("Error detected during credit adjustment: " + exp.getMessage());
        }
    }
