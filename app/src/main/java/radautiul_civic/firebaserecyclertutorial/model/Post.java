package radautiul_civic.firebaserecyclertutorial.model;

/**
 * Created by Paul! on 09-01-18.
 */

public class Post {
    private String imageUrl;
    private long numLikes;
    private String nameHeadline;
    private String UID;


    public Post(){
    }

    public Post(String imageUrl, long numLikes, String UID, String nameHeadline) {
        this.imageUrl = imageUrl;
        this.numLikes = numLikes;
        this.UID = UID;
        this.nameHeadline = nameHeadline;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(long numLikes) {
        this.numLikes = numLikes;
    }

    public void setHeadline(String nameHeadline) {  //Headline
        this.nameHeadline = nameHeadline;           //Headline
    }


}
