package br.com.petconnect.boarding.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.BlobInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseStorageService {

    private final Storage storage;

    private final String bucketName = "pet-connect-a349c.appspot.com";

    public FirebaseStorageService() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("src/main/resources/pet-connect-firebase.json"));
        storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }


    public Map<String, String> uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName).build();

        storage.create(blobInfo, file.getBytes());

        Map<String, String> result = new HashMap<>();
        result.put("url", "https://firebasestorage.googleapis.com/v0/b/" + bucketName + "/o/" + fileName + "?alt=media");
        result.put("name", fileName);
        return result;
    }
}