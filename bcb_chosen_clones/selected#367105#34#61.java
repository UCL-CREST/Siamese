    public static Chunker regexp(final String type, final String regExp, final int group) {
        class RegexpChunker implements Chunker {

            Pattern pat = Pattern.compile(regExp);

            public Collection<Chunk> chunk(TextWithChunks chunkText) {
                Collection<Chunk> result = newSet();
                Matcher matcher = pat.matcher(chunkText.text);
                while (matcher.find()) {
                    result.add(new Chunk(chunkText, type, matcher.start(group), matcher.end(group)));
                }
                return result;
            }

            @Override
            public boolean equals(Object o) {
                if (o == null || getClass() != o.getClass()) return false;
                RegexpChunker that = (RegexpChunker) o;
                return Objects.equal(pat.pattern(), that.pat.pattern());
            }

            @Override
            public int hashCode() {
                return Objects.hashCode(pat.pattern());
            }
        }
        return new RegexpChunker();
    }
