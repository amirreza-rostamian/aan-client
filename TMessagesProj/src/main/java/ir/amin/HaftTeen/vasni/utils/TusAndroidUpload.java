package ir.amin.HaftTeen.vasni.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import io.tus.java.client.TusUpload;

public class TusAndroidUpload extends TusUpload {
    public TusAndroidUpload(File file) throws FileNotFoundException {
        setSize(file.length());
        setInputStream(new FileInputStream(file));

        setFingerprint(String.format("%s-%d", file.getAbsolutePath(), file.length()));

        String fileType = "video/" + file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1);
        Map<String, String> metadata = new HashMap<>();
        metadata.put("filename", file.getName());
        metadata.put("filetype", fileType);
        setMetadata(metadata);
    }
}
