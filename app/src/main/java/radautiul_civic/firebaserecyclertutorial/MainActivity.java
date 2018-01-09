package radautiul_civic.firebaserecyclertutorial;

import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import radautiul_civic.firebaserecyclertutorial.model.Post;
import radautiul_civic.firebaserecyclertutorial.utils.Constants;
import radautiul_civic.firebaserecyclertutorial.utils.Utils;

//https://www.youtube.com/watch?v=TLyF7eBDlqI

public class MainActivity extends AppCompatActivity {

    private RecyclerView mPostRV;
    private FirebaseRecyclerAdapter<Post, PostViewHolder> mPostAdapter;
    private DatabaseReference mPostRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialiseScreen(); //Initiate the screen

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPostToFirebase(); //Add things to the database with a click
            }
        });
    }

    private void sendPostToFirebase() {
        Post post = new Post();
        String UID = Utils.getUID();

        post.setHeadline("Headline"); //Toitle added to the database here
        post.setUID(UID);
        post.setNumLikes(0);
        post.setImageUrl("gs://fir-recyclertutorial.appspot.com/Eric Clapton.jpg");
        mPostRef.child(UID).setValue(post);
    }

    private void initialiseScreen(){
        mPostRV = (RecyclerView)findViewById(R.id.post_rv);
        mPostRV.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mPostRef = FirebaseDatabase.getInstance().getReference(Constants.POSTS);
        setupAdaptater();
        mPostRV.setAdapter(mPostAdapter);
    }

    private void setupAdaptater() {
        mPostAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(
                Post.class,
                R.layout.item_layout_post,
                PostViewHolder.class,
                mPostRef
        ) {
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, final Post model, int position) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(model.getImageUrl());
                Glide.with(MainActivity.this)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .into(viewHolder.postIV);

                viewHolder.setHeadline(model.getHeadline());
                viewHolder.setNumLikes(model.getNumLikes());
                viewHolder.postLikeIV.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                         updateNumLikes(model.getUID());
                    }
                });
            }
        };
    }

    private void updateNumLikes(String uid){
        mPostRef.child(uid).child(Constants.NUM_LIKES)
                .runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        long num = (long) mutableData.getValue();
                        num++;
                        mutableData.setValue(num);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                    }
                });
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder{
        public  ImageView postIV;
        public  ImageView postLikeIV;
        public  TextView numLikesIV;
        public  TextView postHeadline;

        public PostViewHolder(View itemView) {
            super(itemView);
            postIV=(ImageView)itemView.findViewById(R.id.post_iv);
            postLikeIV = (ImageView)itemView.findViewById(R.id.like_iv);
            numLikesIV = (TextView)itemView.findViewById(R.id.num_likes_tv);
            postHeadline = (TextView)itemView.findViewById(R.id.headline_event); //Headline
        }

        public void setPostImage(String url){
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        }

        public void setHeadline(String headline){ //Headline
            postHeadline.setText(String.valueOf(headline)); //Headline
        }

        public void setNumLikes(long num){
            numLikesIV.setText(String.valueOf(num));
        }
    }

}

