    public PTask stop(PTask task, SyrupConnection con) throws Exception {
        PreparedStatement s = null;
        ResultSet result = null;
        try {
            s = con.prepareStatementFromCache(sqlImpl().sqlStatements().checkWorkerStatement());
            s.setString(1, task.key());
            result = s.executeQuery();
            con.commit();
            if (result.next()) {
                String url = result.getString("worker");
                InputStream i = null;
                try {
                    Object b = new URL(url).getContent();
                    if (b instanceof InputStream) {
                        i = (InputStream) b;
                        byte[] bb = new byte[256];
                        int ll = i.read(bb);
                        String k = new String(bb, 0, ll);
                        if (k.equals(task.key())) {
                            return task;
                        }
                    }
                } catch (Exception e) {
                } finally {
                    if (i != null) {
                        i.close();
                    }
                }
                PreparedStatement s2 = null;
                s2 = con.prepareStatementFromCache(sqlImpl().sqlStatements().resetWorkerStatement());
                s2.setString(1, task.key());
                s2.executeUpdate();
                task = sqlImpl().queryFunctions().readPTask(task.key(), con);
                sqlImpl().loggingFunctions().log(task.key(), LogEntry.STOPPED, con);
                con.commit();
            }
        } finally {
            con.rollback();
            close(result);
        }
        return task;
    }
