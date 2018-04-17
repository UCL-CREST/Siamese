    public static NodeId generateTopicId(String topicName) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("No SHA support!");
        }
        md.update(topicName.getBytes());
        byte[] digest = md.digest();
        NodeId newId = new NodeId(digest);
        return newId;
    }
