    private void upgradeSchema() {
        Statement stmt = null;
        boolean updatedSchema = false;
        try {
            int i = getSchema();
            if (i < SCHEMA_VERSION) {
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                updatedSchema = true;
            }
            while (i < SCHEMA_VERSION) {
                String qry;
                switch(i) {
                    case 0:
                        qry = "CREATE TABLE settings (var VARCHAR(32) NOT NULL, val LONG VARCHAR)";
                        stmt.executeUpdate(qry);
                        qry = "INSERT INTO settings (var, val) VALUES ('schema', '1')";
                        stmt.executeUpdate(qry);
                        qry = "ALTER TABLE recordings ADD COLUMN exe LONG VARCHAR NOT NULL DEFAULT '%UNKNOWN%'";
                        stmt.executeUpdate(qry);
                        qry = "CREATE TABLE files (id INT NOT NULL, file LONG VARCHAR NOT NULL, finished INT NOT NULL)";
                        stmt.executeUpdate(qry);
                        updateFilesTable();
                        break;
                    case 1:
                        qry = "ALTER TABLE files ADD COLUMN type VARCHAR(32) NOT NULL DEFAULT '%UNKNOWN%'";
                        stmt.executeUpdate(qry);
                        qry = "UPDATE settings SET val = '2' WHERE var = 'schema'";
                        stmt.executeUpdate(qry);
                        break;
                    case 2:
                        qry = "CREATE UNIQUE INDEX IF NOT EXISTS recordings_history ON recordings(id,type)";
                        stmt.executeUpdate(qry);
                        qry = "CREATE INDEX IF NOT EXISTS files_history ON files(id,type)";
                        stmt.executeUpdate(qry);
                        qry = "UPDATE settings SET val = '3' WHERE var = 'schema'";
                        stmt.executeUpdate(qry);
                        break;
                    case 3:
                        qry = "CREATE TABLE log (id INTEGER PRIMARY KEY, context VARCHAR(16) NOT NULL, level VARCHAR(16) NOT NULL, time LONG INT NOT NULL, msg LONG VARCHAR NOT NULL, parent INT)";
                        stmt.executeUpdate(qry);
                        qry = "UPDATE settings SET val = '4' WHERE var = 'schema'";
                        stmt.executeUpdate(qry);
                        break;
                    case 4:
                        qry = "CREATE UNIQUE INDEX IF NOT EXISTS log_id ON log(id)";
                        stmt.executeUpdate(qry);
                        qry = "CREATE INDEX IF NOT EXISTS log_parent ON log(parent)";
                        stmt.executeUpdate(qry);
                        qry = "UPDATE settings SET val = '5' WHERE var = 'schema'";
                        stmt.executeUpdate(qry);
                        break;
                    case 5:
                        qry = "CREATE TABLE tmp_settings (var varchar(32) NOT NULL PRIMARY KEY, val varchar(128) NOT NULL)";
                        stmt.executeUpdate(qry);
                        qry = "INSERT INTO tmp_settings SELECT var,val FROM settings";
                        stmt.executeUpdate(qry);
                        qry = "DROP TABLE settings";
                        stmt.executeUpdate(qry);
                        qry = "ALTER TABLE tmp_settings RENAME TO settings";
                        stmt.executeUpdate(qry);
                        qry = "UPDATE settings SET val = '6' WHERE var = 'schema'";
                        stmt.executeUpdate(qry);
                        break;
                    case 6:
                        qry = "DROP TABLE log";
                        stmt.executeUpdate(qry);
                        qry = "CREATE TABLE log (id INTEGER PRIMARY KEY, mediaid varchar(32) NOT NULL, taskid varchar(32) NOT NULL, msg LONG VARCHAR NOT NULL)";
                        stmt.executeUpdate(qry);
                        qry = "UPDATE settings SET val = '7' WHERE var = 'schema'";
                        stmt.executeUpdate(qry);
                        break;
                    case 7:
                        qry = "CREATE TABLE client (host varchar(255) PRIMARY KEY, conf LONG VARCHAR)";
                        stmt.executeUpdate(qry);
                        qry = "UPDATE settings SET val = '8' WHERE var = 'schema'";
                        stmt.executeUpdate(qry);
                        break;
                    case 8:
                        qry = "DROP INDEX files_history";
                        stmt.executeUpdate(qry);
                        qry = "DROP INDEX recordings_history";
                        stmt.executeUpdate(qry);
                        qry = "DROP TABLE files";
                        stmt.executeUpdate(qry);
                        qry = "CREATE TABLE new_rec (id INT NOT NULL, type VARCHAR(32) NOT NULL, start INT NOT NULL DEFAULT 0, finish INT NOT NULL DEFAULT 0, state INT NOT NULL DEFAULT 1, PRIMARY KEY(id, type))";
                        stmt.executeUpdate(qry);
                        qry = "INSERT INTO new_rec SELECT id, type, 0, 0, 1 FROM recordings";
                        stmt.executeUpdate(qry);
                        qry = "DROP TABLE recordings";
                        stmt.executeUpdate(qry);
                        qry = "CREATE TABLE recordings (id INT NOT NULL, type VARCHAR(32) NOT NULL, start INT NOT NULL DEFAULT 0, finish INT NOT NULL DEFAULT 0, state INT NOT NULL DEFAULT 1, PRIMARY KEY(id, type))";
                        stmt.executeUpdate(qry);
                        qry = "INSERT INTO recordings SELECT * FROM new_rec";
                        stmt.executeUpdate(qry);
                        qry = "DROP TABLE new_rec";
                        stmt.executeUpdate(qry);
                        qry = "CREATE UNIQUE INDEX recordings_history on recordings(id,type)";
                        stmt.executeUpdate(qry);
                        qry = "UPDATE settings SET val = '9' WHERE var = 'schema'";
                        stmt.executeUpdate(qry);
                        break;
                    case 9:
                        qry = "ALTER TABLE recordings ADD COLUMN host VARCHAR(255) NOT NULL DEFAULT ''";
                        stmt.executeUpdate(qry);
                        qry = "UPDATE settings SET val = '10' WHERE var = 'schema'";
                        stmt.executeUpdate(qry);
                        break;
                    case 10:
                        qry = "ALTER TABLE recordings ADD COLUMN airing VARCHAR(64) NOT NULL DEFAULT '0'";
                        stmt.executeUpdate(qry);
                        qry = "UPDATE settings SET val = '11' WHERE var = 'schema'";
                        stmt.executeUpdate(qry);
                        break;
                    case 11:
                        PreparedStatement pstmt = null;
                        try {
                            MessageDigest msg = MessageDigest.getInstance("MD5");
                            msg.update("sjqadmin".getBytes());
                            String pwd = new String(msg.digest());
                            pstmt = conn.prepareStatement("REPLACE INTO settings (var, val) VALUES ('password', ?)");
                            pstmt.setString(1, pwd);
                            pstmt.executeUpdate();
                        } catch (NoSuchAlgorithmException e) {
                            throw new SQLException(e);
                        } finally {
                            if (pstmt != null) pstmt.close();
                        }
                        stmt.executeUpdate("UPDATE settings SET val = '12' WHERE var = 'schema'");
                        break;
                    case 12:
                        qry = "CREATE INDEX logs_for_tasks ON log(mediaid, taskid)";
                        stmt.executeUpdate(qry);
                        qry = "UPDATE settings SET val = '13' WHERE var = 'schema'";
                        stmt.executeUpdate(qry);
                        break;
                    case 13:
                        qry = "DELETE FROM log";
                        stmt.executeUpdate(qry);
                        qry = "UPDATE settings SET val = '14' WHERE var = 'schema'";
                        stmt.executeUpdate(qry);
                        break;
                    case 14:
                        qry = "DROP TABLE log";
                        stmt.executeUpdate(qry);
                        qry = "CREATE TABLE log (id INTEGER PRIMARY KEY, mediaid varchar(32) NOT NULL, taskid varchar(32) NOT NULL, msg LONG VARCHAR NOT NULL, ts INTEGER NOT NULL DEFAULT 0)";
                        stmt.executeUpdate(qry);
                        qry = "CREATE INDEX logs_by_date ON log(ts)";
                        stmt.executeUpdate(qry);
                        qry = "CREATE INDEX IF NOT EXISTS logs_for_tasks ON log(mediaid, taskid)";
                        stmt.executeUpdate(qry);
                        qry = "UPDATE settings SET val = '15' WHERE var = 'schema'";
                        stmt.executeUpdate(qry);
                        break;
                    case 15:
                        qry = "DELETE FROM log WHERE mediaid = 0 AND taskid = '0'";
                        stmt.executeUpdate(qry);
                        qry = "UPDATE settings SET val = '16' WHERE var = 'schema'";
                        stmt.executeUpdate(qry);
                        break;
                    case 16:
                        qry = "CREATE TEMPORARY TABLE rec_tmp (objtype varchar(64) NOT NULL DEFAULT 'media', id INT NOT NULL, type VARCHAR(32) NOT NULL, start INT NOT NULL DEFAULT 0, finish INT NOT NULL DEFAULT 0, state INT NOT NULL DEFAULT 1, host VARCHAR(255) NOT NULL DEFAULT '', airing VARCHAR(64) NOT NULL DEFAULT '0', PRIMARY KEY (objtype, id, type))";
                        stmt.executeUpdate(qry);
                        qry = "INSERT INTO rec_tmp SELECT 'media', id, type, start, finish, state, host, airing FROM recordings";
                        stmt.executeUpdate(qry);
                        qry = "DROP TABLE recordings";
                        stmt.executeUpdate(qry);
                        qry = "CREATE TABLE recordings (objtype varchar(64) NOT NULL DEFAULT 'media', id INT NOT NULL, type VARCHAR(32) NOT NULL, start INT NOT NULL DEFAULT 0, finish INT NOT NULL DEFAULT 0, state INT NOT NULL DEFAULT 1, host VARCHAR(255) NOT NULL DEFAULT '', airing VARCHAR(64) NOT NULL DEFAULT '0', PRIMARY KEY (objtype, id, type))";
                        stmt.executeUpdate(qry);
                        qry = "INSERT INTO recordings SELECT * FROM rec_tmp";
                        stmt.executeUpdate(qry);
                        qry = "DROP TABLE rec_tmp";
                        stmt.executeUpdate(qry);
                        qry = "ALTER TABLE log ADD COLUMN objtype varchar(64) NOT NULL DEFAULT 'media'";
                        stmt.executeUpdate(qry);
                        qry = "UPDATE settings SET val = '17' WHERE var = 'schema'";
                        stmt.executeUpdate(qry);
                        break;
                    case 17:
                        qry = "DROP INDEX logs_for_tasks";
                        stmt.executeUpdate(qry);
                        qry = "CREATE INDEX logs_for_tasks ON log(mediaid, taskid, objtype)";
                        stmt.executeUpdate(qry);
                        qry = "UPDATE settings SET val = '18' WHERE var = 'schema'";
                        stmt.executeUpdate(qry);
                        break;
                }
                i++;
            }
            if (updatedSchema) conn.commit();
        } catch (SQLException e) {
            try {
                if (updatedSchema) conn.rollback();
            } catch (SQLException x) {
                LOG.fatal("SQL error", x);
            }
            LOG.fatal("SQL error", e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (updatedSchema) conn.setAutoCommit(true);
            } catch (SQLException e) {
                LOG.fatal("SQL error", e);
                throw new RuntimeException(e);
            }
        }
    }
