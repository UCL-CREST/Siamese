    public static void main(String[] args) {
        while (true) {
            System.out.println("while entered");
            try {
                String inputFileName = "ping.txt";
                FileReader inputFileReader = new FileReader(inputFileName);
                BufferedReader inputStream = new BufferedReader(inputFileReader);
                ArrayList<String> iparray = new ArrayList<String>();
                String str = null;
                while ((str = inputStream.readLine()) != null) {
                    iparray.add(str);
                }
                System.out.println("INFO FROM FILE ADDED");
                inputFileReader.close();
                int i = 0;
                while (i < iparray.size()) {
                    System.out.println(iparray.get(i));
                    i++;
                }
                String active = iparray.get(0);
                String passive = iparray.get(1);
                System.out.println("PRIMARY IP " + active);
                System.out.println("SECONDARY IP " + passive);
                String rsync_addr = active.replace(",", ".") + "a" + passive.replace(",", ".");
                String rsync_addr_active = active.replace(",", ".");
                String finalpassive = active.replace(",", ".");
                System.out.println(finalpassive);
                String[] primary_addr = active.split(",");
                int a = Integer.parseInt(primary_addr[0]);
                int b = Integer.parseInt(primary_addr[1]);
                int c = Integer.parseInt(primary_addr[2]);
                int d = Integer.parseInt(primary_addr[3]);
                byte[] prim_addr = new byte[4];
                prim_addr[0] = (byte) a;
                prim_addr[1] = (byte) b;
                prim_addr[2] = (byte) c;
                prim_addr[3] = (byte) d;
                try {
                    pin = InetAddress.getByAddress(prim_addr);
                    boolean check1 = pin.isReachable(3000);
                    if (check1) {
                        System.out.println("Server UP " + active);
                        String inputFileName1 = "/etc/bind/zones/hello.txt";
                        FileReader inputFileReader1 = new FileReader(inputFileName1);
                        BufferedReader inputStream1 = new BufferedReader(inputFileReader1);
                        FileWriter sh = new FileWriter("/etc/bind/zones/linux.lan.db", false);
                        BufferedWriter out = new BufferedWriter(sh);
                        String str1 = null;
                        while ((str1 = inputStream1.readLine()) != null) {
                            out.write(str1 + "\n");
                        }
                        out.write("www     IN      A     " + finalpassive);
                        out.close();
                        String sentence = finalpassive;
                        String sentence1 = rsync_addr;
                        System.out.println("writing done...");
                        System.out.println("------------------------KILLING PRIMARY RSYNC----------------------------");
                        InetAddress IPAddress_prim = InetAddress.getByAddress(prim_addr);
                        DatagramSocket client_kill_Socket_prim = new DatagramSocket();
                        byte[] send_kill_data_prim = new byte[1024];
                        String proc_kill_prim = "kill";
                        send_kill_data_prim = proc_kill_prim.getBytes();
                        DatagramPacket send_kill_Packet_prim = new DatagramPacket(send_kill_data_prim, send_kill_data_prim.length, IPAddress_prim, 9000);
                        client_kill_Socket_prim.send(send_kill_Packet_prim);
                        for (int l = 1; l < iparray.size(); l++) {
                            System.out.println("Entered the RSYNC Module");
                            String newpassive = iparray.get(l);
                            String[] sec_addr = newpassive.split(",");
                            int e = Integer.parseInt(sec_addr[0]);
                            int f = Integer.parseInt(sec_addr[1]);
                            int g = Integer.parseInt(sec_addr[2]);
                            int h = Integer.parseInt(sec_addr[3]);
                            byte[] secbyte_addr = new byte[4];
                            secbyte_addr[0] = (byte) e;
                            secbyte_addr[1] = (byte) f;
                            secbyte_addr[2] = (byte) g;
                            secbyte_addr[3] = (byte) h;
                            DatagramSocket clientSocket = new DatagramSocket();
                            DatagramSocket client_kill_Socket = new DatagramSocket();
                            InetAddress IPAddress = InetAddress.getByAddress(secbyte_addr);
                            byte[] sendData = new byte[1024];
                            byte[] send_kill_data = new byte[1024];
                            sendData = sentence.getBytes();
                            String proc_kill = "kill";
                            send_kill_data = proc_kill.getBytes();
                            DatagramPacket send_kill_Packet = new DatagramPacket(send_kill_data, send_kill_data.length, IPAddress, 9000);
                            client_kill_Socket.send(send_kill_Packet);
                            pause(200);
                            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
                            clientSocket.send(sendPacket);
                        }
                        try {
                            Runtime rt = Runtime.getRuntime();
                            Process pr = rt.exec("/etc/init.d/bind9 restart");
                            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                            String line = null;
                            while ((line = input.readLine()) != null) {
                                System.out.println(line);
                            }
                            int exitVal = pr.waitFor();
                        } catch (Exception e) {
                            System.out.println(e.toString());
                            e.printStackTrace();
                        }
                        boolean che = true, check;
                        while (che) {
                            check = pin.isReachable(3000);
                            if (check) {
                                System.out.println("pinged");
                            } else {
                                che = false;
                            }
                        }
                    } else {
                        System.out.println("Server DOWN " + active);
                    }
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
                System.out.println("Round robin initiated");
                ArrayList<String> iparrayfinal = new ArrayList<String>();
                int k = 0, j = 1;
                while (j < iparray.size()) {
                    iparrayfinal.add(iparray.get(j));
                    j++;
                }
                iparrayfinal.add(iparray.get(0));
                i = 0;
                while (i < iparrayfinal.size()) {
                    System.out.println("finally --------- " + iparrayfinal.get(i));
                    i++;
                }
                i = 0;
                try {
                    File file = new File("ping.txt");
                    Writer output = new BufferedWriter(new FileWriter(file));
                    while (i < iparrayfinal.size()) {
                        output.write(iparrayfinal.get(i) + "\n");
                        i++;
                    }
                    output.close();
                    System.out.println("COMPLETE");
                } catch (Exception e) {
                    System.out.println("outer catch");
                    e.printStackTrace();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
