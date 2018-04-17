    public static void executa(String arquivo, String filial, String ip) {
        String drive = arquivo.substring(0, 2);
        if (drive.indexOf(":") == -1) drive = "";
        Properties p = Util.lerPropriedades(arquivo);
        String servidor = p.getProperty("servidor");
        String impressora = p.getProperty("fila");
        String arqRel = new String(drive + p.getProperty("arquivo"));
        String copias = p.getProperty("copias");
        if (filial.equalsIgnoreCase(servidor)) {
            Socket s = null;
            int tentativas = 0;
            boolean conectado = false;
            while (!conectado) {
                try {
                    tentativas++;
                    System.out.println("Tentando conectar " + ip + " (" + tentativas + ")");
                    s = new Socket(ip, 7000);
                    conectado = s.isConnected();
                } catch (ConnectException ce) {
                    System.err.println(ce.getMessage());
                    System.err.println(ce.getCause());
                } catch (UnknownHostException uhe) {
                    System.err.println(uhe.getMessage());
                } catch (IOException ioe) {
                    System.err.println(ioe.getMessage());
                }
            }
            FileInputStream in = null;
            BufferedOutputStream out = null;
            try {
                in = new FileInputStream(new File(arqRel));
                out = new BufferedOutputStream(new GZIPOutputStream(s.getOutputStream()));
            } catch (FileNotFoundException e3) {
                e3.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            String arqtr = arqRel.substring(2);
            System.out.println("Proximo arquivo: " + arqRel + " ->" + arqtr);
            while (arqtr.length() < 30) arqtr += " ";
            while (impressora.length() < 30) impressora += " ";
            byte aux[] = new byte[30];
            byte cop[] = new byte[2];
            try {
                aux = arqtr.getBytes("UTF8");
                out.write(aux);
                aux = impressora.getBytes("UTF8");
                out.write(aux);
                cop = copias.getBytes("UTF8");
                out.write(cop);
                out.flush();
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            byte b[] = new byte[1024];
            int nBytes;
            try {
                while ((nBytes = in.read(b)) != -1) out.write(b, 0, nBytes);
                out.flush();
                out.close();
                in.close();
                s.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.out.println("Arquivo " + arqRel + " foi transmitido. \n\n");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SimpleDateFormat dfArq = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dfLog = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String arqLog = "log" + filial + dfArq.format(new Date()) + ".txt";
            PrintWriter pw = null;
            try {
                pw = new PrintWriter(new FileWriter(arqLog, true));
            } catch (IOException e) {
                e.printStackTrace();
            }
            pw.println("Arquivo:  " + arquivo + "  " + dfLog.format(new Date()));
            pw.flush();
            pw.close();
            File f = new File(arquivo);
            while (!f.delete()) {
                System.out.println("Erro apagando " + arquivo);
            }
        }
    }
