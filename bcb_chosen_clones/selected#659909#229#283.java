    public float stampPerson(PEntry pe) throws SQLException {
        conn.setAutoCommit(false);
        float result;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery("SELECT now();");
            rset.next();
            Timestamp now = rset.getTimestamp("now()");
            Calendar cal = new GregorianCalendar();
            cal.setTime(now);
            if (pe.getState() != 0) {
                for (int i = 0; i < pe.getOpenItems().size(); i++) {
                    Workitem wi = (Workitem) pe.getOpenItems().get(i);
                    long diff = now.getTime() - wi.getIntime();
                    float diffp = diff * (float) 1f / pe.getOpenItems().size();
                    stmt.executeUpdate("UPDATE stampzk SET outtime='" + now.getTime() + "', diff='" + diff + "', diffp='" + diffp + "' WHERE stampzkid='" + wi.getStampZkId() + "';");
                }
                rset = stmt.executeQuery("SELECT intime FROM stamppersonal WHERE stamppersonalid='" + pe.getState() + "';");
                rset.next();
                long inDate = rset.getLong("intime");
                long diff = (now.getTime() - inDate);
                stmt.executeUpdate("UPDATE stamppersonal SET outtime='" + now.getTime() + "', diff='" + diff + "' WHERE stamppersonalid='" + pe.getState() + "';");
                stmt.executeUpdate("UPDATE personal SET stamppersonalid='0' WHERE personalnr='" + pe.getPersonalId() + "';");
                stmt.executeUpdate("UPDATE personalyearworktime SET worktime=worktime+" + (float) diff / 3600000f + " WHERE year=" + cal.get(Calendar.YEAR) + " AND personalid='" + pe.getPersonalId() + "';");
                rset = stmt.executeQuery("SELECT SUM(diff) AS twt FROM stamppersonal WHERE personalid='" + pe.getPersonalId() + "' AND datum='" + cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "';");
                rset.next();
                result = (float) rset.getInt("twt") / 3600000f;
            } else {
                stmt.executeUpdate("INSERT INTO stamppersonal SET personalid='" + pe.getPersonalId() + "', intime='" + now.getTime() + "', datum='" + cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "';");
                rset = stmt.executeQuery("SELECT stamppersonalid FROM stamppersonal WHERE personalid='" + pe.getPersonalId() + "' AND outtime='0' ORDER BY stamppersonalid DESC LIMIT 1;");
                rset.next();
                int sppid = rset.getInt("stamppersonalid");
                stmt.executeUpdate("UPDATE personal SET stamppersonalid='" + sppid + "' WHERE personalnr='" + pe.getPersonalId() + "';");
                Calendar yest = new GregorianCalendar();
                yest.setTime(now);
                yest.add(Calendar.DAY_OF_YEAR, -1);
                rset = stmt.executeQuery("SELECT SUM(diff) AS twt FROM stamppersonal WHERE personalid='" + pe.getPersonalId() + "' AND datum='" + cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "';");
                rset.next();
                float today = (float) rset.getInt("twt") / 3600000f;
                rset = stmt.executeQuery("SELECT worktime FROM personalyearworktime WHERE personalid='" + pe.getPersonalId() + "' AND year='" + cal.get(Calendar.YEAR) + "';");
                rset.next();
                float ist = rset.getFloat("worktime") - today;
                rset = stmt.executeQuery("SELECT duetime FROM dueworktime WHERE datum='" + yest.get(Calendar.YEAR) + "-" + (yest.get(Calendar.MONTH) + 1) + "-" + yest.get(Calendar.DAY_OF_MONTH) + "' AND personalid='" + pe.getPersonalId() + "';");
                rset.next();
                result = ist - rset.getFloat("duetime");
            }
        } catch (SQLException sqle) {
            conn.rollback();
            conn.setAutoCommit(true);
            throw sqle;
        }
        conn.commit();
        conn.setAutoCommit(true);
        return result;
    }
