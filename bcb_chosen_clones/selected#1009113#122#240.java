    public Collection getAllGroupsByNameAndState(String groupName, Integer state) throws DAOException {
        ArrayList groups = new ArrayList();
        try {
            StringBuffer sql = new StringBuffer();
            StringBuffer sqlZusatz = new StringBuffer();
            this.acquire();
            sql.append("SELECT id ");
            sql.append(",Comments ");
            sql.append(",Group_Name ");
            sql.append(",Group_Type ");
            sql.append(",Description ");
            sql.append(",Org_Unit ");
            sql.append(",Org_Type ");
            sql.append(",Email ");
            sql.append(",Url ");
            sql.append(",Object_State ");
            sql.append(",Time_Modified ");
            sql.append(",Time_Created ");
            sql.append(",Time_Frame_Begin ");
            sql.append(",Time_Frame_End ");
            sql.append(",Time_Frame_Admin_Period ");
            sql.append(",Org_Name ");
            sql.append("FROM ").append(DatabaseTableConstants.GROUP_TABLE);
            sql.append(" WHERE");
            if (state != null) {
                sqlZusatz.append(" AND Object_State = ").append(state);
            } else {
                sqlZusatz.append(" AND (Object_State = '0' OR Object_State = '1')");
            }
            if (groupName != null) {
                if (!(groupName.equals("*") || groupName.equals("") || groupName.startsWith("&") || groupName.endsWith("&") || groupName.startsWith(" ") || groupName.endsWith(" "))) {
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
                        } else {
                            sql.append(" Group_Name like '%'").append(sqlZusatz);
                        }
                        groupName = groupName.substring(m.end(), groupName.length()).trim();
                        m = p.matcher(groupName);
                        while (m.find()) {
                            groupAnf = groupName.substring(0, m.start()).trim();
                            if (groupAnf.length() >= 1) {
                                gross = groupAnf.substring(0, 1).toUpperCase() + groupAnf.substring(1, groupAnf.length());
                                klein = groupAnf.substring(0, 1).toLowerCase() + groupAnf.substring(1, groupAnf.length());
                                sql.append(" OR Group_Name like '").append(gross).append("%'").append(sqlZusatz);
                                sql.append(" OR Group_Name like '").append(klein).append("%'").append(sqlZusatz);
                            } else {
                                sql.append(" OR Group_Name like '%'").append(sqlZusatz);
                            }
                            groupName = groupName.substring(m.end(), groupName.length()).trim();
                            m = p.matcher(groupName);
                        }
                        if (groupName.length() >= 1) {
                            gross = groupName.substring(0, 1).toUpperCase() + groupName.substring(1, groupName.length());
                            klein = groupName.substring(0, 1).toLowerCase() + groupName.substring(1, groupName.length());
                            sql.append(" OR Group_Name like '").append(gross).append("%'").append(sqlZusatz);
                            sql.append(" OR Group_Name like '").append(klein).append("%'").append(sqlZusatz);
                        } else {
                            sql.append(" OR Group_Name like '%'").append(sqlZusatz);
                        }
                    } else {
                        String gross = groupName.substring(0, 1).toUpperCase() + groupName.substring(1, groupName.length());
                        String klein = groupName.substring(0, 1).toLowerCase() + groupName.substring(1, groupName.length());
                        if (groupName.length() >= 1) {
                            sql.append(" Group_Name like '").append(gross).append("%'").append(sqlZusatz);
                            sql.append(" OR Group_Name like '").append(klein).append("%'").append(sqlZusatz);
                        } else {
                            sql.append(" OR Group_Name like '%'").append(sqlZusatz);
                        }
                    }
                } else {
                    sql.append(" Group_Name like '%'").append(sqlZusatz);
                }
            } else {
                sql.append(" (Group_Name like '%'").append(sqlZusatz);
            }
            sql.append(" order by Group_Name");
            Statement statement = getConnection().createStatement();
            ResultSet result = statement.executeQuery(sql.toString());
            while (result.next()) {
                GroupVo group = new GroupVo();
                group.setId(new Integer(result.getString(1)));
                group.setComments(result.getString(2));
                group.setGroupName(result.getString(3));
                group.setGroupType(result.getString(4));
                group.setDescription(result.getString(5));
                group.setOrgUnit(result.getString(6));
                group.setOrgType(result.getString(7));
                group.setEmail(result.getString(8));
                group.setUrl(result.getString(9));
                group.setObjectState(new Integer(result.getString(10)));
                group.setTimeModified((Date) result.getTimestamp(11));
                group.setTimeCreated((Date) result.getTimestamp(12));
                group.setTimeFrameBegin((Date) result.getTimestamp(13));
                group.setTimeFrameEnd((Date) result.getTimestamp(14));
                group.setTimeFrameAdminPeriod(result.getString(15));
                group.setOrgName(result.getString(16));
                groups.add(group);
            }
            statement.close();
            result.close();
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
