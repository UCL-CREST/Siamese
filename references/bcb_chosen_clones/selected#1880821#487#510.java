    public byte[] uniqueID(String name, String topic) {
        String key;
        byte[] id;
        synchronized (cache_) {
            key = name + "|" + topic;
            id = (byte[]) cache_.get(key);
            if (id == null) {
                MessageDigest md;
                try {
                    md = MessageDigest.getInstance("SHA");
                    md.update(name.getBytes());
                    md.update(topic.getBytes());
                    id = md.digest();
                    cache_.put(key, id);
                    if (debug_) {
                        System.out.println("Cached " + key + " [" + id[0] + "," + id[1] + ",...]");
                    }
                } catch (NoSuchAlgorithmException e) {
                    throw new Error("SHA not available!");
                }
            }
        }
        return id;
    }
