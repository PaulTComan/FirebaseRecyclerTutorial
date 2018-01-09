package radautiul_civic.firebaserecyclertutorial.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class Utils {
    public static String getUID()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.POSTS).push();
        String[]urlArray = databaseReference.toString().split("/");
        return urlArray[urlArray.length - 1];
    }
}
