    protected PTask commit_result(Result r, SyrupConnection con) throws Exception {
        try {
            int logAction = LogEntry.ENDED;
            String kk = r.context().task().key();
            if (r.in_1_consumed() && r.context().in_1_link() != null) {
                sqlImpl().updateFunctions().updateInLink(kk, false, null, con);
                logAction = logAction | LogEntry.IN_1;
            }
            if (r.in_2_consumed() && r.context().in_2_link() != null) {
                sqlImpl().updateFunctions().updateInLink(kk, true, null, con);
                logAction = logAction | LogEntry.IN_2;
            }
            if (r.out_1_result() != null && r.context().out_1_link() != null) {
                sqlImpl().updateFunctions().updateOutLink(kk, false, r.out_1_result(), con);
                logAction = logAction | LogEntry.OUT_1;
            }
            if (r.out_2_result() != null && r.context().out_2_link() != null) {
                sqlImpl().updateFunctions().updateOutLink(kk, true, r.out_2_result(), con);
                logAction = logAction | LogEntry.OUT_2;
            }
            sqlImpl().loggingFunctions().log(r.context().task().key(), logAction, con);
            boolean isParent = r.context().task().isParent();
            if (r instanceof Workflow) {
                Workflow w = (Workflow) r;
                Task[] tt = w.tasks();
                Link[] ll = w.links();
                Hashtable tkeyMap = new Hashtable();
                for (int i = 0; i < tt.length; i++) {
                    String key = sqlImpl().creationFunctions().newTask(tt[i], r.context().task(), con);
                    tkeyMap.put(tt[i], key);
                }
                for (int j = 0; j < ll.length; j++) {
                    sqlImpl().creationFunctions().newLink(ll[j], tkeyMap, con);
                }
                String in_link_1 = sqlImpl().queryFunctions().readInTask(kk, false, con);
                String in_link_2 = sqlImpl().queryFunctions().readInTask(kk, true, con);
                String out_link_1 = sqlImpl().queryFunctions().readOutTask(kk, false, con);
                String out_link_2 = sqlImpl().queryFunctions().readOutTask(kk, true, con);
                sqlImpl().updateFunctions().rewireInLink(kk, false, w.in_1_binding(), tkeyMap, con);
                sqlImpl().updateFunctions().rewireInLink(kk, true, w.in_2_binding(), tkeyMap, con);
                sqlImpl().updateFunctions().rewireOutLink(kk, false, w.out_1_binding(), tkeyMap, con);
                sqlImpl().updateFunctions().rewireOutLink(kk, true, w.out_2_binding(), tkeyMap, con);
                for (int k = 0; k < tt.length; k++) {
                    String kkey = (String) tkeyMap.get(tt[k]);
                    sqlImpl().updateFunctions().checkAndUpdateDone(kkey, con);
                }
                sqlImpl().updateFunctions().checkAndUpdateDone(in_link_1, con);
                sqlImpl().updateFunctions().checkAndUpdateDone(in_link_2, con);
                sqlImpl().updateFunctions().checkAndUpdateDone(out_link_1, con);
                sqlImpl().updateFunctions().checkAndUpdateDone(out_link_2, con);
                for (int k = 0; k < tt.length; k++) {
                    String kkey = (String) tkeyMap.get(tt[k]);
                    sqlImpl().updateFunctions().checkAndUpdateTargetExecutable(kkey, con);
                }
                sqlImpl().updateFunctions().checkAndUpdateTargetExecutable(in_link_1, con);
                sqlImpl().updateFunctions().checkAndUpdateTargetExecutable(in_link_2, con);
                sqlImpl().updateFunctions().checkAndUpdateTargetExecutable(out_link_1, con);
                sqlImpl().updateFunctions().checkAndUpdateTargetExecutable(out_link_2, con);
                isParent = true;
            }
            sqlImpl().updateFunctions().checkAndUpdateDone(kk, con);
            sqlImpl().updateFunctions().checkAndUpdateTargetExecutable(kk, con);
            PreparedStatement s3 = null;
            s3 = con.prepareStatementFromCache(sqlImpl().sqlStatements().updateTaskModificationStatement());
            java.util.Date dd = new java.util.Date();
            s3.setLong(1, dd.getTime());
            s3.setBoolean(2, isParent);
            s3.setString(3, r.context().task().key());
            s3.executeUpdate();
            sqlImpl().loggingFunctions().log(kk, LogEntry.ENDED, con);
            con.commit();
            return sqlImpl().queryFunctions().readPTask(kk, con);
        } finally {
            con.rollback();
        }
    }
