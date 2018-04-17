    public static void main(String[] args) {
        byte[] receiveData = new byte[1024];
        while (true) {
            System.out.println("KILLER STARTED");
            try {
                File file = new File("/home/arijit/Desktop/killer.txt");
                Writer output = new BufferedWriter(new FileWriter(file));
                output.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                Process p = Runtime.getRuntime().exec("sh /home/arijit/Desktop/my_script.sh");
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = null;
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (Exception e3) {
                System.out.println(e3);
            }
            String full_pid = "hello";
            try {
                String inputFileName = "/home/arijit/Desktop/killer.txt";
                FileReader inputFileReader = new FileReader(inputFileName);
                BufferedReader inputStream = new BufferedReader(inputFileReader);
                String str = null;
                while ((str = inputStream.readLine()) != null) {
                    full_pid = str;
                    System.out.println("Process ID is -------------- " + full_pid);
                }
            } catch (Exception ex1) {
            }
            full_pid = full_pid.trim();
            String[] extract = full_pid.split(" ");
            String kill_pid = extract[0];
            String cmd = "kill " + kill_pid;
            try {
                Process p = Runtime.getRuntime().exec(cmd);
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = null;
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (Exception e4) {
                System.out.println(e4);
            }
        }
    }
