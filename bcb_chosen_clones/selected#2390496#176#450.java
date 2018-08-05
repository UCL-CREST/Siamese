    public Collection findUsersByLoginNameStateGroup(String login, String name, Integer state, Integer groupId) throws DAOException {
        ArrayList users = new ArrayList();
        try {
            StringBuffer sql = new StringBuffer();
            boolean loginExists = false;
            this.acquire();
            sql.append("SELECT u.Id ");
            sql.append(",u.Login ");
            sql.append(",u.Password ");
            sql.append(",u.Object_State ");
            sql.append(",p.id ");
            sql.append(",p.Name ");
            sql.append(",p.First_Name ");
            sql.append(",p.Nick_Name ");
            sql.append(",p.Email ");
            sql.append(",p.Title ");
            sql.append(",p.Url ");
            sql.append(",p.Salutation ");
            sql.append(",p.Position ");
            sql.append(",p.Department ");
            sql.append(",p.Disability ");
            sql.append("FROM ").append(DatabaseTableConstants.USER_TABLE).append(" u, ");
            sql.append(DatabaseTableConstants.PERSON_TABLE).append(" p ");
            if (groupId != null) {
                sql.append(",").append(DatabaseTableConstants.USER_GROUP_TABLE).append(" up ");
            }
            sql.append("WHERE u.Person_Id = p.id");
            StringBuffer sqlZusatz = new StringBuffer();
            String[] loginArray = new String[login.length() + 1];
            int i = 0;
            if (state != null) {
                sqlZusatz.append(" AND u.Object_State = ").append(state);
            } else {
                sqlZusatz.append(" AND u.Object_State <>").append(Constants.DELETED);
            }
            if (groupId != null) {
                sqlZusatz.append(" AND u.id = up.User_Id AND up.Group_Id = ").append(groupId);
            }
            if (login != null) {
                if (!(login.equals("") || login.equals("*") || login.startsWith("&") || login.endsWith("&") || login.startsWith(" ") || login.endsWith(" "))) {
                    loginExists = true;
                    login = login.replace('*', '%');
                    Pattern p = Pattern.compile("&");
                    Matcher m = p.matcher(login);
                    if (m.find()) {
                        String loginAnf = login.substring(0, m.start()).trim();
                        String gross = loginAnf.substring(0, 1).toUpperCase() + loginAnf.substring(1, loginAnf.length());
                        String klein = loginAnf.substring(0, 1).toLowerCase() + loginAnf.substring(1, loginAnf.length());
                        if (loginAnf.length() >= 1) {
                            loginArray[i] = (" AND u.Login like '" + gross + "%'");
                            i++;
                            loginArray[i] = (" AND u.Login like '" + klein + "%'");
                            i++;
                        } else {
                            loginArray[i] = (" AND u.Login like '%'");
                            i++;
                        }
                        login = login.substring(m.end(), login.length()).trim();
                        m = p.matcher(login);
                        while (m.find()) {
                            loginAnf = login.substring(0, m.start()).trim();
                            if (loginAnf.length() >= 1) {
                                gross = loginAnf.substring(0, 1).toUpperCase() + loginAnf.substring(1, loginAnf.length());
                                klein = loginAnf.substring(0, 1).toLowerCase() + loginAnf.substring(1, loginAnf.length());
                                loginArray[i] = (" AND u.Login like '" + gross + "%'");
                                i++;
                                loginArray[i] = (" AND u.Login like '" + klein + "%'");
                                i++;
                            } else {
                                loginArray[i] = (" AND u.Login like '%'");
                                i++;
                            }
                            login = login.substring(m.end(), login.length()).trim();
                            m = p.matcher(login);
                        }
                        if (login.length() >= 1) {
                            gross = login.substring(0, 1).toUpperCase() + login.substring(1, login.length());
                            klein = login.substring(0, 1).toLowerCase() + login.substring(1, login.length());
                            loginArray[i] = (" AND u.Login like '" + gross + "%'");
                            i++;
                            loginArray[i] = (" AND u.Login like '" + klein + "%'");
                            i++;
                        } else {
                            loginArray[i] = (" AND u.Login like '%'");
                            i++;
                        }
                    } else {
                        String gross = login.substring(0, 1).toUpperCase() + login.substring(1, login.length());
                        String klein = login.substring(0, 1).toLowerCase() + login.substring(1, login.length());
                        loginArray[i] = (" AND u.Login like '" + gross + "%'");
                        i++;
                        loginArray[i] = (" AND u.Login like '" + klein + "%'");
                        i++;
                    }
                } else {
                    loginArray[i] = (" AND u.Login like '%'");
                    i++;
                }
            } else {
                loginArray[i] = (" AND u.Login like '%'");
                i++;
            }
            if (name != null) {
                if (!(name.equals("") || name.equals("*") || name.startsWith("&") || name.endsWith("&") || name.startsWith(" ") || name.endsWith(" "))) {
                    name = name.replace('*', '%');
                    Pattern p = Pattern.compile("&");
                    Matcher m = p.matcher(name);
                    if (m.find()) {
                        String nameAnf = name.substring(0, m.start()).trim();
                        String gross = nameAnf.substring(0, 1).toUpperCase() + nameAnf.substring(1, nameAnf.length());
                        String klein = nameAnf.substring(0, 1).toLowerCase() + nameAnf.substring(1, nameAnf.length());
                        if (nameAnf.length() >= 1) {
                            sql.append(" AND p.Name like '").append(gross).append("%'").append(sqlZusatz).append(loginArray[0]);
                            for (int j = 1; j < i; j++) {
                                sql.append(" OR u.Person_Id = p.id AND p.Name like '").append(gross).append("%'").append(sqlZusatz).append(loginArray[j]);
                            }
                            for (int j = 0; j < i; j++) {
                                sql.append(" OR u.Person_Id = p.id AND p.Name like '").append(klein).append("%'").append(sqlZusatz).append(loginArray[j]);
                            }
                        } else {
                            for (int j = 0; j < i; j++) {
                                sql.append(" OR u.Person_Id = p.id AND (p.Name like '%' OR p.Name is null)").append(sqlZusatz).append(loginArray[j]);
                            }
                        }
                        name = name.substring(m.end(), name.length()).trim();
                        m = p.matcher(name);
                        while (m.find()) {
                            nameAnf = name.substring(0, m.start()).trim();
                            if (nameAnf.length() >= 1) {
                                gross = nameAnf.substring(0, 1).toUpperCase() + nameAnf.substring(1, nameAnf.length());
                                klein = nameAnf.substring(0, 1).toLowerCase() + nameAnf.substring(1, nameAnf.length());
                                for (int j = 0; j < i; j++) {
                                    sql.append(" OR u.Person_Id = p.id AND p.Name like '").append(gross).append("%'").append(sqlZusatz).append(loginArray[j]);
                                }
                                for (int j = 0; j < i; j++) {
                                    sql.append(" OR u.Person_Id = p.id AND p.Name like '").append(klein).append("%'").append(sqlZusatz).append(loginArray[j]);
                                }
                            } else {
                                for (int j = 0; j < i; j++) {
                                    sql.append(" OR u.Person_Id = p.id AND (p.Name like '%' OR p.Name is null)").append(sqlZusatz).append(loginArray[j]);
                                }
                            }
                            name = name.substring(m.end(), name.length()).trim();
                            m = p.matcher(name);
                        }
                        if (name.length() >= 1) {
                            gross = name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
                            klein = name.substring(0, 1).toLowerCase() + name.substring(1, name.length());
                            for (int j = 0; j < i; j++) {
                                sql.append(" OR u.Person_Id = p.id AND p.Name like '").append(gross).append("%'").append(sqlZusatz).append(loginArray[j]);
                            }
                            for (int j = 0; j < i; j++) {
                                sql.append(" OR u.Person_Id = p.id AND p.Name like '").append(klein).append("%'").append(sqlZusatz).append(loginArray[j]);
                            }
                        } else {
                            for (int j = 0; j < i; j++) {
                                sql.append(" OR u.Person_Id = p.id AND (p.Name like '%' OR p.Name is null)").append(sqlZusatz).append(loginArray[j]);
                            }
                        }
                    } else {
                        String gross = name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
                        String klein = name.substring(0, 1).toLowerCase() + name.substring(1, name.length());
                        sql.append(" AND p.Name like '").append(gross).append("%'").append(sqlZusatz).append(loginArray[0]);
                        for (int j = 1; j < i; j++) {
                            sql.append(" OR u.Person_Id = p.id AND p.Name like '").append(gross).append("%'").append(sqlZusatz).append(loginArray[j]);
                        }
                        for (int j = 0; j < i; j++) {
                            sql.append(" OR u.Person_Id = p.id AND p.Name like '").append(klein).append("%'").append(sqlZusatz).append(loginArray[j]);
                        }
                    }
                } else {
                    sql.append(" AND (p.Name like '%' OR p.Name is null)").append(sqlZusatz).append(loginArray[0]);
                    for (int j = 1; j < i; j++) {
                        sql.append(" OR u.Person_Id = p.id AND (p.Name like '%' OR p.Name is null)").append(sqlZusatz).append(loginArray[j]);
                    }
                }
            } else {
                sql.append(" AND (p.Name like '%' OR p.Name is null)").append(sqlZusatz).append(loginArray[0]);
                for (int j = 1; j < i; j++) {
                    sql.append(" OR u.Person_Id = p.id AND (p.Name like '%' OR p.Name is null)").append(sqlZusatz).append(loginArray[j]);
                }
            }
            if (loginExists) {
                sql.append(" order by u.Login");
            } else {
                sql.append(" order by p.Name");
            }
            Statement statement = getConnection().createStatement();
            ResultSet result = statement.executeQuery(sql.toString());
            while (result.next()) {
                UserVo user = new UserVo();
                PersonVo person = new PersonVo();
                user.setId(new Integer(result.getInt(1)));
                user.setLogin(result.getString(2));
                user.setPassword(result.getString(3));
                user.setObjectState(new Integer(result.getInt(4)));
                person.setId(new Integer(result.getInt(5)));
                person.setName(result.getString(6));
                person.setFirstName(result.getString(7));
                person.setNickName(result.getString(8));
                person.setEmail(result.getString(9));
                person.setTitle(result.getString(10));
                person.setUrl(result.getString(11));
                person.setSalutation(result.getString(12));
                person.setPosition(result.getString(13));
                person.setDepartment(result.getString(14));
                person.setDisability(result.getString(15));
                user.setPerson(person);
                users.add(user);
            }
            statement.close();
            result.close();
            StringBuffer phoneSearch = new StringBuffer();
            phoneSearch.append("SELECT Typ ");
            phoneSearch.append(",Phone_Jack ");
            phoneSearch.append(",Direct_Dial ");
            phoneSearch.append(",City_Code ");
            phoneSearch.append(",Country_Code ");
            phoneSearch.append("FROM ").append(DatabaseTableConstants.PHONE_TABLE);
            phoneSearch.append(" WHERE Person_Id = ?");
            PreparedStatement phoneStmt = getConnection().prepareStatement(phoneSearch.toString());
            for (Iterator it = users.iterator(); it.hasNext(); ) {
                PersonVo person = ((UserVo) it.next()).getPerson();
                phoneStmt.setInt(1, person.getId().intValue());
                ResultSet res = phoneStmt.executeQuery();
                ArrayList phones = new ArrayList();
                while (res.next()) {
                    PhoneVo phone = new PhoneVo();
                    phone.setTyp(new Integer(res.getInt(1)));
                    phone.setPhoneJack(res.getString(2));
                    phone.setDirectDial(res.getString(3));
                    phone.setCityCode(res.getString(4));
                    phone.setCountryCode(res.getString(5));
                    phones.add(phone);
                }
                person.addPhones(phones);
                res.close();
            }
            phoneStmt.close();
            StringBuffer addressSearch = new StringBuffer();
            addressSearch.append("SELECT Typ ");
            addressSearch.append(",Locality ");
            addressSearch.append(",Street ");
            addressSearch.append(",Pcode ");
            addressSearch.append("FROM ").append(DatabaseTableConstants.ADDRESS_TABLE);
            addressSearch.append(" WHERE Person_Id = ?");
            PreparedStatement addressStmt = getConnection().prepareStatement(addressSearch.toString());
            for (Iterator it = users.iterator(); it.hasNext(); ) {
                PersonVo person = ((UserVo) it.next()).getPerson();
                addressStmt.setInt(1, person.getId().intValue());
                ResultSet res = addressStmt.executeQuery();
                ArrayList addresses = new ArrayList();
                while (res.next()) {
                    AddressVo address = new AddressVo();
                    address.setTyp(new Integer(res.getInt(1)));
                    address.setLocality(res.getString(2));
                    address.setStreet(res.getString(3));
                    address.setPcode(res.getString(4));
                    addresses.add(address);
                }
                person.addAddresses(addresses);
                res.close();
            }
            addressStmt.close();
        } catch (Exception e) {
            throw new DAOException();
        } finally {
            try {
                this.release();
            } catch (Exception e) {
                System.out.println("Exception releasing connection !" + e.toString());
            }
        }
        return users;
    }
