    public void run() {
        checkupdates.BetaST.setText("");
        checkupdates.stableST.setText("");
        
        String[] s = new String[7];
       int i=0;
        try {
            
            URL url = new URL("http://memorize-words.sourceforge.net/latest.txt");
            
            BufferedReader in = new BufferedReader(new
                    InputStreamReader(url.openStream()));
            String str;
           
            while (((str = in.readLine()) != null)&&(i<7)) {
                
                s[i]=str;
                i++;
            }
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
       if (i<5) 
       { checkupdates.CheckBT.setEnabled(true);
           return;
       }
       //S[0]=latest Beta version
       //S[1]=latest Stable version
       //s[2]=Beta version download path
       //s[3]=Stable version download path
       //s[4]= Beta name
       //s[5]=Stable name
       boolean updated=false;
       if ((MF.CurVersion<Integer.parseInt(s[0]))&&(checkupdates.BetaCHK.isSelected()))
       {
       checkupdates.BetaST.setText("<HTML>A newer BETA version (<b>"+s[4]+"</b>) is available at<BR> <a href=''>"+s[2]+"</a></HTML>");
       updated=true;
       checkupdates.hasBeta=true;
       checkupdates.BetaURL=s[2];
       }else
           checkupdates.BetaST.setText("");
           
       if ((MF.CurVersion<Integer.parseInt(s[1]))&&(checkupdates.StableCHK.isSelected()))
       {
       checkupdates.stableST.setText("<HTML>A newer STABLE version (<b>"+s[5]+"</b>) is available at<BR> <a href=''>"+s[3]+"</a></HTML>");
       updated=true;
       checkupdates.hasStable=true;
       checkupdates.StableURL=s[3];
       }
       else
           checkupdates.stableST.setText("");
       
       if (updated)
           checkupdates.setVisible(true);
       checkupdates.CheckBT.setEnabled(true);
       // "A newer stable version (memorize-words 1-2-2) is available at http://"
       //   MF.CurVersion;
    }
