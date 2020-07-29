    public void Transcode() {
        File ListDir = new File(m_GUI.strOutputDir + "/dv/");
        if (m_Quality == 0) m_flags = "'8, -c -q 2 -4 1 -2 1 -H -K file=matrix.txt -R 2'"; else if (m_Quality == 1) m_flags = "'8, -c -q 4 -4 2 -2 2 -K file=matrix.txt -R 1'"; else m_flags = "'8, -c -q 6 -4 3 -2 3 -N 0.5 -E -10 -K tmpgenc -R 0'";
        m_transcode = m_transcode.replaceAll("flags", m_flags);
        m_transcode = m_transcode.replaceAll("bitr", m_bitrate);
        m_transcode = m_transcode.replaceAll("format", m_Format);
        m_transcode = m_transcode.replaceAll("aspectRatio", m_AspectRatio);
        m_transcode = m_transcode.replaceAll("frames/s", String.valueOf(m_fps));
        matrix();
        m_GUI.lblConvert.setEnabled(true);
        m_GUI.prgConvert.setEnabled(true);
        m_GUI.lblConvertProg.setEnabled(true);
        double cummulative_time = 0;
        double current_time = 0;
        double[] average_fps = new double[5];
        average_fps[4] = -1;
        int hour, min, sec;
        int total_time = (((Integer) m_GUI.spnMinutes.getValue()).intValue() * 60) + ((Integer) m_GUI.spnSeconds.getValue()).intValue();
        StringTokenizer st, time;
        while (thread_track < ListDir.list().length) {
            video_files = ListDir.list();
            try {
                if (thread_track == 0) {
                    m_transcode = m_transcode.replaceAll("inp", video_files[thread_track]);
                    m_transcode = m_transcode.replaceAll("test1", video_files[thread_track].substring(0, 11));
                    mplex = mplex.replaceAll("inp", video_files[thread_track].substring(0, 11));
                } else {
                    m_transcode = m_transcode.replaceAll(video_files[thread_track - 1], video_files[thread_track]);
                    m_transcode = m_transcode.replaceAll(video_files[thread_track - 1].substring(0, 11), video_files[thread_track].substring(0, 11));
                    mplex = mplex.replaceAll(video_files[thread_track - 1].substring(0, 11), video_files[thread_track].substring(0, 11));
                }
            } catch (StringIndexOutOfBoundsException ex) {
                SaveStackTrace.printTrace(m_GUI.strOutputDir, ex);
                m_GUI.MessageBox(m_BaseErr + "Are there non-standard (not .dv)" + "\nfiles in the dv directory?\n" + ex.toString(), 0);
                ex.printStackTrace();
                m_Error = true;
            }
            try {
                String[] transcode_cmd = { "/bin/sh", "-c", m_transcode };
                Process p = Runtime.getRuntime().exec(transcode_cmd, null, new File(m_GUI.strOutputDir));
                BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedReader err_in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                BufferedWriter out = new BufferedWriter(new FileWriter(m_GUI.strOutputDir + "/log/transcode.log"));
                BufferedWriter err_out = new BufferedWriter(new FileWriter(m_GUI.strOutputDir + "/log/transcode.err"));
                out.write(m_transcode);
                out.newLine();
                String line;
                if (err_in.ready()) {
                    line = err_in.readLine();
                    if (line.equals("/bin/sh: transcode: command not found")) {
                        m_GUI.MessageBox("Could not locate transcode in your path." + "\nPlease install all necessary dependencies" + "\nand rerun dvd-homevideo.", 0);
                        in.close();
                        out.close();
                        err_in.close();
                        err_out.close();
                        m_Error = true;
                    } else {
                        m_GUI.txtAreaOutput.append(line + "\n");
                        out.write(line);
                        out.newLine();
                    }
                }
                int k = 0;
                while ((line = in.readLine()) != null) {
                    st = new StringTokenizer(line);
                    if (st.hasMoreTokens()) {
                        if (st.nextToken().equals("encoding")) {
                            st.nextToken();
                            st.nextToken();
                            average_fps[k % 5] = Double.valueOf(st.nextToken()).doubleValue();
                            st.nextToken();
                            st.nextToken();
                            time = new StringTokenizer(st.nextToken(), ":,");
                            if (time.hasMoreTokens()) {
                                hour = Integer.valueOf(time.nextToken()).intValue();
                                min = Integer.valueOf(time.nextToken()).intValue();
                                sec = Integer.valueOf(time.nextToken()).intValue();
                                current_time = (hour * 3600) + (min * 60) + sec + cummulative_time;
                                m_GUI.prgConvert.setValue((int) ((current_time / total_time) * 100));
                                m_GUI.lblConvertProg.setText(String.valueOf((int) ((current_time / total_time) * 100)) + "%");
                                double fps_sum = 0;
                                int time_remaining;
                                if (average_fps[4] != -1) {
                                    for (int j = 0; j < 5; j++) {
                                        fps_sum += average_fps[j];
                                    }
                                    fps_sum /= 5;
                                    time_remaining = (int) (((total_time - current_time) * (int) m_fps) / fps_sum);
                                    int captureTipHour = (time_remaining / 3600);
                                    int captureTipMinute = (time_remaining - 3600 * captureTipHour) / 60;
                                    if ((captureTipHour == 0) && (captureTipMinute < 5)) m_GUI.prgConvert.setToolTipText("Less than 5 minutes remaining"); else if (captureTipMinute < 10) m_GUI.prgConvert.setToolTipText(captureTipHour + ":0" + captureTipMinute + " Remaining"); else m_GUI.prgConvert.setToolTipText(captureTipHour + ":" + captureTipMinute + " Remaining");
                                }
                                m_GUI.txtAreaOutput.append(line + "\n");
                                m_GUI.txtAreaOutput.setCaretPosition(m_GUI.txtAreaOutput.getDocument().getLength());
                                out.write(line);
                                out.newLine();
                            } else {
                                m_GUI.txtAreaOutput.append(line + "\n");
                                m_GUI.txtAreaOutput.setCaretPosition(m_GUI.txtAreaOutput.getDocument().getLength());
                                out.write(line);
                                out.newLine();
                            }
                            k++;
                        }
                    }
                }
                while ((line = err_in.readLine()) != null) {
                    m_GUI.txtAreaOutput.append(line + "\n");
                    m_GUI.txtAreaOutput.setCaretPosition(m_GUI.txtAreaOutput.getDocument().getLength());
                    out.write(line);
                    out.newLine();
                }
                in.close();
                out.close();
                err_in.close();
                err_out.close();
                cummulative_time = current_time;
                String[] mplex_cmd = { "/bin/sh", "-c", mplex };
                p = Runtime.getRuntime().exec(mplex_cmd, null, new File(m_GUI.strOutputDir));
                in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                err_in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                out = new BufferedWriter(new FileWriter(m_GUI.strOutputDir + "/log/mplex.log"));
                err_out = new BufferedWriter(new FileWriter(m_GUI.strOutputDir + "/log/mplex.err"));
                if (err_in.ready()) {
                    line = err_in.readLine();
                    if (line.equals("/bin/sh: mplex: command not found")) {
                        m_GUI.MessageBox("Could not locate mplex in your path." + "\nPlease install all necessary dependencies" + "\nand rerun dvd-homevideo.", 0);
                        in.close();
                        out.close();
                        err_in.close();
                        err_out.close();
                        m_Error = true;
                    } else {
                        m_GUI.txtAreaOutput.append(line + "\n");
                        out.write(line);
                        out.newLine();
                    }
                }
                while ((line = err_in.readLine()) != null) {
                    m_GUI.txtAreaOutput.append(line + "\n");
                    m_GUI.txtAreaOutput.setCaretPosition(m_GUI.txtAreaOutput.getDocument().getLength());
                    out.write(line);
                    out.newLine();
                }
                in.close();
                out.close();
                err_in.close();
                err_out.close();
                Thread.sleep(2000);
            } catch (IOException ex) {
                SaveStackTrace.printTrace(m_GUI.strOutputDir, ex);
                m_GUI.MessageBox(m_BaseErr + "IO Error\n" + ex.toString(), 0);
                ex.printStackTrace();
                m_Error = true;
            } catch (NullPointerException ex) {
                SaveStackTrace.printTrace(m_GUI.strOutputDir, ex);
                m_GUI.MessageBox(m_BaseErr + "Error executing Runtime.getRuntime().exec()\n" + ex.toString(), 0);
                ex.printStackTrace();
                m_Error = true;
            } catch (IllegalArgumentException ex) {
                SaveStackTrace.printTrace(m_GUI.strOutputDir, ex);
                m_GUI.MessageBox(m_BaseErr + "Illegal argument sent to Runtime.getRuntime().exec()\n" + ex.toString(), 0);
                ex.printStackTrace();
                m_Error = true;
            } catch (Exception ex) {
                SaveStackTrace.printTrace(m_GUI.strOutputDir, ex);
                m_GUI.MessageBox(m_BaseErr + "Unknown Error occurred\n" + ex.toString(), 0);
                ex.printStackTrace();
                m_Error = true;
            }
            thread_track++;
        }
        m_GUI.prgConvert.setValue(100);
        m_GUI.lblConvertProg.setText("100%");
        m_GUI.prgConvert.setEnabled(false);
        m_GUI.lblConvert.setEnabled(false);
        m_GUI.lblConvertProg.setEnabled(false);
    }
