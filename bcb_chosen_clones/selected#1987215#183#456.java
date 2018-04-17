    public Object process(Object request) {
        this.logger.debug("Processing " + request);
        if (request instanceof Ping) return request;
        if (request instanceof GetRecInfo) {
            GetRecInfoResponse response = new GetRecInfoResponse();
            for (Timer recording : this.state.getRecordings()) response.getRecordings().add(recording.toRecordingDescription());
            return response;
        }
        if (request instanceof GetSysInfo) {
            GetSysInfoResponse response = new GetSysInfoResponse();
            response.setSysInfo(this.state.getSysInfo().toDescription());
            return response;
        }
        if (request instanceof ChangeChannel) {
            ChangeChannel cc = (ChangeChannel) request;
            ChannelSelection current = new ChannelSelection();
            current.setType(this.state.getCurrentChannel().getType());
            current.setNumber(cc.getService());
            this.state.setCurrentChannel(current);
            return request;
        }
        if (request instanceof GetChannelList) {
            GetChannelListResponse response = new GetChannelListResponse();
            ArrayList<ChannelDescription> channels = new ArrayList<ChannelDescription>(32);
            for (Station station : this.state.getStations(((GetChannelList) request).getType()).getStations()) {
                ChannelDescription desc = new ChannelDescription();
                desc.setAudioPid(station.getAudioPid());
                desc.setDolby(station.getDolby());
                desc.setFlags(station.getFlags());
                desc.setFrequency(station.getFrequency() / 100000);
                desc.setLogicalChannel(station.getLogicalChannel());
                desc.setMultifeed(station.getMultifeed());
                desc.setName(station.getLabel());
                desc.setOriginatingNetworkId(station.getOriginatingNetworkId());
                desc.setPcrId(station.getPcrId());
                desc.setPmtId(station.getPmtId());
                desc.setPolar(station.isPolar());
                desc.setSatellite(station.getSatellite());
                desc.setSatelliteIndex(station.getSatelliteIndex());
                desc.setServiceId(station.getServiceId());
                desc.setSr(station.getSr());
                desc.setTransportStreamId(station.getTransportStreamId());
                desc.setTtxAvailable(station.isTtxAvailable());
                desc.setTunerNumber(station.getTunerNumber());
                desc.setVideoPid(station.getVideoPid());
                channels.add(desc);
            }
            response.setChannels(channels);
            return response;
        }
        if (request instanceof GetOneDaysEvents) {
            GetOneDaysEvents req = (GetOneDaysEvents) request;
            GetOneDaysEventsResponse response = new GetOneDaysEventsResponse();
            Calendar to = Calendar.getInstance();
            to.setTime(req.getDate());
            to.add(Calendar.DAY_OF_MONTH, 1);
            for (Channel epg : this.state.getEPG()) {
                if (epg != null) {
                    List<EventDescription> events = response.getEvents();
                    for (Event evt : epg.findEvents(req.getDate(), to.getTime())) {
                        EventDescription ed = new EventDescription();
                        ed.setDescription(evt.getDescription());
                        ed.setDuration(evt.getDuration());
                        ed.setEventId(evt.getEventId());
                        ed.setFinish(evt.getFinish());
                        ed.setRunning(evt.isRunning());
                        ed.setServiceId(evt.getContext().getIndex());
                        ed.setStart(evt.getStart());
                        ed.setTitle(evt.getTitle());
                        ed.setTransportStreamId(this.state.getStation(ServiceType.TV, evt.getContext().getIndex()).getTransportStreamId());
                        events.add(ed);
                    }
                }
            }
            return response;
        }
        if (request instanceof GetOneChannelEvents) {
            GetOneChannelEvents req = (GetOneChannelEvents) request;
            GetOneChannelEventsResponse response = new GetOneChannelEventsResponse();
            Channel epg = this.state.getEPG(ServiceType.TV, req.getService());
            if (epg != null) {
                List<EventDescription> events = response.getEvents();
                for (Event evt : epg.findEvents(req.getFrom(), req.getTo())) {
                    EventDescription ed = new EventDescription();
                    ed.setDescription(evt.getDescription());
                    ed.setDuration(evt.getDuration());
                    ed.setEventId(evt.getEventId());
                    ed.setFinish(evt.getFinish());
                    ed.setRunning(evt.isRunning());
                    ed.setServiceId(evt.getContext().getIndex());
                    ed.setStart(evt.getStart());
                    ed.setTitle(evt.getTitle());
                    ed.setTransportStreamId(this.state.getStation(ServiceType.TV, evt.getContext().getIndex()).getTransportStreamId());
                    events.add(ed);
                }
            }
            return response;
        }
        if (request instanceof GetTimersList) {
            GetTimersListResponse response = new GetTimersListResponse();
            for (Timer timer : this.state.getTimers()) response.getTimers().add(timer.toDescription());
            return response;
        }
        if (request instanceof GetCurrentChannel) {
            GetCurrentChannelResponse response = new GetCurrentChannelResponse();
            response.setChannel(this.state.getCurrentChannel());
            return response;
        }
        if (request instanceof AddTimer) {
            AddTimerResponse response = new AddTimerResponse();
            TimerDescription timer = ((AddTimer) request).getTimerInfo().toTimer();
            this.state.addTimer(timer);
            return response;
        }
        if (request instanceof ModifyTimer) {
            ModifyTimerInfo info = ((ModifyTimer) request).getTimerInfo();
            ModifyTimerResponse response = new ModifyTimerResponse();
            Timer timer = this.state.getTimers().get(info.slot);
            TimerDescription desc = timer.toDescription();
            desc.setDuration(info.duration);
            desc.setFileName(info.fileName);
            desc.setRecording(info.recording);
            desc.setReservationType(info.reservationType);
            desc.setServiceId(info.serviceId);
            desc.setServiceType(info.serviceType);
            this.state.getTimers().set(info.slot, new Timer(this.state, desc));
            return response;
        }
        if (request instanceof DeleteTimer) {
            DeleteTimer req = (DeleteTimer) request;
            DeleteTimerResponse response = new DeleteTimerResponse();
            int slot = req.getSlot();
            List<Timer> timers = this.state.getTimers();
            timers.remove(slot);
            while (slot < timers.size()) {
                timers.get(slot).setSlot(slot + 1);
                slot++;
            }
            return response;
        }
        if (request instanceof ListFiles) {
            ListFiles req = (ListFiles) request;
            ListFilesResponse response = new ListFilesResponse();
            TestDirectory dir = (TestDirectory) this.fileSystem.find(req.getDirectory());
            if (dir != null) response.getFiles().addAll(dir.asFileInfos());
            return response;
        }
        if (request instanceof GetPlayInfo) {
            GetPlayInfoResponse response = new GetPlayInfoResponse();
            Playback play = this.state.getPlayInfo();
            if (play != null) {
                response.setEvent(play.getEvent() == null ? null : play.getEvent().toDescription());
                response.setFileInfo(play.getFile());
                response.setPlayInfo(play.toDescription());
            }
            return response;
        }
        if (request instanceof GetFileInfos) {
            GetFileInfos req = (GetFileInfos) request;
            GetFileInfosResponse response = new GetFileInfosResponse();
            TestDirectory dir = (TestDirectory) this.fileSystem.find(req.getDirectory());
            if (dir != null) response.getFiles().addAll(dir.asRecordingInfos());
            return response;
        }
        if (request instanceof PlayTS) {
            PlayTS req = (PlayTS) request;
            PlayTSResponse response = new PlayTSResponse();
            FileEntry file = (FileEntry) this.state.getRoot().find(req.getPath());
            Channel epg = this.state.getEPG(ServiceType.TV, 0);
            if (file != null && file.isVideoPlayable()) {
                PlaybackDescription pi = new PlaybackDescription();
                pi.setCurrentBlock(100);
                pi.setDuration(35);
                pi.setPlayMode(PlayMode.PLAYING);
                pi.setServiceId(0);
                pi.setServiceType(ServiceType.TV);
                pi.setSpeed(1);
                pi.setTotalBlocks(400);
                pi.setTrickMode(TrickMode.FORWARD);
                this.state.setPlayInfo(pi, epg.findEvent(new Date()), file.getFile().toShort());
            }
            return response;
        }
        if (request instanceof StopTS) {
            StopTSResponse response = new StopTSResponse();
            this.state.setPlayInfo(null);
            return response;
        }
        if (request instanceof StartRecord) {
            if (this.getState().getRecordings().size() >= 4) return new CommandError(CommandStatus.CANT_RECORD, null);
            RecordingDescription recording = new RecordingDescription();
            recording.setDuration(120);
            recording.setFileName("Recording.mpg");
            recording.setStart(new Date());
            recording.setFinish(new Date(recording.getStart().getTime() + recording.getDuration() * 60 * 1000));
            recording.setRecorded(0);
            recording.setRecordingType(RecordingType.NORMAL);
            recording.setServiceId(this.state.getCurrentChannel().getNumber());
            recording.setServiceType(this.state.getCurrentChannel().getType());
            recording.setTuner(0x03);
            ;
            this.state.getRecordings().add(new Timer(this.state, recording, this.state.getRecordings().size()));
            this.logger.debug("Started recording " + recording + " now " + this.state.getRecordings().size() + " recordings.");
            return new StartRecordResponse();
        }
        if (request instanceof StopRecord) {
            Timer recording = this.state.getRecording(((StopRecord) request).getSlot());
            if (recording != null) this.state.getRecordings().remove(recording);
            return new StopRecordResponse();
        }
        if (request instanceof SetRecordDuration) {
            SetRecordDurationInfo info = ((SetRecordDuration) request).getInfo();
            RecordingDescription recording = this.state.getRecording(info.getSlot()).toRecordingDescription();
            if (recording != null) {
                recording.setDuration(info.getDuration());
                recording.setFinish(new Date(recording.getStart().getTime() + recording.getDuration() * 60 * 1000));
                this.state.getRecordings().set(info.getSlot(), new Timer(this.state, recording, info.getSlot()));
            }
            return new SetRecordDurationResponse();
        }
        if (request instanceof CreateDirectory) {
            this.fileSystem.createDirectory(((CreateDirectory) request).getPath());
            return new CreateDirectoryResponse();
        }
        if (request instanceof DeleteFile) {
            DeleteFile delete = (DeleteFile) request;
            if (this.fileSystem.delete(delete.getPath())) return new DeleteFileResponse(); else return new CommandError(CommandStatus.CANT_DELETE, null);
        }
        if (request instanceof RenameFile) {
            RenameFile rename = (RenameFile) request;
            if (this.fileSystem.rename(rename.getOldPath(), rename.getNewPath())) return new RenameFileResponse(); else return new CommandError(CommandStatus.CANT_RENAME, null);
        }
        if (request instanceof CreateFile) {
            CreateFile create = (CreateFile) request;
            this.fileSystem.create(create.getPath());
            return new CreateFileResponse();
        }
        if (request instanceof OpenFile) {
            OpenFile open = (OpenFile) request;
            FileInfo file = (FileInfo) this.fileSystem.find(open.getPath());
            TestOpenFile of;
            if (file == null) return new CommandError(CommandStatus.CANT_OPEN_FILE, null);
            of = new TestOpenFile(file, this.files.size() + 1);
            this.files.put(of.getFd(), of);
            return new OpenFileResponse(of.getFd());
        }
        if (request instanceof CloseFile) {
            CloseFile close = (CloseFile) request;
            TestOpenFile of;
            of = this.files.get(close.getFd());
            if (of == null) return new CommandError(CommandStatus.FILE_NOT_FOUND, null);
            of.setOpen(false);
            return new CloseFileResponse();
        }
        if (request instanceof GetFile) {
            GetFile get = (GetFile) request;
            TestOpenFile of;
            of = this.files.get(get.getFd());
            if (of == null) return new CommandError(CommandStatus.FILE_NOT_FOUND, null);
            return new GetFileResponse((int) of.getPosition(), of.readBytes(131000));
        }
        if (request instanceof SendFile) {
            SendFile send = (SendFile) request;
            TestOpenFile of;
            CRC32 crc = new CRC32();
            crc.update(send.getData());
            this.logger.debug("Received " + send.getData().length + " bytes CRC=" + crc.getValue());
            of = this.files.get(send.getFd());
            if (of == null) return new CommandError(CommandStatus.FILE_NOT_FOUND, null);
            of.writeBytes(send.getData());
            return new SendFileResponse();
        }
        return new MessageError(MessageStatus.INVALID_COMMAND, null);
    }
