    public final char mapChar(char c) {
        if (c < 256) {
            return latin1Map[c];
        } else {
            int bot = 0, top = characters.length - 1;
            while (top >= bot) {
                int mid = (bot + top) / 2;
                char mc = characters[mid];
                if (c == mc) return codepoints[mid]; else if (c < mc) top = mid - 1; else bot = mid + 1;
            }
            return 0;
        }
    }
