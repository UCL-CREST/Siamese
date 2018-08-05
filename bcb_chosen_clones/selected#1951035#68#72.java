    protected long hash(String key) {
        IntegerHash ih = IntegerHash.getInstance(this.hashAlgo);
        ih.update(key.getBytes());
        return ih.digest();
    }
