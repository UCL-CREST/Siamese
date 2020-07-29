    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: HashCalculator <Algorithm> <Input>");
            System.out.println("The preferred algorithm is SHA.");
        } else {
            MessageDigest md;
            try {
                md = MessageDigest.getInstance(args[0]);
                md.update(args[1].getBytes());
                System.out.print("Hashed value of " + args[1] + " is: ");
                System.out.println((new BASE64Encoder()).encode(md.digest()));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }
