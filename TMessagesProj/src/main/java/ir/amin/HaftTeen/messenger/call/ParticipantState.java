package ir.amin.HaftTeen.messenger.call;

import org.webrtc.AudioTrack;
import org.webrtc.PeerConnection;

public class ParticipantState {
    public enum Status {
        CONNECTING,
        CONNECTED
    }

    private Status status = Status.CONNECTING;
    private final long creationTime = System.currentTimeMillis();
    private final PeerConnection connection;
    private final AudioTrack localAudioTrack;
    private final boolean initiator;

    public ParticipantState(PeerConnection connection, AudioTrack localAudioTrack, boolean initiator) {
        this.connection = connection;
        this.initiator = initiator;
        this.localAudioTrack = localAudioTrack;
    }

    public Status getStatus() {
            return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public PeerConnection getConnection() {
        return connection;
    }

    public AudioTrack getLocalAudioTrack() {
        return localAudioTrack;
    }

    public boolean isInitiator() {
        return initiator;
    }
}
