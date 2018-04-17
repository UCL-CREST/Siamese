    private void updateSystem() throws IOException {
          String stringUrl="http://code.google.com/p/senai-pe-cronos/downloads/list";
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        InputStream is = url.openStream();   
InputStreamReader isr = new InputStreamReader(is);   
BufferedReader br = new BufferedReader(isr);   
  
String linha = br.readLine();  
  
while (linha != null) { 
    linha = br.readLine();  
   
   if(linha.contains("/files/updateCronos-0-")){
   String[] s=linha.split("-");
   String[] v=s[4].split(".exe");
   versao=v[0];
   println("----"+versao);
  
  break;
}   

}
      
stringUrl="http://senai-pe-cronos.googlecode.com/files/updateCronos-0-"+versao+".exe";
UpdateCronos update=new UpdateCronos();
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }       
System.out.println("versão:"+versao);
         if(Integer.parseInt(versao)>version){
              
             File f = update.gravaArquivoDeURL(url,System.getProperty("user.dir"),String.valueOf(version),versao);
            
             
            
             if(update.isS()) {
                 Runtime.getRuntime().exec(location+"\\update.exe");
                 System.exit(0);
         }
         }

          
    }
