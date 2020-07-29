    private void OK_actionPerformed(ActionEvent e) {
        System.out.println("\nOK_actionPerformed(ActionEvent e) called.");
        String[] runstring = new String[2];
        try {
            FileWriter sh_ping = new FileWriter("ping.txt", false);
            BufferedWriter out_ping = new BufferedWriter(sh_ping);
            String primary_serv = jTextField2.getText();
            String secondary_serv = jTextField4.getText();
            primary_serv = primary_serv.replace(".", ",");
            secondary_serv = secondary_serv.replace(".", ",");
            out_ping.write(primary_serv + "\n");
            out_ping.write(secondary_serv + "\n");
            out_ping.close();
            System.out.println("ping.txt COMPLETE");
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec("mkdir /etc/bind/zones");
            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line = null;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            int exitVal = pr.waitFor();
            System.out.println("Exited with error code " + exitVal);
            FileWriter sh = new FileWriter("/etc/bind/named.conf.local", true);
            BufferedWriter out = new BufferedWriter(sh);
            String zone = "zone " + "\"" + jTextField3.getText() + "\" {";
            String file1 = "file " + "\"/etc/bind/zones/" + jTextField3.getText() + ".db\";";
            out.write(zone + "\n");
            out.write("type master;" + "\n");
            out.write(file1 + "\n");
            out.write("};" + "\n");
            out.close();
            System.out.println("named.conf.local COMPLETE");
            FileWriter sh1 = new FileWriter("/etc/resolv.conf", true);
            BufferedWriter out1 = new BufferedWriter(sh1);
            String nameserver = "nameserver " + jTextField1.getText();
            out1.write(nameserver + "\n");
            out1.close();
            System.out.println("resolv.conf COMPLETE");
            String filename = "/etc/bind/zones/" + jTextField3.getText() + ".db";
            FileWriter sh2 = new FileWriter(filename, false);
            FileWriter sh3 = new FileWriter("/etc/bind/zones/hello.txt", false);
            BufferedWriter out2 = new BufferedWriter(sh2);
            BufferedWriter out3 = new BufferedWriter(sh3);
            String origin = "$ORIGIN " + jTextField3.getText() + ".";
            String first = "@	IN	SOA ns." + jTextField3.getText() + ".   root." + jTextField3.getText() + ". (";
            String second = "@	IN	NS  ns." + jTextField3.getText() + ".";
            String third = "@	IN	A   " + jTextField1.getText();
            String fourth = "www     IN      A     " + jTextField2.getText();
            out2.write(";" + "\n");
            out2.write("; BIND data file for local loopback interface" + "\n");
            out2.write(";" + "\n");
            out2.write("$TTL	604800" + "\n");
            out2.write(origin + "\n");
            out2.write(first + "\n");
            out2.write("2		; Serial" + "\n");
            out2.write("604800		; Refresh" + "\n");
            out2.write("86400		; Retry" + "\n");
            out2.write("2419200		; Expire" + "\n");
            out2.write("604800 )	; Negative Cache TTL" + "\n");
            out2.write(";" + "\n");
            out2.write(second + "\n");
            out2.write(third + "\n");
            out2.write(fourth + "\n");
            out3.write(";" + "\n");
            out3.write("; BIND data file for local loopback interface" + "\n");
            out3.write(";" + "\n");
            out3.write("$TTL	604800" + "\n");
            out3.write(origin + "\n");
            out3.write(first + "\n");
            out3.write("2		; Serial" + "\n");
            out3.write("604800		; Refresh" + "\n");
            out3.write("86400		; Retry" + "\n");
            out3.write("2419200		; Expire" + "\n");
            out3.write("604800 )	; Negative Cache TTL" + "\n");
            out3.write(";" + "\n");
            out3.write(second + "\n");
            out3.write(third + "\n");
            out2.close();
            out3.close();
            System.out.println("hello.txt & db file COMPLETE");
            Runtime rt1 = Runtime.getRuntime();
            Process pr1 = rt1.exec("/etc/init.d/bind9 restart");
            input = new BufferedReader(new InputStreamReader(pr1.getInputStream()));
            line = null;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            exitVal = pr1.waitFor();
            System.out.println("Exited with error code " + exitVal);
            ViewPrimary next_step = new ViewPrimary();
            next_step.show();
        } catch (Exception ex) {
            System.out.println(ex.toString());
            ex.printStackTrace();
        }
    }
