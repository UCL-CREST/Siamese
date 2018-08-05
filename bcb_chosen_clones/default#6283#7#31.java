    public static void main(String[] args) {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console.");
            System.exit(1);
        }
        while (true) {
            String regex = console.readLine("%nEnter your regex: ");
            Pattern pattern = Pattern.compile(regex);
            String input = console.readLine("Enter input string to search: ");
            Matcher matcher = pattern.matcher(input);
            boolean found = false;
            while (matcher.find()) {
                console.format("I found the text \"%s\" starting at " + "index %d and ending at index %d.%n", matcher.group(), matcher.start(), matcher.end());
                found = true;
            }
            if (!found) {
                console.format("No match found.%n");
            }
            String[] arr = input.split(regex);
            for (int i = 0; i < arr.length; i++) {
                console.format("a[%d]='%s' \n", i, arr[i]);
            }
        }
    }
