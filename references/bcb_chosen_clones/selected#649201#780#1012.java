	public void upload(){
		try{
			HttpClient client = establishConnection();
			
			if(client == null){
				return;
			}
			
			Vector<File> successFiles = new Vector<File>(10);
			String startDB = "";
			String endDB = "";
			
			//ファイルのアップロード
			setLog("アップロード開始");
			for(int i = 0; i < logFiles_.length; i++){
				if(!isRunning_){
					//アップロードを中止して切断
					releaseConnection(client);
					
					break;
				}
				
				if(logFiles_[i].exists()){
					//ファイル形式のチェックとコメントの取得
					FileInputStream fis = new FileInputStream(logFiles_[i]);
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader br = new BufferedReader(isr);
                    
                    String comment = "";
                    String line = null;
                    if((line = br.readLine()) != null){
                    	if(line.startsWith("#LockyStumbler Log")){
    						//LockyStumbler Logには2行目に半角100文字のコメントがある
    						if((line = br.readLine()) != null){
    							if(line.startsWith("#")){
    								comment = line.substring(1);
    								
    								//コメント行の後ろにある半角スペースを除去
    								while(comment.endsWith(" ")){
										comment = comment.substring(0, comment.length() - 1);
									}
    							}
    						}
    					}
                    }
                    
                    fis.close();
                    isr.close();
                    br.close();
                    
                    //POSTメソッドの作成
					PostMethod uploadMethod = new PostMethod("/member/result.html");
					
					uploadMethod.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE, true);
					Part[] parts = { new StringPart("from", "logbrowser"), new StringPart("comment", comment), new FilePart("fileName", logFiles_[i], "text/plain", null) };
					uploadMethod.setRequestEntity(new MultipartRequestEntity(parts, uploadMethod.getParams()));
					
					
					client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
					
					//POSTデータの送信
					int statusCode = client.executeMethod(uploadMethod);
					
					if(statusCode == HttpStatus.SC_OK){
    					//データベースに登録された始点と終点を取得する
    					String response = uploadMethod.getResponseBodyAsString();
    					
    					String start = response.substring(0, response.indexOf("¥t"));
    					String end = response.substring(response.indexOf("¥t") + 1);
    					
    					//始点の初期値設定
    					if(startDB.equals("")){
    						startDB = start;
    					}
    					
    					//終点の初期値設定
    					if(endDB.equals("")){
    						endDB = end;
    					}
    					
    					//終点の更新
    					if(Integer.parseInt(endDB) < Integer.parseInt(end)){
    						endDB = end;
    					}
    					
    					//送信成功ファイルに追加
    					successFiles.add(logFiles_[i]);
    					
    					//終了処理が開始されている場合は出力しない
    					if(isRunning_){
    						setLog(logFiles_[i].getName() + "¥t[ SUCCESS ]");
    					}
					}
					
					uploadMethod.releaseConnection();
					
					setProgress(i + 1);
				}
			}
			if(isRunning_){
				setLog("アップロード終了");
			}
			
			
			
			//アップロードの結果を表示
			String view = readParameter(UPLOAD_RESULT);
			if(!isRunning_){
				//終了処理中は結果を表示しない
			}
			else if(view.equals("MAP")){
				//新規発見アクセスポイントをマップに表示する
				MessageDigest md5 = MessageDigest.getInstance("MD5");
            	md5.update(accountName_.getBytes());
            	byte[] digest = md5.digest();
            	
            	//ダイジェストを文字列に変換
            	String userNameDigest = "";
            	for(int i = 0; i < digest.length; i++){
            		int d = digest[i];
            		if(d < 0){
            			//byte型では128‾255が負になっているので補正
            			d += 256;
            		}
            		if(d < 16){
            			//2桁に調節
            			userNameDigest += "0";
            		}
            		
            		//ダイジェスト値の1バイトを16進数2桁で表示
            		userNameDigest += Integer.toString(d, 16);
            	}
            	
            	//始点と終点を正常に取得できなかった場合
            	if(startDB.equals("")){
            		startDB = "0";
            	}
            	if(endDB.equals("")){
            		endDB = "0";
            	}
            	
            	//新規発見数が零の場合は表示しない
            	if(startDB.equals("0")&&endDB.equals("0")){
            		setLog("新規発見数： 0");
            	}
            	else{
            		ProcessBuilder process = new ProcessBuilder(readParameter(WEB_BROWSER), "http://" + readParameter(WEB_HOST) + "/service/logviewer.html?user=" + userNameDigest + "&start=" + startDB + "&end=" + endDB);
                	process.start();
            	}
			}
			else if(view.equals("TEXT")){
				if(startDB.equals("")||endDB.equals("")){
            		//情報に不備ある場合は表示しない
					setLog("受信情報が欠けているため表示できません");
            	}
				else{
					int newCount = Integer.parseInt(endDB) - Integer.parseInt(startDB);
    				setLog("新規発見数： " + String.valueOf(newCount));
				}
			}
			
			
			//アップロードしたファイルのフラグを変更
			for(int i = 0; i < successFiles.size(); i++){
				try{
					RandomAccessFile file = new RandomAccessFile(successFiles.get(i), "rw");
					
					//ログファイル情報を取得
					String line;
					String seekString = "";
					while((line = file.readLine()) != null){
						if(line.startsWith("#LockyStumbler Log")){
							
							//ログファイルのバージョンを確認
							int version = Integer.parseInt(line.substring("#LockyStumbler Log Version ".length()));
							if(version < 2){
								return;
							}
							
							
							//2行目までの文字列を記録
							//seekString += line + "¥r¥n" + file.readLine() + "¥r¥n";
							file.readLine();
							long pos = file.getFilePointer();
							
							//3行目の付加情報を取得
							line = file.readLine();
							String[] token = line.substring(1).split("[|]");
							for(int j = 0; j < token.length; j++){
								if(token[j].startsWith("UPLOAD=")){
									//ファイルのアップロードフラグを更新
									//file.seek((seekString + "|UPLOAD=").length());
									file.seek(pos + "|UPLOAD=".length());
									file.write("T".getBytes());
								}
								else{
									//seekString += "|" + token[j];
									pos += ("|" + token[j]).length();
								}
							}
						}
					}
					
					file.close();
				}
				catch(FileNotFoundException exception){
					exception.printStackTrace();
				}
				catch(IOException exception){
					exception.printStackTrace();
				}
			}
			
			//アップロード中断
			if(!isRunning_){
				//終了処理待機ループの解除
				isRunning_ = true;
				return;
			}
			
			//アップロード正常終了
			isRunning_ = false;
			enableClose();
			releaseConnection(client);
		}
		catch(IOException exception){
			exception.printStackTrace();
		}
		catch(NoSuchAlgorithmException exception){
			exception.printStackTrace();
			setLog("JREのバージョンが古いため表示できませんでした");
		}
	}
