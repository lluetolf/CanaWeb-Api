package ch.canaweb.api.persistence;

import org.springframework.stereotype.Service;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

@Service
public class FireStoreService {
    private static final String projectId = "dev-canaweb-firestore";
    private static Firestore firestore;

    public static Firestore getFirestore() {
        if (firestore == null) {
            Firestore db =
                    FirestoreOptions.newBuilder().setProjectId(FireStoreService.projectId)
                            .build().getService();
            firestore = db;
        }

        return firestore;
    }


}
