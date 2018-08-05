    private String protectMarkup(String content, String markupRegex, String replaceSource, String replaceTarget) {
        Matcher matcher = Pattern.compile(markupRegex, Pattern.MULTILINE | Pattern.DOTALL).matcher(content);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String protectedMarkup = matcher.group();
            protectedMarkup = protectedMarkup.replaceAll(replaceSource, replaceTarget);
            try {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                digest.reset();
                digest.update(protectedMarkup.getBytes("UTF-8"));
                String hash = bytesToHash(digest.digest());
                matcher.appendReplacement(result, hash);
                c_protectionMap.put(hash, protectedMarkup);
                m_hashList.add(hash);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        matcher.appendTail(result);
        return result.toString();
    }
