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
package com.google.android.exoplayer2.decoder;

import android.annotation.TargetApi;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.util.Util;

/**
 * Compatibility wrapper for {@link android.media.MediaCodec.CryptoInfo}.
 */
public final class CryptoInfo {

    private final android.media.MediaCodec.CryptoInfo frameworkCryptoInfo;
    private final PatternHolderV24 patternHolder;
    /**
     * @see android.media.MediaCodec.CryptoInfo#iv
     */
    public byte[] iv;
    /**
     * @see android.media.MediaCodec.CryptoInfo#key
     */
    public byte[] key;
    /**
     * @see android.media.MediaCodec.CryptoInfo#mode
     */
    @C.CryptoMode
    public int mode;
    /**
     * @see android.media.MediaCodec.CryptoInfo#numBytesOfClearData
     */
    public int[] numBytesOfClearData;
    /**
     * @see android.media.MediaCodec.CryptoInfo#numBytesOfEncryptedData
     */
    public int[] numBytesOfEncryptedData;
    /**
     * @see android.media.MediaCodec.CryptoInfo#numSubSamples
     */
    public int numSubSamples;
    /**
     * @see android.media.MediaCodec.CryptoInfo.Pattern
     */
    public int encryptedBlocks;
    /**
     * @see android.media.MediaCodec.CryptoInfo.Pattern
     */
    public int clearBlocks;

    public CryptoInfo() {
        frameworkCryptoInfo = Util.SDK_INT >= 16 ? newFrameworkCryptoInfoV16() : null;
        patternHolder = Util.SDK_INT >= 24 ? new PatternHolderV24(frameworkCryptoInfo) : null;
    }

    /**
     * @see android.media.MediaCodec.CryptoInfo#set(int, int[], int[], byte[], byte[], int)
     */
    public void set(int numSubSamples, int[] numBytesOfClearData, int[] numBytesOfEncryptedData,
                    byte[] key, byte[] iv, @C.CryptoMode int mode, int encryptedBlocks, int clearBlocks) {
        this.numSubSamples = numSubSamples;
        this.numBytesOfClearData = numBytesOfClearData;
        this.numBytesOfEncryptedData = numBytesOfEncryptedData;
        this.key = key;
        this.iv = iv;
        this.mode = mode;
        this.encryptedBlocks = encryptedBlocks;
        this.clearBlocks = clearBlocks;
        if (Util.SDK_INT >= 16) {
            updateFrameworkCryptoInfoV16();
        }
    }

    /**
     * Returns an equivalent {@link android.media.MediaCodec.CryptoInfo} instance.
     * <p>
     * Successive calls to this method on a single {@link CryptoInfo} will return the same instance.
     * Changes to the {@link CryptoInfo} will be reflected in the returned object. The return object
     * should not be modified directly.
     *
     * @return The equivalent {@link android.media.MediaCodec.CryptoInfo} instance.
     */
    @TargetApi(16)
    public android.media.MediaCodec.CryptoInfo getFrameworkCryptoInfoV16() {
        return frameworkCryptoInfo;
    }

    @TargetApi(16)
    private android.media.MediaCodec.CryptoInfo newFrameworkCryptoInfoV16() {
        return new android.media.MediaCodec.CryptoInfo();
    }

    @TargetApi(16)
    private void updateFrameworkCryptoInfoV16() {
        // Update fields directly because the framework's CryptoInfo.set performs an unnecessary object
        // allocation on Android N.
        frameworkCryptoInfo.numSubSamples = numSubSamples;
        frameworkCryptoInfo.numBytesOfClearData = numBytesOfClearData;
        frameworkCryptoInfo.numBytesOfEncryptedData = numBytesOfEncryptedData;
        frameworkCryptoInfo.key = key;
        frameworkCryptoInfo.iv = iv;
        frameworkCryptoInfo.mode = mode;
        if (Util.SDK_INT >= 24) {
            patternHolder.set(encryptedBlocks, clearBlocks);
        }
    }

    @TargetApi(24)
    private static final class PatternHolderV24 {

        private final android.media.MediaCodec.CryptoInfo frameworkCryptoInfo;
        private final android.media.MediaCodec.CryptoInfo.Pattern pattern;

        private PatternHolderV24(android.media.MediaCodec.CryptoInfo frameworkCryptoInfo) {
            this.frameworkCryptoInfo = frameworkCryptoInfo;
            pattern = new android.media.MediaCodec.CryptoInfo.Pattern(0, 0);
        }

        private void set(int encryptedBlocks, int clearBlocks) {
            pattern.set(encryptedBlocks, clearBlocks);
            frameworkCryptoInfo.setPattern(pattern);
        }

    }

}
