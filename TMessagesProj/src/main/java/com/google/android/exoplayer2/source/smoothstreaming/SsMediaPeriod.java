/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.exoplayer2.source.smoothstreaming;

import android.support.annotation.Nullable;
import android.util.Base64;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.extractor.mp4.TrackEncryptionBox;
import com.google.android.exoplayer2.source.CompositeSequenceableLoaderFactory;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSourceEventListener.EventDispatcher;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.source.SequenceableLoader;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.chunk.ChunkSampleStream;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifest;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifest.ProtectionElement;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import com.google.android.exoplayer2.upstream.TransferListener;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A SmoothStreaming {@link MediaPeriod}.
 */
/* package */ final class SsMediaPeriod implements MediaPeriod,
        SequenceableLoader.Callback<ChunkSampleStream<SsChunkSource>> {

    private static final int INITIALIZATION_VECTOR_SIZE = 8;

    private final SsChunkSource.Factory chunkSourceFactory;
    private final @Nullable
    TransferListener transferListener;
    private final LoaderErrorThrower manifestLoaderErrorThrower;
    private final int minLoadableRetryCount;
    private final EventDispatcher eventDispatcher;
    private final Allocator allocator;
    private final TrackGroupArray trackGroups;
    private final TrackEncryptionBox[] trackEncryptionBoxes;
    private final CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory;

    private @Nullable
    Callback callback;
    private SsManifest manifest;
    private ChunkSampleStream<SsChunkSource>[] sampleStreams;
    private SequenceableLoader compositeSequenceableLoader;
    private boolean notifiedReadingStarted;

    public SsMediaPeriod(
            SsManifest manifest,
            SsChunkSource.Factory chunkSourceFactory,
            @Nullable TransferListener transferListener,
            CompositeSequenceableLoaderFactory compositeSequenceableLoaderFactory,
            int minLoadableRetryCount,
            EventDispatcher eventDispatcher,
            LoaderErrorThrower manifestLoaderErrorThrower,
            Allocator allocator) {
        this.chunkSourceFactory = chunkSourceFactory;
        this.transferListener = transferListener;
        this.manifestLoaderErrorThrower = manifestLoaderErrorThrower;
        this.minLoadableRetryCount = minLoadableRetryCount;
        this.eventDispatcher = eventDispatcher;
        this.allocator = allocator;
        this.compositeSequenceableLoaderFactory = compositeSequenceableLoaderFactory;

        trackGroups = buildTrackGroups(manifest);
        ProtectionElement protectionElement = manifest.protectionElement;
        if (protectionElement != null) {
            byte[] keyId = getProtectionElementKeyId(protectionElement.data);
            // We assume pattern encryption does not apply.
            trackEncryptionBoxes = new TrackEncryptionBox[]{
                    new TrackEncryptionBox(true, null, INITIALIZATION_VECTOR_SIZE, keyId, 0, 0, null)};
        } else {
            trackEncryptionBoxes = null;
        }
        this.manifest = manifest;
        sampleStreams = newSampleStreamArray(0);
        compositeSequenceableLoader =
                compositeSequenceableLoaderFactory.createCompositeSequenceableLoader(sampleStreams);
        eventDispatcher.mediaPeriodCreated();
    }

    private static TrackGroupArray buildTrackGroups(SsManifest manifest) {
        TrackGroup[] trackGroups = new TrackGroup[manifest.streamElements.length];
        for (int i = 0; i < manifest.streamElements.length; i++) {
            trackGroups[i] = new TrackGroup(manifest.streamElements[i].formats);
        }
        return new TrackGroupArray(trackGroups);
    }

    @SuppressWarnings("unchecked")
    private static ChunkSampleStream<SsChunkSource>[] newSampleStreamArray(int length) {
        return new ChunkSampleStream[length];
    }

    private static byte[] getProtectionElementKeyId(byte[] initData) {
        StringBuilder initDataStringBuilder = new StringBuilder();
        for (int i = 0; i < initData.length; i += 2) {
            initDataStringBuilder.append((char) initData[i]);
        }
        String initDataString = initDataStringBuilder.toString();
        String keyIdString = initDataString.substring(
                initDataString.indexOf("<KID>") + 5, initDataString.indexOf("</KID>"));
        byte[] keyId = Base64.decode(keyIdString, Base64.DEFAULT);
        swap(keyId, 0, 3);
        swap(keyId, 1, 2);
        swap(keyId, 4, 5);
        swap(keyId, 6, 7);
        return keyId;
    }

    private static void swap(byte[] data, int firstPosition, int secondPosition) {
        byte temp = data[firstPosition];
        data[firstPosition] = data[secondPosition];
        data[secondPosition] = temp;
    }

    public void updateManifest(SsManifest manifest) {
        this.manifest = manifest;
        for (ChunkSampleStream<SsChunkSource> sampleStream : sampleStreams) {
            sampleStream.getChunkSource().updateManifest(manifest);
        }
        callback.onContinueLoadingRequested(this);
    }

    public void release() {
        for (ChunkSampleStream<SsChunkSource> sampleStream : sampleStreams) {
            sampleStream.release();
        }
        callback = null;
        eventDispatcher.mediaPeriodReleased();
    }

    @Override
    public void prepare(Callback callback, long positionUs) {
        this.callback = callback;
        callback.onPrepared(this);
    }

    @Override
    public void maybeThrowPrepareError() throws IOException {
        manifestLoaderErrorThrower.maybeThrowError();
    }

    @Override
    public TrackGroupArray getTrackGroups() {
        return trackGroups;
    }

    @Override
    public long selectTracks(TrackSelection[] selections, boolean[] mayRetainStreamFlags,
                             SampleStream[] streams, boolean[] streamResetFlags, long positionUs) {
        ArrayList<ChunkSampleStream<SsChunkSource>> sampleStreamsList = new ArrayList<>();
        for (int i = 0; i < selections.length; i++) {
            if (streams[i] != null) {
                @SuppressWarnings("unchecked")
                ChunkSampleStream<SsChunkSource> stream = (ChunkSampleStream<SsChunkSource>) streams[i];
                if (selections[i] == null || !mayRetainStreamFlags[i]) {
                    stream.release();
                    streams[i] = null;
                } else {
                    sampleStreamsList.add(stream);
                }
            }
            if (streams[i] == null && selections[i] != null) {
                ChunkSampleStream<SsChunkSource> stream = buildSampleStream(selections[i], positionUs);
                sampleStreamsList.add(stream);
                streams[i] = stream;
                streamResetFlags[i] = true;
            }
        }
        sampleStreams = newSampleStreamArray(sampleStreamsList.size());
        sampleStreamsList.toArray(sampleStreams);
        compositeSequenceableLoader =
                compositeSequenceableLoaderFactory.createCompositeSequenceableLoader(sampleStreams);
        return positionUs;
    }

    @Override
    public void discardBuffer(long positionUs, boolean toKeyframe) {
        for (ChunkSampleStream<SsChunkSource> sampleStream : sampleStreams) {
            sampleStream.discardBuffer(positionUs, toKeyframe);
        }
    }

    @Override
    public void reevaluateBuffer(long positionUs) {
        compositeSequenceableLoader.reevaluateBuffer(positionUs);
    }

    @Override
    public boolean continueLoading(long positionUs) {
        return compositeSequenceableLoader.continueLoading(positionUs);
    }

    @Override
    public long getNextLoadPositionUs() {
        return compositeSequenceableLoader.getNextLoadPositionUs();
    }

    // SequenceableLoader.Callback implementation

    @Override
    public long readDiscontinuity() {
        if (!notifiedReadingStarted) {
            eventDispatcher.readingStarted();
            notifiedReadingStarted = true;
        }
        return C.TIME_UNSET;
    }

    // Private methods.

    @Override
    public long getBufferedPositionUs() {
        return compositeSequenceableLoader.getBufferedPositionUs();
    }

    @Override
    public long seekToUs(long positionUs) {
        for (ChunkSampleStream<SsChunkSource> sampleStream : sampleStreams) {
            sampleStream.seekToUs(positionUs);
        }
        return positionUs;
    }

    @Override
    public long getAdjustedSeekPositionUs(long positionUs, SeekParameters seekParameters) {
        for (ChunkSampleStream<SsChunkSource> sampleStream : sampleStreams) {
            if (sampleStream.primaryTrackType == C.TRACK_TYPE_VIDEO) {
                return sampleStream.getAdjustedSeekPositionUs(positionUs, seekParameters);
            }
        }
        return positionUs;
    }

    @Override
    public void onContinueLoadingRequested(ChunkSampleStream<SsChunkSource> sampleStream) {
        callback.onContinueLoadingRequested(this);
    }

    private ChunkSampleStream<SsChunkSource> buildSampleStream(TrackSelection selection,
                                                               long positionUs) {
        int streamElementIndex = trackGroups.indexOf(selection.getTrackGroup());
        SsChunkSource chunkSource =
                chunkSourceFactory.createChunkSource(
                        manifestLoaderErrorThrower,
                        manifest,
                        streamElementIndex,
                        selection,
                        trackEncryptionBoxes,
                        transferListener);
        return new ChunkSampleStream<>(
                manifest.streamElements[streamElementIndex].type,
                null,
                null,
                chunkSource,
                this,
                allocator,
                positionUs,
                minLoadableRetryCount,
                eventDispatcher);
    }

}
