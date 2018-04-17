    public void calculate() throws FormatException, java.io.IOException {
        if (input == null) throw new IllegalStateException("FastaChecksummer input not set");
        contigHashes = new HashMap<String, ChecksumEntry>();
        String currentContig = null;
        java.security.MessageDigest hasher = null;
        try {
            hasher = java.security.MessageDigest.getInstance(checksumAlgorithm);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("Unexpected NoSuchAlgorithmException when asking for " + checksumAlgorithm + " algorithm");
        }
        String line = input.readLine();
        if (line == null) throw new FormatException("empty Fasta");
        try {
            while (line != null) {
                if (line.startsWith(">")) {
                    if (currentContig != null) {
                        String cs = new String(Hex.encodeHex(hasher.digest()));
                        contigHashes.put(currentContig, new ChecksumEntry(currentContig, cs));
                    }
                    Matcher m = ContigNamePattern.matcher(line);
                    if (m.matches()) {
                        currentContig = m.group(1);
                        hasher.reset();
                    } else throw new FormatException("Unexpected contig name format: " + line);
                } else {
                    if (currentContig == null) throw new FormatException("Sequence outside any fasta record (header is missing). Line: " + line); else hasher.update(line.getBytes("US-ASCII"));
                }
                line = input.readLine();
            }
            if (currentContig != null) {
                String cs = new String(Hex.encodeHex(hasher.digest()));
                contigHashes.put(currentContig, new ChecksumEntry(currentContig, cs));
            }
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("Unexpected UnsupportedEncodingException! Line: " + line);
        }
    }
