package ir.amin.HaftTeen.messenger.call;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.telecom.TelecomManager;
import android.util.Log;

import ir.amin.HaftTeen.messenger.AndroidUtilities;
import ir.amin.HaftTeen.messenger.ApplicationLoader;
import ir.amin.HaftTeen.messenger.BuildVars;
import ir.amin.HaftTeen.messenger.ContactsController;
import ir.amin.HaftTeen.messenger.FileLog;
import ir.amin.HaftTeen.messenger.MessagesController;
import ir.amin.HaftTeen.messenger.NotificationCenter;
import ir.amin.HaftTeen.messenger.UserConfig;
import ir.amin.HaftTeen.messenger.voip.VoIPBaseService;
import ir.amin.HaftTeen.messenger.voip.VoIPController;
import ir.amin.HaftTeen.messenger.voip.VoIPServerConfig;
import ir.amin.HaftTeen.tgnet.ConnectionsManager;
import ir.amin.HaftTeen.tgnet.RequestDelegate;
import ir.amin.HaftTeen.tgnet.TLObject;
import ir.amin.HaftTeen.tgnet.TLRPC;
import ir.amin.HaftTeen.ui.VoIPActivity;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RtpReceiver;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CallService extends VoIPBaseService {
    public static final int STATE_HANGING_UP = 10;
    public static final int STATE_WAITING = 13;
    public static final int STATE_REQUESTING = 14;
    public static final int STATE_CONNECTING = 31;
    public static final int STATE_WAITING_INCOMING = 15;
    public static final int STATE_RINGING = 16;
    public static final int STATE_BUSY = 17;


    public static TLRPC.TL_call_callDescription incomingCall;

    private TLRPC.User user;
    private Runnable delayedOutgoingCallTask;
    private Runnable delayedIncomingCallTask;

    private long callStartTime = 0L;

    private Map<Integer, ParticipantState> participants = new HashMap<>();

    private String callId;
    private String title;
    //    private ArrayList<Integer> participants;
    private ArrayList<PeerConnection.IceServer> relays;

    public static CallService getSharedInstance() {
        return (CallService) sharedInstance;
    }

    private static PeerConnectionFactory connectionFactory;

    private AudioTrack localAudioTrack;

    @Override
    public long getCallID() {
        return 0;
    }

    private void debug(String message) {
        FileLog.d(String.format("[CallService] %s", message));
    }

    @Override
    public TLRPC.User getUser() {
        return user;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        debug("Starting call service");
        createPeerConnectionFactory();
        if (sharedInstance != null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("Tried to start the VoIP service when it's already started");
            }
            return START_NOT_STICKY;
        }

        currentAccount = intent.getIntExtra("account", -1);
        if(currentAccount == -1)
            throw new IllegalStateException("No account specified when starting VoIP service");
        int userID = intent.getIntExtra("user_id", 0);
        isOutgoing = intent.getBooleanExtra("is_outgoing", false);
        user = MessagesController.getInstance(currentAccount).getUser(userID);

        if (user == null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.w("VoIPService: user==null");
            }
            stopSelf();
            return START_NOT_STICKY;
        }
        sharedInstance = this;

        if (isOutgoing) {
            dispatchStateChanged(STATE_REQUESTING);
            if(USE_CONNECTION_SERVICE) {
                debug("Using connection service");
                TelecomManager tm = (TelecomManager) getSystemService(TELECOM_SERVICE);
                Bundle extras=new Bundle();
                Bundle myExtras=new Bundle();
                extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, addAccountToTelecomManager());
                myExtras.putInt("call_type", 1);
                extras.putBundle(TelecomManager.EXTRA_OUTGOING_CALL_EXTRAS, myExtras);
                tm.placeCall(Uri.fromParts("sip", UserConfig.getInstance(currentAccount).getClientUserId()+";user="+user.id, null), extras);
            } else {
                debug("Using delayed start");
                delayedOutgoingCallTask = () -> {
                    delayedOutgoingCallTask = null;
                    startOutgoingCall();
                };
                AndroidUtilities.runOnUIThread(delayedOutgoingCallTask, 2000);
            }
            if (intent.getBooleanExtra("start_incall_activity", false)) {
                startActivity(new Intent(this, VoIPActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        } else {
            debug("Handling incoming call");
            dispatchStateChanged(STATE_WAITING);
            delayedIncomingCallTask = () -> {
                delayedIncomingCallTask = null;
                startIncomingCall();
            };
            AndroidUtilities.runOnUIThread(delayedIncomingCallTask);
        }

        return START_NOT_STICKY;
    }

    public void participantJoined(String callId, int participantId, int flags) {
        if (checkCallId(callId)) {
            debug(String.format("Participant joined: participantId = %d; flags = %d", participantId, flags));
            PeerConnectionInfo info = createPeerConnection(participantId);
            debug("Peer connected created for participant " + participantId);
            createOffer(participantId, info.connection);
            debug("Offer created for participant " + participantId);
            participants.put(participantId, new ParticipantState(info.connection, info.audioTrack, true));
        }
    }

    public void participantDisconnected(String callId, int participantId) {
        if (checkCallId(callId)) {
            debug(String.format("Participant disconnected %s", participantId));
            if (currentState==STATE_RINGING && isOutgoing) {
                stopRinging();
                playingSound = true;
                soundPool.play(spBusyId, 1, 1, 0, -1, 1);
                AndroidUtilities.runOnUIThread(afterSoundRunnable, 1500);
                onConnectionStateChanged(STATE_BUSY);
                TLRPC.TL_call_disconnect req = createDisconnectRequest(DISCARD_REASON_MISSED);
                ConnectionsManager.getInstance(currentAccount).sendRequest(req, (response, error) -> {
                    if (error != null) {
                        if (BuildVars.LOGS_ENABLED) {
                            logError("TL_call_disconnect", error);
                        }
                    } else {
                        if (BuildVars.LOGS_ENABLED) {
                            debug("TL_call_disconnect " + response);
                        }
                    }
                }, ConnectionsManager.RequestFlagFailOnServerErrors);
                stopSelf();
            } else {
                hangUp();
            }
        }
    }

    public void discardParticipant(String callId) {
        if (checkCallId(callId)) {
            debug(String.format("Discarding this participant"));
            callEnded();
        }
    }

    public void sdpReceived(TLRPC.TL_call_sdp sdp) {
        if (checkCallId(sdp.call_id)) {
            debug(String.format("Received sdp from participant %d: %s", sdp.participant_id, sdp.sdp));
            ParticipantState state = participants.get(sdp.participant_id);
            if (state == null) {
                debug("There is no state for participant " + sdp.participant_id + ", creating new state");
                PeerConnectionInfo info = createPeerConnection(sdp.participant_id);
                state = new ParticipantState(info.connection, info.audioTrack, false);
                participants.put(sdp.participant_id, state);
                createAnswer(sdp, info.connection);
            } else {
                if (!state.isInitiator()) {
                    debug("State already has been created for participant");
                    createAnswer(sdp, state.getConnection());
                } else {
                    SessionDescription desc = new SessionDescription(SessionDescription.Type.ANSWER, sdp.sdp);
                    state.getConnection().setRemoteDescription(new SdpObserverAdapter() {
                        @Override
                        public void onSetSuccess() {
                            debug("SDP answer has been successfully applied");
                        }
                        @Override
                        public void onSetFailure(String s) {
                            logError("SDP answer set failure: " + s);
                        }
                    }, desc);
                }
            }
        }
    }

    public synchronized void iceCandidateReceived(TLRPC.TL_call_iceCandidate candidate) {
        if (checkCallId(candidate.call_id)) {
            ParticipantState state = participants.get(candidate.participant_id);
            if (state != null) {
                debug("Adding IceCandidate from participant " + candidate.participant_id);
                IceCandidate iceCandidate = new IceCandidate(candidate.sdpMId, candidate.sdpMLineIndex, candidate.candidate);
                state.getConnection().addIceCandidate(iceCandidate);
            } else {
                logError("Received IceCandidate for unknown participant " + candidate.participant_id);
            }
        }
    }

    private synchronized void createPeerConnectionFactory() {
        if (connectionFactory == null) {
            PeerConnectionFactory.InitializationOptions options = PeerConnectionFactory.InitializationOptions
                    .builder(ApplicationLoader.applicationContext)
                    .createInitializationOptions();
            PeerConnectionFactory.initialize(options);
            PeerConnectionFactory.Options factoryOptions = new PeerConnectionFactory.Options();
            PeerConnectionFactory factory = PeerConnectionFactory.builder().createPeerConnectionFactory();
            connectionFactory = factory;
        }
    }

    private PeerConnectionInfo createPeerConnection(final int participantId) {
        PeerConnection.RTCConfiguration config = new PeerConnection.RTCConfiguration(relays);
        PeerConnection conn = connectionFactory.createPeerConnection(config, new PeerConnection.Observer() {
            @Override
            public void onSignalingChange(PeerConnection.SignalingState signalingState) {
                debug("NEW Signaling state: " + signalingState);
            }

            @Override
            public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
                debug("NEW IceConnection state: " + iceConnectionState);
//                handleIceConnectionState(iceConnectionState);
                handleIceConnectionState(iceConnectionState, participantId);

            }

            @Override
            public void onIceConnectionReceivingChange(boolean b) {
                debug("onIceConnectionReceivingChange" + b);
            }

            @Override
            public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
                debug("onIceGatheringChange: " + iceGatheringState);
            }

            @Override
            public void onIceCandidate(IceCandidate iceCandidate) {
                debug("onIceCandidate: "+iceCandidate);
                sendIceCandidate(participantId, iceCandidate);
            }

            @Override
            public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {
                debug("onIceCandidatesRemoved: " + Arrays.toString(iceCandidates));
            }

            @Override
            public void onAddStream(MediaStream mediaStream) {
                debug("onAddStream: " + mediaStream);
            }

            @Override
            public void onRemoveStream(MediaStream mediaStream) {
                debug("onRemoveStream: " + mediaStream);
            }

            @Override
            public void onDataChannel(DataChannel dataChannel) {
                debug("onDataChannel: " + dataChannel);
            }

            @Override
            public void onRenegotiationNeeded() {
                debug("onRenegotiationNeeded");
            }

            @Override
            public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {
                debug("onAddTrack: rtpReceiver=" + rtpReceiver + " mediaStreams=" + Arrays.toString(mediaStreams));
            }
        });
        if (conn != null) {
            MediaStream stream = connectionFactory.createLocalMediaStream("" + participantId);
            debug("PeerConn: " + conn);
            debug("Local stream: " + conn);
            conn.addStream(stream);
            AudioSource audioSource = connectionFactory.createAudioSource(new MediaConstraints());
            AudioTrack track = connectionFactory.createAudioTrack("" + participantId, audioSource);
            stream.addTrack(track);
            return new PeerConnectionInfo(conn, track);
        } else {
            logError("Can't create peer connection for participant " + participantId);
            callFailed(); //TODO tim add error code
            return null;
        }

    }

    private void handleIceConnectionState(PeerConnection.IceConnectionState iceConnectionState, int participantId) {
        switch (iceConnectionState) {
            case CONNECTED:
                if (timeoutRunnable != null) {
                    AndroidUtilities.cancelRunOnUIThread(timeoutRunnable);
                    timeoutRunnable = null;
                }
                AndroidUtilities.runOnUIThread(() -> {
                    callStartTime = System.currentTimeMillis();
                    onConnectionStateChanged(STATE_ESTABLISHED);
                    Log.e("call duration:", "duration initailize " + callStartTime);
                    TLRPC.TL_call_connected req = new TLRPC.TL_call_connected();
                    req.call_id = callId;
                    req.participant_id = participantId;
                    sendRequest(req, (response, error) -> {
                        if (error != null) {
                            logError("TL_call_connected", error);
                            callFailed();
                        } else {
                            debug("TL_call_connected successfully sent");
                        }
                    });

                });
                break;
            case DISCONNECTED:
                debug("Ice connection state has been changed on DISCONNECTED");
//                AndroidUtilities.runOnUIThread(() -> callEnded());
                break;
            case FAILED:
                logError("Ice connection failed");
                AndroidUtilities.runOnUIThread(() -> declineIncomingCall(DISCARD_REASON_DISCONNECT, null));
                break;

        }
    }

    private void ringbackCallProc() {
//        if (currentState == STATE_WAITING) {
        dispatchStateChanged(STATE_RINGING);
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("!!!!!! CALL RECEIVED");
        }
        Log.e("","Call request ringing.");
        if (spPlayID != 0)
            soundPool.stop(spPlayID);
        spPlayID = soundPool.play(spRingbackID, 1, 1, 0, -1, 1);
        if (timeoutRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(timeoutRunnable);
            timeoutRunnable = null;
        }
        timeoutRunnable = new Runnable() {
            @Override
            public void run() {
                debug("Executing RINGING timeout timer");
                timeoutRunnable = null;
                declineIncomingCall(DISCARD_REASON_MISSED, null);
            }
        };
        AndroidUtilities.runOnUIThread(timeoutRunnable, MessagesController.getInstance(currentAccount).callRingTimeout);
//        }
    }

    @Override
    public void setMicMute(boolean mute) {
        debug("setMicMute: " + mute);
        this.micMute = true;
        for (ParticipantState state: participants.values()) {
            state.getLocalAudioTrack().setEnabled(mute);
        }
    }

    @Override
    public boolean isMicMute() {
        return micMute;
    }

    @Override
    public long getCallDuration() {
        long res = System.currentTimeMillis() - callStartTime;
        Log.e("getCallDuration:", "  " + callStartTime);
        return res > 0 ? res : 0;
    }

    private void createOffer(final int participantId, final PeerConnection connection) {
        debug("Creating offer for participant " + participantId);
        MediaConstraints sdpConstraints = new MediaConstraints();
        connection.createOffer(new SdpObserver() {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                debug("Generated local SDP: " + sessionDescription);
                connection.setLocalDescription(new SdpObserverAdapter(), sessionDescription);
                sendSdp(participantId, sessionDescription);
            }

            @Override
            public void onSetSuccess() {
                Log.e("Set SDP", "Success");
            }

            @Override
            public void onCreateFailure(String s) {
                Log.e("Generate SDP", "Failed:" + s);
            }

            @Override
            public void onSetFailure(String s) {
                Log.e("Set SDP", "Failed" + s);
            }
        }, sdpConstraints);
    }

    private void createAnswer(final TLRPC.TL_call_sdp sdp, final PeerConnection connection) {
        debug("Creating answer on offer from participant " + sdp.participant_id);
        MediaConstraints sdpConstraints = new MediaConstraints();
        SessionDescription desc = new SessionDescription(SessionDescription.Type.OFFER, sdp.sdp);
        connection.setRemoteDescription(new SdpObserverAdapter() {
            @Override
            public void onSetSuccess() {
                debug("Remote SDP successfully applied for participant: " + sdp.participant_id);
            }

            @Override
            public void onSetFailure(String s) {
                logError(String.format("Remote SDP applying failure for participant %d: %s", sdp.participant_id, s));
                callFailed();
            }
        }, desc);
        connection.createAnswer(new SdpObserverAdapter() {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                sendSdp(sdp.participant_id, sessionDescription);
                connection.setLocalDescription(new SdpObserverAdapter(), sessionDescription);
            }
        }, sdpConstraints);
    }

    private void sendIceCandidate(int participantId, IceCandidate iceCandidate) {
//        TLRPC.TL_call_sendIceCandidate req = new TLRPC.TL_call_sendIceCandidate();
        TLRPC.TL_call_sendSignalingMessage req = new TLRPC.TL_call_sendSignalingMessage();
        TLRPC.TL_call_iceCandidate candidate = new TLRPC.TL_call_iceCandidate();
        candidate.call_id = callId;
        candidate.participant_id = participantId;
        candidate.candidate = iceCandidate.sdp;
        candidate.sdpMId = iceCandidate.sdpMid;
        candidate.sdpMLineIndex = iceCandidate.sdpMLineIndex;
        candidate.userNameFragment = "";
//        req.candidate = candidate;
        req.message = candidate;
        debug(String.format("Sending ice candidate to participant %d: %s", participantId, candidate.candidate));
        sendRequest(req, (response, error) -> {
            if (error != null) {
                logError("TL_call_sendIceCandidate", error);
                callFailed();
            } else {
                debug("Candidate successfully has been sent");
            }
        });
    }

    private void sendSdp(final int participantId, final SessionDescription sessionDescription) {
        TLRPC.TL_call_sdp sdp = new TLRPC.TL_call_sdp();
        sdp.call_id = callId;
        sdp.participant_id = participantId;
        sdp.sdp = sessionDescription.description;
//        TLRPC.TL_call_sendSdp req = new TLRPC.TL_call_sendSdp();
//        req.sdp = sdp;
        TLRPC.TL_call_sendSignalingMessage req = new TLRPC.TL_call_sendSignalingMessage();
        req.message = sdp;
        debug(String.format("sending SDP to %d: %s", participantId, sessionDescription.description));
        sendRequest(req, (response, error) -> {
            if (error != null) {
                logError("TL_call_sendSdp", error);
                callFailed();
            } else {
                debug("SDP  has been successfully sent to participant " + participantId);
            }
        });
    }

    private boolean checkCallId(String testingCallId) {
        if (this.callId == null || !this.callId.equals(testingCallId)) {
            FileLog.w(String.format("Invalid call_id, received %s but expected %s", testingCallId, callId));
            return false;
        } else {
            return true;
        }
    }

    private void startIncomingCall() {
        debug("Starting incoming call");
        setCallDescription(incomingCall);
        dispatchStateChanged(STATE_WAITING);
        AndroidUtilities.runOnUIThread(() -> {
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didStartedCall);
        });
        startRinging();
    }

    private void joinToCall() {
        TLRPC.TL_call_join joinReq = new TLRPC.TL_call_join();
        joinReq.call_id = callId;
        sendRequest(joinReq, (response, error) -> {
            if (error != null) {
                logError("TL_call_join", error);
                callFailed();
            } else {
                debug("Received successful response on join!");
            }
        });
    }

    private void logError(String request, TLRPC.TL_error error) {
        FileLog.e(String.format(
                "[CallService] Request %s failed: error_code=%d; text=%s",
                request,
                error.code,
                error.text));
    }

    private void logError(String message) {
        FileLog.e(String.format("[CallService] %s", message));
    }

    private void logError(String message, Throwable cause) {
        FileLog.e(String.format("[CallService] %s", message), cause);
    }

    private void startOutgoingCall() {
        if(USE_CONNECTION_SERVICE && systemCallConnection!=null)
            systemCallConnection.setDialing();
        debug("Starting outgoing call");
        configureDeviceForCall();
        showNotification();
        startConnectingSound();
        dispatchStateChanged(STATE_REQUESTING);
        AndroidUtilities.runOnUIThread(new Runnable(){
            @Override
            public void run(){
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didStartedCall);
            }
        });

        TLRPC.TL_call_initiate createCallReq = new TLRPC.TL_call_initiate();
        createCallReq.title = "Phone call";
        debug("sending 'initiate' request");
        int msgId = ConnectionsManager.getInstance(currentAccount).sendRequest(createCallReq, (response, error) -> {
            if (error != null) {
                logError("TL_call_initiate", error);
                callFailed();
            } else {
                debug("Received response on initiate request!");
                TLRPC.TL_call_callDescription dsc = (TLRPC.TL_call_callDescription) response;
                debug(
                        String.format(
                                "call_id=%s; initiator_id=%d, title=%s, creation_time=%d, participants:%s, relays:%s",
                                dsc.id,
                                dsc.initiator_id,
                                dsc.title,
                                dsc.creation_time,
                                dsc.participants.toString(),
                                dsc.relays.toString()));
                setCallDescription(dsc);

                TLRPC.TL_call_invite inviteReq = new TLRPC.TL_call_invite();
                inviteReq.input_user = MessagesController.getInstance(currentAccount).getInputUser(user);
                inviteReq.call_id = callId;
                sendRequest(inviteReq, new RequestDelegate() {
                    @Override
                    public void run(TLObject response, TLRPC.TL_error error) {
                        if (error != null) {
                            logError("TL_call_invite", error);
                            callFailed(); //errorCode
                        } else {
                            ringbackCallProc();
                            debug("Participant invited");
                            dispatchStateChanged(STATE_RINGING);
                        }
                    }
                });
            }
        });
    }

    private void scheduleInviteTimeout() {
        timeoutRunnable = () -> {
            timeoutRunnable = null;
            TLRPC.TL_call_disconnect req = createDisconnectRequest(DISCARD_REASON_MISSED);
            ConnectionsManager.getInstance(currentAccount).sendRequest(req, (response, error) -> {
                if (BuildVars.LOGS_ENABLED) {
                    if (error != null) {
                        logError("TL_call_disconnect", error);
                    } else {
                        debug("TL_call_disconnect " + response);
                    }
                }
                AndroidUtilities.runOnUIThread(() -> callFailed());
            }, ConnectionsManager.RequestFlagFailOnServerErrors);
        };
        AndroidUtilities.runOnUIThread(
                timeoutRunnable,
                MessagesController.getInstance(currentAccount).callReceiveTimeout);
    }

    private int sendRequest(TLObject request, RequestDelegate callback) {
        return ConnectionsManager.getInstance(currentAccount).sendRequest(request, callback);
    }

    private void setCallDescription(TLRPC.TL_call_callDescription dsc) {
        callId = dsc.id;
        title = dsc.title;
        relays = new ArrayList(dsc.relays.size());
        for (TLRPC.TL_call_iceServer server: dsc.relays) {
            debug("adding relay: " + server.uri);
            PeerConnection.IceServer.Builder builder = PeerConnection.IceServer.builder(server.uri);
            if (!server.username.isEmpty() && !server.password.isEmpty()) {
                builder.setUsername(server.username).setPassword(server.password);
            }
            relays.add(builder.createIceServer());
        }
    }

    private void startConnectingSound() {
        if (spPlayID != 0)
            soundPool.stop(spPlayID);
        spPlayID = soundPool.play(spConnectingId, 1, 1, 0, -1, 1);
        if (spPlayID == 0) {
            AndroidUtilities.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (sharedInstance == null)
                        return;
                    if (spPlayID == 0)
                        spPlayID = soundPool.play(spConnectingId, 1, 1, 0, -1, 1);
                    if (spPlayID == 0)
                        AndroidUtilities.runOnUIThread(this, 100);
                }
            }, 100);
        }
    }

    @Override
    public void hangUp() {
        debug("Hangup");
        int reason = currentState == STATE_RINGING || (currentState==STATE_WAITING && isOutgoing) ?
                DISCARD_REASON_MISSED : DISCARD_REASON_HANGUP;
        declineIncomingCall(reason, null);
    }

    @Override
    public void hangUp(Runnable onDone) {
        debug("Hangup, onDone: " + onDone);
        int reason = currentState == STATE_RINGING || (currentState==STATE_WAITING && isOutgoing) ?
                DISCARD_REASON_MISSED : DISCARD_REASON_HANGUP;
        declineIncomingCall(reason , onDone);
    }

    @Override
    public void acceptIncomingCall() {
        //send join
        debug("Accepting incoming call");
        stopRinging();
        showNotification();
        configureDeviceForCall();
        startConnectingSound();
        AndroidUtilities.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didStartedCall);
            }
        });

        onConnectionStateChanged(STATE_CONNECTING);

        joinToCall();
    }

    @Override
    protected Class<? extends Activity> getUIActivityClass() {
        return VoIPActivity.class;
    }

    @Override
    public void declineIncomingCall() {
        declineIncomingCall(DISCARD_REASON_HANGUP, null);
    }

    @Override
    protected void callEnded() {
        if (BuildVars.LOGS_ENABLED) {
            debug(String.format("Call %s ended", callId));
        }
        for (Map.Entry<Integer, ParticipantState> entry: participants.entrySet()) {
            if (BuildVars.LOGS_ENABLED) {
                debug("Closing peer connection for participant " + entry.getKey());
                try {
                    entry.getValue().getConnection().close();
                } catch (Throwable e) {
                    logError("Error closing peer connection for participant " + entry.getKey(), e);
                }
            }
        }
        super.callEnded();
    }

    @Override
    public void declineIncomingCall(int reason, Runnable onDone) {
        debug("declineIncomingCall: reason = " + reason + "; currentState = " + currentState);
        stopRinging();
        callDiscardReason=reason;
        if (currentState == STATE_REQUESTING) {

            if (delayedOutgoingCallTask != null) {
                AndroidUtilities.cancelRunOnUIThread(delayedOutgoingCallTask);
                callEnded();
            } else {
                dispatchStateChanged(STATE_HANGING_UP);
//                endCallAfterRequest = true;
            }
            return;
        }
        if (currentState == STATE_HANGING_UP || currentState == STATE_ENDED)
            return;
        dispatchStateChanged(STATE_HANGING_UP);
        if (callId == null) {
            if (onDone != null)
                onDone.run();
            callEnded();
            return;
        }
        TLRPC.TL_call_disconnect req = createDisconnectRequest(reason);
        final boolean wasNotConnected = ConnectionsManager.getInstance(currentAccount).getConnectionState() != ConnectionsManager.ConnectionStateConnected;
        final Runnable stopper;
        if (wasNotConnected) {
            if (onDone != null)
                onDone.run();
            callEnded();
            stopper = null;
        } else {
            stopper = new Runnable() {
                private boolean done = false;

                @Override
                public void run() {
                    debug("Executing stopper");
                    if (done) {
                        return;
                    }
                    done = true;
                    if(onDone != null)
                        onDone.run();
                    callEnded();
                }
            };
            AndroidUtilities.runOnUIThread(
                    stopper,
                    30000); //get timeout from config as below
        }

        ConnectionsManager.getInstance(currentAccount).sendRequest(req, (response, error) -> {
            if (error != null) {
                if (BuildVars.LOGS_ENABLED) {
                    logError("TL_call_disconnect", error);
                }
            } else {
                if (BuildVars.LOGS_ENABLED) {
                    debug("TL_call_disconnect " + response);
                }
            }
            if (!wasNotConnected) {
                AndroidUtilities.cancelRunOnUIThread(stopper);
                if(onDone!=null)
                    onDone.run();
                callEnded();
            }
        }, ConnectionsManager.RequestFlagFailOnServerErrors);
    }

    private TLRPC.TL_call_disconnect createDisconnectRequest(int reason) {
        TLRPC.TL_call_disconnect req = new TLRPC.TL_call_disconnect();
        req.call_id = callId;
        switch (reason) {
//            case DISCARD_REASON_DISCONNECT:
//                req.reason = new TLRPC.TL_phoneCallDiscardReasonDisconnect();
//                break;
//            case DISCARD_REASON_LINE_BUSY:
//                req.reason = new TLRPC.TL_phoneCallDiscardReasonBusy();
//                break;
            case DISCARD_REASON_MISSED:
                req.reason = new TLRPC.TL_call_disconnectReasonMissed();
                break;
            default:
                req.reason = new TLRPC.TL_call_disconnectReasonHangUp();
                break;
        }
        return req;
    }

    @Override
    public CallConnection getConnectionAndStartCall() {
        return null;
    }

    protected void startRinging() {
        if(currentState==STATE_WAITING_INCOMING){
            return;
        }
        if(USE_CONNECTION_SERVICE && systemCallConnection != null)
            systemCallConnection.setRinging();
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("starting ringing for call " + callId);
        }
        dispatchStateChanged(STATE_WAITING_INCOMING);
        startRingtoneAndVibration(user.id);
        if (   Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && !((KeyguardManager) getSystemService(KEYGUARD_SERVICE)).inKeyguardRestrictedInputMode()
                && NotificationManagerCompat.from(this).areNotificationsEnabled())
        {
            showIncomingNotification(
                    ContactsController.formatName(user.first_name, user.last_name),
                    null,
                    user,
                    null,
                    0,
                    VoIPActivity.class);
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("Showing incoming call notification");
            }
        } else {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("Starting incall activity for incoming call");
            }
            try {
                PendingIntent.getActivity(
                        CallService.this,
                        12345,
                        new Intent(CallService.this, VoIPActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0).send();
            } catch (Exception x) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("Error starting incall activity", x);
                }
            }
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                showNotification();
            }
        }
    }

    @Override
    public void onDestroy() {
        debug("Destroying...");
        sharedInstance = null;
        super.onDestroy();
    }

    @Override
    protected void updateServerConfig() {

    }

    @Override
    protected void showNotification() {
        showNotification(
                ContactsController.formatName(user.first_name, user.last_name),
                user.photo!=null ? user.photo.photo_small : null,
                VoIPActivity.class);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onGroupCallKeyReceived(byte[] key) {

    }

    @Override
    public void onGroupCallKeySent() {

    }

    @Override
    public void onCallUpgradeRequestReceived() {

    }

    public void onUIForegroundStateChanged(boolean isForeground) {
        if (currentState == STATE_WAITING_INCOMING) {
            if (isForeground) {
                stopForeground(true);
            } else {
                if (!((KeyguardManager) getSystemService(KEYGUARD_SERVICE)).inKeyguardRestrictedInputMode()) {
                    if(NotificationManagerCompat.from(this).areNotificationsEnabled())
                        showIncomingNotification(ContactsController.formatName(user.first_name, user.last_name), null, user, null, 0, VoIPActivity.class);
                    else
                        declineIncomingCall(DISCARD_REASON_LINE_BUSY, null);
                } else {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(CallService.this, VoIPActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            try {
                                PendingIntent.getActivity(CallService.this, 0, intent, 0).send();
                            } catch (PendingIntent.CanceledException e) {
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.e("error restarting activity", e);
                                }
                                declineIncomingCall(DISCARD_REASON_LINE_BUSY, null);
                            }
                            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                                showNotification();
                            }
                        }
                    }, 500);
                }
            }
        }
    }

    private static class PeerConnectionInfo {
        public final PeerConnection connection;
        public final AudioTrack audioTrack;

        public PeerConnectionInfo(PeerConnection connection, AudioTrack audioTrack) {
            this.connection = connection;
            this.audioTrack = audioTrack;
        }
    }
}
