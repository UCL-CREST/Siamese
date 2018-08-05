    public Collection getGroupsAndUserByName(String groupName, Integer state) throws DAOException {
        ArrayList groups = new ArrayList();
        try {
            StringBuffer sql = new StringBuffer();
            StringBuffer sqlZusatz = new StringBuffer();
            this.acquire();
            sql.append("SELECT Id ");
            sql.append(",Group_Name ");
            sql.append("FROM ").append(DatabaseTableConstants.GROUP_TABLE);
            sql.append(" WHERE");
            if (state != null) {
                sqlZusatz.append(" AND Object_State = ").append(state);
            } else {
                sqlZusatz.append(" AND (Object_State = '0' OR Object_State = '1')");
            }
            if (groupName != null) {
                if (!(groupName.equals("*") || groupName.equals(""))) {
                    groupName = groupName.replace('*', '%');
                    Pattern p = Pattern.compile("&");
                    Matcher m = p.matcher(groupName);
                    if (m.find()) {
                        String groupAnf = groupName.substring(0, m.start()).trim();
                        String gross = groupAnf.substring(0, 1).toUpperCase() + groupAnf.substring(1, groupAnf.length());
                        String klein = groupAnf.substring(0, 1).toLowerCase() + groupAnf.substring(1, groupAnf.length());
                        if (groupAnf.length() >= 1) {
                            sql.append(" Group_Name like '").append(gross).append("%'").append(sqlZusatz);
                            sql.append(" OR Group_Name like '").append(klein).append("%'").append(sqlZusatz);
                        }
                        groupName = groupName.substring(m.end(), groupName.length()).trim();
                        m = p.matcher(groupName);
                        while (m.find()) {
                            groupAnf = groupName.substring(0, m.start()).trim();
                            gross = groupAnf.substring(0, 1).toUpperCase() + groupAnf.substring(1, groupAnf.length());
                            klein = groupAnf.substring(0, 1).toLowerCase() + groupAnf.substring(1, groupAnf.length());
                            if (groupAnf.length() >= 1) {
                                sql.append(" OR Group_Name like '").append(gross).append("%'").append(sqlZusatz);
                                sql.append(" OR Group_Name like '").append(klein).append("%'").append(sqlZusatz);
                            }
                            groupName = groupName.substring(m.end(), groupName.length()).trim();
                            m = p.matcher(groupName);
                        }
                        gross = groupName.substring(0, 1).toUpperCase() + groupName.substring(1, groupName.length());
                        klein = groupName.substring(0, 1).toLowerCase() + groupName.substring(1, groupName.length());
                        if (groupName.length() >= 1) {
                            sql.append(" OR Group_Name like '").append(gross).append("%'").append(sqlZusatz);
                            sql.append(" OR Group_Name like '").append(klein).append("%'").append(sqlZusatz);
                        }
                    } else {
                        String gross = groupName.substring(0, 1).toUpperCase() + groupName.substring(1, groupName.length());
                        String klein = groupName.substring(0, 1).toLowerCase() + groupName.substring(1, groupName.length());
                        if (groupName.length() >= 1) {
                            sql.append(" Group_Name like '").append(gross).append("%'").append(sqlZusatz);
                            sql.append(" OR Group_Name like '").append(klein).append("%'").append(sqlZusatz);
                        }
                    }
                } else {
                    sql.append(" Group_Name like '%'").append(sqlZusatz);
                }
            } else {
                sql.append(" Group_Name like '%'").append(sqlZusatz);
            }
            sql.append(" order by Group_Name");
            Statement statement = getConnection().createStatement();
            String s = sql.toString();
            ResultSet result = statement.executeQuery(s);
            while (result.next()) {
                GroupVo group = new GroupVo();
                group.setId(new Integer(result.getString(1)));
                group.setGroupName(result.getString(2));
                groups.add(group);
            }
            statement.close();
            result.close();
            StringBuffer userSearch = new StringBuffer();
            userSearch.append("SELECT u.Id ");
            userSearch.append(",u.Login ");
            userSearch.append(",u.Object_State ");
            userSearch.append(",p.id ");
            userSearch.append(",p.Name ");
            userSearch.append(",p.First_Name ");
            userSearch.append("FROM ").append(DatabaseTableConstants.USER_TABLE).append(" u, ");
            userSearch.append(DatabaseTableConstants.PERSON_TABLE).append(" p ");
            userSearch.append(",").append(DatabaseTableConstants.USER_GROUP_TABLE).append(" up ");
            userSearch.append("WHERE u.Person_Id = p.id");
            userSearch.append(" AND u.Id = up.User_Id AND up.Group_Id = ?");
            userSearch.append(" order by p.Name");
            String us = userSearch.toString();
            PreparedStatement prestmt = getConnection().prepareStatement(userSearch.toString());
            for (Iterator it = groups.iterator(); it.hasNext(); ) {
                GroupVo group = (GroupVo) it.next();
                ArrayList users = new ArrayList();
                prestmt.setInt(1, group.getId().intValue());
                ResultSet res = prestmt.executeQuery();
                while (res.next()) {
                    UserVo user = new UserVo();
                    PersonVo person = new PersonVo();
                    user.setId(new Integer(res.getInt(1)));
                    user.setLogin(res.getString(2));
                    user.setObjectState(new Integer(res.getInt(3)));
                    person.setId(new Integer(res.getInt(4)));
                    person.setName(res.getString(5));
                    person.setFirstName(res.getString(6));
                    user.setPerson(person);
                    users.add(user);
                    group.setUser(users);
                }
                res.close();
            }
            prestmt.close();
        } catch (Exception e) {
            throw new DAOException();
        } finally {
            try {
                this.release();
            } catch (Exception e) {
                System.out.println("Exception releasing connection !" + e.toString());
            }
        }
        return groups;
    }
