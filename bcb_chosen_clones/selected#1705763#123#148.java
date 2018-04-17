    private long config(final String options) throws SQLException {
        MessageDigest msgDigest = null;
        try {
            msgDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        msgDigest.update(options.getBytes());
        final String md5sum = Concrete.md5(msgDigest.digest());
        Statement stmt = connection.createStatement();
        ResultSet rst = stmt.executeQuery("SELECT configId FROM configs WHERE md5='" + md5sum + "'");
        final long configId;
        if (rst.next()) {
            configId = rst.getInt(1);
        } else {
            stmt.executeUpdate("INSERT INTO configs(config, md5) VALUES ('" + options + "', '" + md5sum + "')");
            ResultSet aiRst = stmt.getGeneratedKeys();
            if (aiRst.next()) {
                configId = aiRst.getInt(1);
            } else {
                throw new SQLException("Could not retrieve generated id");
            }
        }
        stmt.executeUpdate("UPDATE executions SET configId=" + configId + " WHERE executionId=" + executionId);
        return configId;
    }
