package ir.amin.HaftTeen.vasni.utils;

import android.os.AsyncTask;

import java.net.URL;

import io.tus.java.client.TusClient;
import io.tus.java.client.TusUpload;
import io.tus.java.client.TusUploader;

public class UploadTask extends AsyncTask<Void, Long, URL> {
    private TusClient client;
    private TusUpload upload;
    private Exception exception;
    private Result result;

    public UploadTask(TusClient client, TusUpload upload) {
        this.client = client;
        this.upload = upload;
    }

    public UploadTask getResult(Result result) {
        this.result = result;
        return this;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(URL uploadURL) {
//        String file = uploadURL.toString().substring(uploadURL.toString().lastIndexOf("/"));
//        Log.e("upload", "Upload finished!\n" + uploadURL.toString() + "\nfile!\n" + file);
        result.onProgressFinished(uploadURL);
    }

    @Override
    protected void onCancelled() {

    }

    @Override
    protected void onProgressUpdate(Long... updates) {
        long uploadedBytes = updates[0];
        long totalBytes = updates[1];
        result.onProgressUpdate((int) ((double) uploadedBytes / totalBytes * 100));
    }

    @Override
    protected URL doInBackground(Void... params) {
        try {
            TusUploader uploader = client.resumeOrCreateUpload(upload);
            long totalBytes = upload.getSize();
            long uploadedBytes = uploader.getOffset();

            // Upload file in 1MiB chunks
            uploader.setChunkSize(1024 * 1024);

            while (!isCancelled() && uploader.uploadChunk() > 0) {
                uploadedBytes = uploader.getOffset();
                publishProgress(uploadedBytes, totalBytes);
            }

            uploader.finish();
            return uploader.getUploadURL();

        } catch (Exception e) {
            exception = e;
            result.onProgressFailed(e.getMessage());
            cancel(true);
        }
        return null;
    }

    public interface Result {
        public void onProgressUpdate(int percent);

        public void onProgressFinished(URL uploadURL);

        public void onProgressFailed(String error);
    }
}
