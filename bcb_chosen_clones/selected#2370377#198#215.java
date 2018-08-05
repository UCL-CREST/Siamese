        private int findCodeOffsetPair(int glyphId) {
            List<CodeOffsetPairBuilder> pairList = this.getOffsetArray();
            int location = 0;
            int bottom = 0;
            int top = pairList.size();
            while (top != bottom) {
                location = (top + bottom) / 2;
                CodeOffsetPairBuilder pair = pairList.get(location);
                if (glyphId < pair.glyphCode()) {
                    top = location;
                } else if (glyphId > pair.glyphCode()) {
                    bottom = location + 1;
                } else {
                    return location;
                }
            }
            return -1;
        }
