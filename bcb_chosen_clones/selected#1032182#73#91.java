    static void useFind() {
        Console c = System.console();
        if (c == null) {
            System.err.println("No console");
            System.exit(1);
        }
        while (true) {
            Pattern p = Pattern.compile(c.readLine("%n find(), Enter your regex:"));
            Matcher m = p.matcher(c.readLine("Enter input string to search:"));
            boolean found = false;
            while (m.find()) {
                c.printf("Found the text \"%s\" starting at %d and ending at %d.%n", m.group(), m.start(), m.end());
                found = true;
            }
            if (!found) {
                c.format("No matcher found.%n");
            }
        }
    }
