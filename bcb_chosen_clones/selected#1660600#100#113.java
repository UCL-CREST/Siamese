    private void findByMail(List<String> emailList) {
        log.info("Find by mail start ...");
        Pattern pattern = Pattern.compile(this.regex, PATTERN_FLAGS);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String mail = text.substring(start, end + 1);
            if (log.isDebugEnabled()) {
                log.debug("Find [" + mail + "]");
            }
            emailList.add(mail);
        }
    }
