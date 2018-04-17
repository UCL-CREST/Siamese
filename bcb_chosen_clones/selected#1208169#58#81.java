    public Song(String s) {
        StringTokenizer tokenizer = new StringTokenizer(s, ";");
        free = false;
        title = tokenizer.nextToken();
        artist = tokenizer.nextToken();
        location = tokenizer.nextToken();
        rating = Integer.parseInt(tokenizer.nextToken());
        overplay = Integer.parseInt(tokenizer.nextToken());
        String temp = tokenizer.nextToken();
        tokenizer = new StringTokenizer(temp, ",[] ");
        tags = new ArrayList<String>();
        while (tokenizer.hasMoreTokens()) {
            tags.add(tokenizer.nextToken());
        }
        byte[] bytes = new byte[40];
        try {
            MessageDigest hasher = MessageDigest.getInstance("SHA-1");
            hasher.update(title.getBytes());
            hasher.update(artist.getBytes());
            bytes = hasher.digest();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
