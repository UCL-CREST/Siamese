    private void findByMailTo(List<String> emailList) {
        log.info("Find by mailTo start ...");
        Pattern pattern = Pattern.compile(this.regex, PATTERN_FLAGS);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String mail = text.substring(start + 7, end + 1);
            if (log.isDebugEnabled()) {
                log.debug("Find [" + mail + "]");
            }
            emailList.add(mail);
        }
    }
