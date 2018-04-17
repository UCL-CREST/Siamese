    public void testRegister() throws IOException {
        User newUser = new User(false, "testregUser", "regUser");
        newUser.setEmail("eagle-r@gmx.de");
        newUser.setUniversity("uni");
        newUser.setFirstName("first");
        newUser.setLastName("last");
        User regUser = null;
        try {
            regUser = (User) getJdbcTemplate().queryForObject("select id, login, password, email, hash, REGISTRATION_DATE, university, FORUM_ID from USER where login = ?", new Object[] { newUser.getUsername() }, new UserMapper());
        } catch (EmptyResultDataAccessException e) {
        }
        assertNull("This test user already exists! Abort test", regUser);
        userServiceRemote.registrate(newUser);
        setComplete();
        endTransaction();
        regUser = (User) getJdbcTemplate().queryForObject("select id, login, password, email, hash, REGISTRATION_DATE, university, FORUM_ID from USER where login = ?", new Object[] { newUser.getUsername() }, new UserMapper());
        assertNotNull(regUser);
        assertNotNull(regUser.getId());
        assertNotNull(regUser.getHash());
        assertFalse(regUser.getHash().isEmpty());
        assertEquals(regUser.getLogin(), newUser.getLogin());
        assertEquals(regUser.getPassword(), newUser.getPassword());
        assertEquals(regUser.getUniversity(), newUser.getUniversity());
        assertEquals(regUser.getEmail(), newUser.getEmail());
        Integer id = newUser.getId();
        getJdbcTemplate().execute("DELETE FROM USER_AUTHORITIES WHERE USER_ID =" + id);
        getJdbcTemplate().execute("DELETE FROM USER WHERE ID = " + id);
        StringBuilder urlString = new StringBuilder(userService.getForumUrl());
        urlString.append("phpBB.php?action=remove").append("&id=").append(newUser.getForumID()).append("&mode=remove");
        logger.debug("Connecting to URL: " + urlString.toString());
        URL url = new URL(urlString.toString());
        URLConnection con = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) logger.debug("Response: " + inputLine);
        in.close();
    }
