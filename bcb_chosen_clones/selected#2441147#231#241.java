    private void setNodekeyInJsonResponse(String service) throws Exception {
        String filename = this.baseDirectory + service + ".json";
        Scanner s = new Scanner(new File(filename));
        PrintWriter fw = new PrintWriter(new File(filename + ".new"));
        while (s.hasNextLine()) {
            fw.println(s.nextLine().replaceAll("NODEKEY", this.key));
        }
        s.close();
        fw.close();
        (new File(filename + ".new")).renameTo(new File(filename));
    }
