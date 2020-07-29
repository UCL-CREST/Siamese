    protected void launchMediaApplication() {
        if (audio_app != null || video_app != null) {
            printLog("DEBUG: media application is already running", LogLevel.HIGH);
            return;
        }
        SessionDescriptor local_sdp = new SessionDescriptor(call.getLocalSessionDescriptor());
        String local_media_address = (new Parser(local_sdp.getConnection().toString())).skipString().skipString().getString();
        int local_audio_port = 0;
        int local_video_port = 0;
        for (Enumeration<MediaDescriptor> e = local_sdp.getMediaDescriptors().elements(); e.hasMoreElements(); ) {
            MediaField media = ((MediaDescriptor) e.nextElement()).getMedia();
            if (media.getMedia().equals("audio")) local_audio_port = media.getPort();
            if (media.getMedia().equals("video")) local_video_port = media.getPort();
        }
        SessionDescriptor remote_sdp = new SessionDescriptor(call.getRemoteSessionDescriptor());
        String remote_media_address = (new Parser(remote_sdp.getConnection().toString())).skipString().skipString().getString();
        int remote_audio_port = 0;
        int remote_video_port = 0;
        for (Enumeration<MediaDescriptor> e = remote_sdp.getMediaDescriptors().elements(); e.hasMoreElements(); ) {
            MediaField media = ((MediaDescriptor) e.nextElement()).getMedia();
            if (media.getMedia().equals("audio")) remote_audio_port = media.getPort();
            if (media.getMedia().equals("video")) remote_video_port = media.getPort();
        }
        int dir = 0;
        if (userProfile.recv_only) dir = -1; else if (userProfile.send_only) dir = 1;
        if (((MediaUserAgentProfile) userProfile).audio && local_audio_port != 0 && remote_audio_port != 0) {
            if (((MediaUserAgentProfile) userProfile).use_rat) {
                audio_app = new RATLauncher(((MediaUserAgentProfile) userProfile).bin_rat, local_audio_port, remote_media_address, remote_audio_port, log);
            } else if (((MediaUserAgentProfile) userProfile).use_jmf) {
                try {
                    Class myclass = Class.forName("local.ua.JMFAudioLauncher");
                    Class[] parameter_types = { java.lang.Integer.TYPE, Class.forName("java.lang.String"), java.lang.Integer.TYPE, java.lang.Integer.TYPE, Class.forName("org.jjsip.tools.Log") };
                    Object[] parameters = { new Integer(local_audio_port), remote_media_address, new Integer(remote_audio_port), new Integer(dir), log };
                    java.lang.reflect.Constructor constructor = myclass.getConstructor(parameter_types);
                    audio_app = (MediaLauncher) constructor.newInstance(parameters);
                } catch (Exception e) {
                    printException(e, LogLevel.HIGH);
                    printLog("Error trying to create the JMFAudioLauncher", LogLevel.HIGH);
                }
            }
            if (audio_app == null) {
                String audio_in = null;
                if (userProfile.send_tone) audio_in = JAudioLauncher.TONE; else if (userProfile.send_file != null) audio_in = userProfile.send_file;
                String audio_out = null;
                if (userProfile.recv_file != null) audio_out = userProfile.recv_file;
                audio_app = new JAudioLauncher(local_audio_port, remote_media_address, remote_audio_port, dir, audio_in, audio_out, ((MediaUserAgentProfile) userProfile).audio_sample_rate, ((MediaUserAgentProfile) userProfile).audio_sample_size, ((MediaUserAgentProfile) userProfile).audio_frame_size, log);
            }
            audio_app.startMedia();
        }
        if (((MediaUserAgentProfile) userProfile).video && local_video_port != 0 && remote_video_port != 0) {
            if (((MediaUserAgentProfile) userProfile).use_vic) {
                video_app = new VICLauncher(((MediaUserAgentProfile) userProfile).bin_vic, local_video_port, remote_media_address, remote_video_port, log);
            } else if (((MediaUserAgentProfile) userProfile).use_jmf) {
                try {
                    Class myclass = Class.forName("local.ua.JMFVideoLauncher");
                    Class[] parameter_types = { java.lang.Integer.TYPE, Class.forName("java.lang.String"), java.lang.Integer.TYPE, java.lang.Integer.TYPE, Class.forName("org.jjsip.tools.Log") };
                    Object[] parameters = { new Integer(local_video_port), remote_media_address, new Integer(remote_video_port), new Integer(dir), log };
                    java.lang.reflect.Constructor constructor = myclass.getConstructor(parameter_types);
                    video_app = (MediaLauncher) constructor.newInstance(parameters);
                } catch (Exception e) {
                    printException(e, LogLevel.HIGH);
                    printLog("Error trying to create the JMFVideoLauncher", LogLevel.HIGH);
                }
            }
            if (video_app == null) {
                printLog("No external video application nor JMF has been provided: Video not started", LogLevel.HIGH);
                return;
            }
            video_app.startMedia();
        }
    }
