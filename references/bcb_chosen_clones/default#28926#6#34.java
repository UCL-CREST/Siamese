    public static void main(String[] args) {
        String s = File.separator;
        String classLocation = MyProxyClient.class.getName().replaceAll("\\.", s) + ".class";
        ClassLoader loader = MyProxyClient.class.getClassLoader();
        URL location = loader.getResource(classLocation);
        String gridsamHome = location.getFile().replace("MyProxyClient.class", "");
        StringBuffer a = new StringBuffer();
        for (int i = 0; i < args.length; i++) {
            a.append(args[i]);
            a.append(" ");
        }
        String command = "java -classpath " + getClassPath(gridsamHome + "lib") + "conf " + "-Djava.endorsed.dirs=endorsed " + "-Dorg.apache.ws.security.crypto.merlin.file=conf" + s + "gridsam-default.ks " + "-Dorg.apache.ws.security.crypto.merlin.crldir=conf" + s + "CRLs " + "-Dlog4j.appender.LOG.File=client.log " + "-Djava.util.logging.config.file=conf" + s + "logging.properties " + "-Dlog4j.configuration=file://" + gridsamHome + "conf" + s + "log4j.properties " + "-Daxis.ClientConfigFile=conf" + s + "client-config.wsdd " + "-Dgrid.config.dir=conf " + "org.globus.tools.MyProxy " + a.toString();
        String[] commandArgs = command.split(" +");
        ProcessBuilder pb = new ProcessBuilder(commandArgs);
        pb.directory(new File(gridsamHome));
        pb.redirectErrorStream(true);
        try {
            Process p = pb.start();
            InputStream is = p.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (java.io.IOException ioe) {
            System.out.println("Error processing command");
        }
    }
