    public static void main(String[] args) throws IOException {
        String root = new File("./").getPath() + "\\Native";
        Process proc = Runtime.getRuntime().exec("java -Djava.library.path=" + root + " -jar run.jar");
        Scanner errBe = new Scanner(proc.getErrorStream());
        Scanner be = new Scanner(proc.getInputStream());
        while (errBe.hasNext() || be.hasNext()) {
            if (errBe.hasNext()) System.err.println(errBe.nextLine()); else System.out.println(be.nextLine());
        }
    }
