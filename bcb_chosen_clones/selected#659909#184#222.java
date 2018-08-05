    public void openAndClose(ZKEntry zke, LinkedList toOpen, LinkedList toRemove) throws SQLException {
        conn.setAutoCommit(false);
        try {
            Statement stm = conn.createStatement();
            ResultSet rset = stm.executeQuery("SELECT now();");
            rset.next();
            Timestamp now = rset.getTimestamp("now()");
            for (int i = 0; i < toRemove.size(); i++) {
                Workitem wi = (Workitem) toRemove.get(i);
                rset = stm.executeQuery("SELECT intime, part FROM stampzk WHERE stampzkid = '" + wi.getStampZkId() + "';");
                rset.next();
                long diff = now.getTime() - rset.getLong("intime");
                float diffp = diff * rset.getFloat("part");
                stm.executeUpdate("UPDATE stampzk SET outtime='" + now.getTime() + "', diff='" + diff + "', diffp='" + diffp + "' WHERE stampzkid='" + wi.getStampZkId() + "';");
            }
            rset = stm.executeQuery("SELECT COUNT(*) FROM stampzk WHERE personalid='" + zke.getWorker().getPersonalId() + "' AND outtime='0';");
            rset.next();
            int count = rset.getInt("COUNT(*)") + toOpen.size();
            rset = stm.executeQuery("SELECT * FROM stampzk WHERE personalid='" + zke.getWorker().getPersonalId() + "' AND outtime='0';");
            while (rset.next()) {
                long diff = now.getTime() - rset.getLong("intime");
                float diffp = diff * rset.getFloat("part");
                int firstId = rset.getInt("firstid");
                if (firstId == 0) firstId = rset.getInt("stampzkid");
                Statement ust = conn.createStatement();
                ust.executeUpdate("UPDATE stampzk SET outtime='" + now.getTime() + "', diff='" + diff + "', diffp='" + diffp + "' WHERE stampzkid='" + rset.getInt("stampzkid") + "';");
                ust.executeUpdate("INSERT INTO stampzk SET zeitkid='" + rset.getInt("zeitkid") + "', personalid='" + zke.getWorker().getPersonalId() + "', funcsid='" + rset.getInt("funcsid") + "', part='" + (float) 1f / count + "', intime='" + now.getTime() + "', firstid='" + firstId + "';");
            }
            for (int i = 0; i < toOpen.size(); i++) {
                stm.executeUpdate("INSERT INTO stampzk SET zeitkid='" + zke.getZeitKId() + "', personalid='" + zke.getWorker().getPersonalId() + "', intime='" + now.getTime() + "', funcsid='" + ((Workitem) toOpen.get(i)).getWorkType() + "', part='" + (float) 1f / count + "';");
            }
        } catch (SQLException sqle) {
            conn.rollback();
            conn.setAutoCommit(true);
            throw sqle;
        }
        conn.commit();
        conn.setAutoCommit(true);
    }
