package ch.canaweb.api.persistence;

import ch.canaweb.api.core.Field.Field;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.common.collect.ImmutableList;
import com.google.cloud.firestore.Query.Direction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static ch.canaweb.api.persistence.FireStoreService.getFirestore;

@Service
public class FieldRepository {

    private static final long TIMEOUT_SECONDS = 5;

    public static final String COLLECTION_NAME = "Field";

    private final CollectionReference collection;

    FieldRepository() {
        this.collection = getFirestore().collection(FieldRepository.COLLECTION_NAME);
    }

    public Field add(Field field) {
        try {
            ApiFuture<DocumentReference> addedFieldRef = collection.add(field);
            addedFieldRef.get();
            return field;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            return  null;
        }
    }

    public List<Field> getAllFields() {
        ImmutableList.Builder<Field> fields = new ImmutableList.Builder<Field>();

        ApiFuture<QuerySnapshot> query =
                this.collection.orderBy("date", Direction.DESCENDING).get();

        try {
            QuerySnapshot querySnapshot = query.get();
            for (QueryDocumentSnapshot field : querySnapshot.getDocuments()) {
                fields.add(field.toObject(Field.class));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return fields.build();
    }

    public Field getField(String id) {
        ApiFuture<QuerySnapshot> query =
                this.collection.whereEqualTo("id", id).get();

        try {
            QuerySnapshot querySnapshot = query.get();
            return querySnapshot.getDocuments().get(0).toObject(Field.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

}
