    public boolean shufflePatterns() {
        this.checker.init("shufflePatterns");
        this.checker.addCheck(this.patterns != null, "The pattern list is a null pointer.");
        this.checker.addCheck(this.patterns.size() > 0, "The pattern list is empty.");
        if (this.checker.isSecure()) {
            Random r = new Random();
            int j = 0;
            for (int i = 0; i < this.patterns.size(); i++) {
                j = r.nextInt(i + 1);
                Pattern temp = this.patterns.get(i);
                this.patterns.set(i, this.patterns.get(j));
                this.patterns.set(j, temp);
            }
            return true;
        }
        return false;
    }
