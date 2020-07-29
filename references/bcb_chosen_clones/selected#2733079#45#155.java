    public Collection getCoursesByKeywordAndState(String keywords, Integer state) throws DAOException {
        ArrayList courses = new ArrayList();
        boolean hasKeywords = false;
        try {
            StringBuffer sql = new StringBuffer();
            this.acquire();
            sql.append("SELECT id ");
            sql.append("FROM ").append(COURSE_TABLE);
            if (null != keywords && !keywords.equals("") && !keywords.equals("*") && !keywords.startsWith("&") && !keywords.endsWith("&") && !keywords.startsWith(" ") && !keywords.endsWith(" ")) {
                sql.append(" WHERE");
                hasKeywords = true;
                keywords = keywords.replace('*', '%');
                Pattern p = Pattern.compile("&");
                Matcher m = p.matcher(keywords);
                if (m.find()) {
                    String keyAnf = keywords.substring(0, m.start()).trim();
                    String gross = keyAnf.substring(0, 1).toUpperCase() + keyAnf.substring(1, keyAnf.length());
                    String klein = keyAnf.substring(0, 1).toLowerCase() + keyAnf.substring(1, keyAnf.length());
                    if (keyAnf.length() >= 1) {
                        sql.append(" (Keywords like '").append(gross).append("%'");
                        sql.append(" OR Title like '").append(gross).append("%'");
                        sql.append(" OR Description like '").append(gross).append("%'");
                        sql.append(" OR Keywords like '").append(klein).append("%'");
                        sql.append(" OR Title like '").append(klein).append("%'");
                        sql.append(" OR Description like '").append(klein).append("%'");
                    } else {
                        sql.append(" (Keywords like '%'");
                        sql.append(" OR Title like '%'");
                        sql.append(" OR Description like '%'");
                    }
                    keywords = keywords.substring(m.end(), keywords.length()).trim();
                    m = p.matcher(keywords);
                    while (m.find()) {
                        keyAnf = keywords.substring(0, m.start()).trim();
                        if (keyAnf.length() >= 1) {
                            gross = keyAnf.substring(0, 1).toUpperCase() + keyAnf.substring(1, keyAnf.length());
                            klein = keyAnf.substring(0, 1).toLowerCase() + keyAnf.substring(1, keyAnf.length());
                            sql.append(" OR Keywords like '").append(gross).append("%'");
                            sql.append(" OR Title like '").append(gross).append("%'");
                            sql.append(" OR Description like '").append(gross).append("%'");
                            sql.append(" OR Keywords like '").append(klein).append("%'");
                            sql.append(" OR Title like '").append(klein).append("%'");
                            sql.append(" OR Description like '").append(klein).append("%'");
                        } else {
                            sql.append(" OR Keywords like '%'");
                            sql.append(" OR Title like '%'");
                            sql.append(" OR Description like '%'");
                        }
                        keywords = keywords.substring(m.end(), keywords.length()).trim();
                        m = p.matcher(keywords);
                    }
                    if (keywords.length() >= 1) {
                        gross = keywords.substring(0, 1).toUpperCase() + keywords.substring(1, keywords.length());
                        klein = keywords.substring(0, 1).toLowerCase() + keywords.substring(1, keywords.length());
                        sql.append(" OR Keywords like '").append(gross).append("%'");
                        sql.append(" OR Title like '").append(gross).append("%'");
                        sql.append(" OR Description like '").append(gross).append("%'");
                        sql.append(" OR Keywords like '").append(klein).append("%'");
                        sql.append(" OR Title like '").append(klein).append("%'");
                        sql.append(" OR Description like '").append(klein).append("%')");
                    } else {
                        sql.append(" OR Keywords like '%'");
                        sql.append(" OR Title like '%'");
                        sql.append(" OR Description like '%'");
                    }
                } else {
                    String gross = keywords.substring(0, 1).toUpperCase() + keywords.substring(1, keywords.length());
                    String klein = keywords.substring(0, 1).toLowerCase() + keywords.substring(1, keywords.length());
                    if (keywords.length() >= 1) {
                        sql.append(" (Keywords like '").append(gross).append("%'");
                        sql.append(" OR Title like '").append(gross).append("%'");
                        sql.append(" OR Description like '").append(gross).append("%'");
                        sql.append(" OR Keywords like '").append(klein).append("%'");
                        sql.append(" OR Title like '").append(klein).append("%'");
                        sql.append(" OR Description like '").append(klein).append("%')");
                    } else {
                        sql.append(" (Keywords like '%'");
                        sql.append(" OR Title like '%'");
                        sql.append(" OR Description like '%'");
                    }
                }
            }
            if (state != null) {
                if (!state.equals(new Integer(3))) {
                    if (!hasKeywords) {
                        sql.append(" WHERE");
                    } else {
                        sql.append(" AND");
                    }
                    sql.append(" Object_State = ").append(state);
                }
            }
            Statement statement = getConnection().createStatement();
            ResultSet result = statement.executeQuery(sql.toString());
            while (result.next()) {
                Integer id = new Integer(result.getInt(1));
                courses.add(id);
            }
            statement.close();
            result.close();
        } catch (SQLException e) {
            throw new DAOException();
        } finally {
            try {
                this.release();
            } catch (Exception e) {
                System.out.println("Exception releasing connection !" + e.toString());
            }
        }
        return courses;
    }
