    public boolean doUpload(int count) {
        String objFileName = Long.toString(new java.util.Date().getTime()) + Integer.toString(count);
        try {
            this.objectFileName[count] = objFileName + "_bak." + this.sourceFileExt[count];
            File objFile = new File(this.contextPath + "/" + this.savePath, this.objectFileName[count]);
            if (objFile.exists()) {
                this.doUpload(count);
            } else {
                objFile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(objFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            int readLength = 0;
            int offset = 0;
            String str = "";
            long readSize = 0L;
            while ((readLength = this.inStream.readLine(this.b, 0, this.b.length)) != -1) {
                str = new String(this.b, 0, readLength);
                if (str.indexOf("Content-Type:") != -1) {
                    break;
                }
            }
            this.inStream.readLine(this.b, 0, this.b.length);
            while ((readLength = this.inStream.readLine(this.b, 0, b.length)) != -1) {
                str = new String(this.b, 0, readLength);
                if (this.b[0] == 45 && this.b[1] == 45 && this.b[2] == 45 && this.b[3] == 45 && this.b[4] == 45) {
                    break;
                }
                bos.write(this.b, 0, readLength);
                readSize += readLength;
                if (readSize > this.size) {
                    this.fileMessage[count] = "�ϴ��ļ������ļ���С�������ƣ�";
                    this.ok = false;
                    break;
                }
            }
            if (this.ok) {
                bos.flush();
                bos.close();
                int fileLength = (int) (objFile.length());
                byte[] bb = new byte[fileLength - 2];
                FileInputStream fis = new FileInputStream(objFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                bis.read(bb, 0, (fileLength - 2));
                fis.close();
                bis.close();
                this.objectFileName[count] = objFileName + "." + this.sourceFileExt[count];
                File ok_file = new File(this.contextPath + "/" + this.savePath, this.objectFileName[count]);
                ok_file.createNewFile();
                BufferedOutputStream bos_ok = new BufferedOutputStream(new FileOutputStream(ok_file));
                bos_ok.write(bb);
                bos_ok.close();
                objFile.delete();
                this.fileMessage[count] = "OK";
                return true;
            } else {
                bos.flush();
                bos.close();
                File delFile = new File(this.contextPath + "/" + this.savePath, this.objectFileName[count]);
                delFile.delete();
                this.objectFileName[count] = "none";
                return false;
            }
        } catch (Exception e) {
            this.objectFileName[count] = "none";
            this.fileMessage[count] = e.toString();
            return false;
        }
    }
