    public boolean register(Object o) {
        String passwordAsText;
        if (o == null) throw new IllegalArgumentException("object cannot be null");
        if (!(o instanceof User)) {
            throw new IllegalArgumentException("passed argument is not an instance of the User class");
        }
        User newUser = (User) o;
        passwordAsText = newUser.getPassword();
        newUser.setPassword(passwordEncoder.encodePassword(passwordAsText, null));
        newUser.setRegDate(new Date());
        logger.debug("Setting default Authority {} to new user!", Authority.DEFAULT_NAME);
        newUser.getAuthorities().add(super.find(Authority.class, 1));
        logger.debug("Creating hash from email address! using Base64");
        newUser.setHash(new String(Base64.encodeBase64(newUser.getEmail().getBytes())));
        logger.debug("Creating phpBB forum User, by calling URL: {}", forumUrl);
        try {
            StringBuilder urlString = new StringBuilder(forumUrl);
            urlString.append("phpBB.php?action=register").append("&login=").append(newUser.getLogin()).append("&password=").append(passwordAsText).append("&email=").append(newUser.getEmail());
            sqlInjectionPreventer(urlString.toString());
            logger.debug("Connecting to URL: {}", urlString.toString());
            URL url = new URL(urlString.toString());
            URLConnection urlCon = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) newUser.setForumID(Integer.valueOf(inputLine));
            in.close();
        } catch (IOException io) {
            logger.error("Connecting failed! Msg: {}", io.getMessage());
            throw new RuntimeException("Couldn't conntect to phpBB");
        } catch (NumberFormatException e) {
            logger.error("phpBB user generation failed! Msg: {}", e.getMessage());
            throw new RuntimeException("phpBB user generation failed!");
        }
        entityManager.persist(newUser);
        try {
            sendConfirmationEmail(newUser);
            return true;
        } catch (MailException ex) {
            return false;
        }
    }
