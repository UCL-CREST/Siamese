    public static void main(String[] args) {
        try {
            {
                byte[] bytes1 = { (byte) 2, (byte) 2, (byte) 3, (byte) 0, (byte) 9 };
                byte[] bytes2 = { (byte) 99, (byte) 2, (byte) 2, (byte) 3, (byte) 0, (byte) 9 };
                System.out.println("Bytes 2,2,3,0,9 as Base64: " + encodeBytes(bytes1));
                System.out.println("Bytes 2,2,3,0,9 w/ offset: " + encodeBytes(bytes2, 1, bytes2.length - 1));
                byte[] dbytes = decode(encodeBytes(bytes1));
                System.out.print(encodeBytes(bytes1) + " decoded: ");
                for (int i = 0; i < dbytes.length; i++) System.out.print(dbytes[i] + (i < dbytes.length - 1 ? "," : "\n"));
            }
            {
                java.io.FileInputStream fis = new java.io.FileInputStream("test.gif.b64");
                Base64.InputStream b64is = new Base64.InputStream(fis, DECODE);
                byte[] bytes = new byte[0];
                int b = -1;
                while ((b = b64is.read()) >= 0) {
                    byte[] temp = new byte[bytes.length + 1];
                    System.arraycopy(bytes, 0, temp, 0, bytes.length);
                    temp[bytes.length] = (byte) b;
                    bytes = temp;
                }
                b64is.close();
                javax.swing.ImageIcon iicon = new javax.swing.ImageIcon(bytes);
                javax.swing.JLabel jlabel = new javax.swing.JLabel("Read from test.gif.b64", iicon, 0);
                javax.swing.JFrame jframe = new javax.swing.JFrame();
                jframe.getContentPane().add(jlabel);
                jframe.pack();
                jframe.show();
                java.io.FileOutputStream fos = new java.io.FileOutputStream("test.gif_out");
                fos.write(bytes);
                fos.close();
                fis = new java.io.FileInputStream("test.gif_out");
                b64is = new Base64.InputStream(fis, ENCODE);
                byte[] ebytes = new byte[0];
                b = -1;
                while ((b = b64is.read()) >= 0) {
                    byte[] temp = new byte[ebytes.length + 1];
                    System.arraycopy(ebytes, 0, temp, 0, ebytes.length);
                    temp[ebytes.length] = (byte) b;
                    ebytes = temp;
                }
                b64is.close();
                String s = new String(ebytes);
                javax.swing.JTextArea jta = new javax.swing.JTextArea(s);
                javax.swing.JScrollPane jsp = new javax.swing.JScrollPane(jta);
                jframe = new javax.swing.JFrame();
                jframe.setTitle("Read from test.gif_out");
                jframe.getContentPane().add(jsp);
                jframe.pack();
                jframe.show();
                fos = new java.io.FileOutputStream("test.gif.b64_out");
                fos.write(ebytes);
                fis = new java.io.FileInputStream("test.gif.b64_out");
                b64is = new Base64.InputStream(fis, DECODE);
                byte[] edbytes = new byte[0];
                b = -1;
                while ((b = b64is.read()) >= 0) {
                    byte[] temp = new byte[edbytes.length + 1];
                    System.arraycopy(edbytes, 0, temp, 0, edbytes.length);
                    temp[edbytes.length] = (byte) b;
                    edbytes = temp;
                }
                b64is.close();
                iicon = new javax.swing.ImageIcon(edbytes);
                jlabel = new javax.swing.JLabel("Read from test.gif.b64_out", iicon, 0);
                jframe = new javax.swing.JFrame();
                jframe.getContentPane().add(jlabel);
                jframe.pack();
                jframe.show();
            }
            {
                java.io.FileInputStream fis = new java.io.FileInputStream("test.gif_out");
                byte[] rbytes = new byte[0];
                int b = -1;
                while ((b = fis.read()) >= 0) {
                    byte[] temp = new byte[rbytes.length + 1];
                    System.arraycopy(rbytes, 0, temp, 0, rbytes.length);
                    temp[rbytes.length] = (byte) b;
                    rbytes = temp;
                }
                fis.close();
                java.io.FileOutputStream fos = new java.io.FileOutputStream("test.gif.b64_out2");
                Base64.OutputStream b64os = new Base64.OutputStream(fos, ENCODE);
                b64os.write(rbytes);
                b64os.close();
                fis = new java.io.FileInputStream("test.gif.b64_out2");
                byte[] rebytes = new byte[0];
                b = -1;
                while ((b = fis.read()) >= 0) {
                    byte[] temp = new byte[rebytes.length + 1];
                    System.arraycopy(rebytes, 0, temp, 0, rebytes.length);
                    temp[rebytes.length] = (byte) b;
                    rebytes = temp;
                }
                fis.close();
                String s = new String(rebytes);
                javax.swing.JTextArea jta = new javax.swing.JTextArea(s);
                javax.swing.JScrollPane jsp = new javax.swing.JScrollPane(jta);
                javax.swing.JFrame jframe = new javax.swing.JFrame();
                jframe.setTitle("Read from test.gif.b64_out2");
                jframe.getContentPane().add(jsp);
                jframe.pack();
                jframe.show();
                fos = new java.io.FileOutputStream("test.gif_out2");
                b64os = new Base64.OutputStream(fos, DECODE);
                b64os.write(rebytes);
                b64os.close();
                javax.swing.ImageIcon iicon = new javax.swing.ImageIcon("test.gif_out2");
                javax.swing.JLabel jlabel = new javax.swing.JLabel("Read from test.gif_out2", iicon, 0);
                jframe = new javax.swing.JFrame();
                jframe.getContentPane().add(jlabel);
                jframe.pack();
                jframe.show();
            }
            {
                java.io.FileInputStream fis = new java.io.FileInputStream("D:\\temp\\testencoding.txt");
                Base64.InputStream b64is = new Base64.InputStream(fis, DECODE);
                java.io.FileOutputStream fos = new java.io.FileOutputStream("D:\\temp\\file.zip");
                int b;
                while ((b = b64is.read()) >= 0) fos.write(b);
                fos.close();
                b64is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
