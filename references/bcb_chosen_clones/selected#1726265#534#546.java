    public NodeId generateTopicId(String topicName) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("No SHA support!");
        }
        if (m_ready) System.out.println("Scribe is ready at" + getNodeId() + " , topic is " + topicName);
        md.update(topicName.getBytes());
        byte[] digest = md.digest();
        NodeId newId = new NodeId(digest);
        return newId;
    }
