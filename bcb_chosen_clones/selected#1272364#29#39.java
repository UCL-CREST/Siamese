    public void updateDb(int scriptNumber) throws SQLException, IOException {
        String pathName = updatesPackage.replace(".", "/");
        InputStream in = getClass().getClassLoader().getResourceAsStream(pathName + "/" + scriptNumber + ".sql");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(in, out);
        String script = out.toString("UTF-8");
        String[] statements = script.split(";");
        for (String statement : statements) {
            getJdbcTemplate().execute(statement);
        }
    }
