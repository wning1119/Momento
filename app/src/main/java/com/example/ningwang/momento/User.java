package com.example.ningwang.momento;

/**
 * Created by ningwang on 11/2/16.
 */

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;

public class User {
    private String id;
    private String name;
    private String email;
    private ArrayList<Post> favorites;
    private ArrayList<Post> posts;

    public User() {
        getUser();
    }

    public User(String uname) {
        setUsername(uname);
        getUser();
    }

    public User(String id, String username, String email) {
        this.id = id;
        this.name = username;
        this.email = email;
    }

    public void setUsername(String uname) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(uname)
//                .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build();

        user.updateProfile(profileUpdates);
        getUser();
    }

    public String getId() {
        getUser();
        return id;
    }

    public String getName() {
        getUser();
        return name;
    }

    public String getEmail() {
        getUser();
        return email;
    }

    public void getUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            id = user.getUid();
            name = user.getDisplayName();
            email = user.getEmail();
//            Uri photoUrl = user.getPhotoUrl();
        }
    }

    public Post createNewPost(String content)
    {
        Post post = new Post();
        post.setDetail(content);
        post.setTimestamp();
        post.setOwner(id);
        posts.add(post);
        return post;
    }

    public void favoritePost(Post post)
    {
        post.increaseFavorite();
        post.increaseTimeout();
        favorites.add(post);
    }

    public Feedback submitFeedback(String content)
    {
        return new Feedback(content);
    }

    public void replyToPost(Post post, String content)
    {
        Reply reply = new Reply(id, content);
        post.addToReplies(reply);
    }

    public ArrayList<Post> getMy_posts()
    {
        return posts;
    }

    public ArrayList<Post> getFavorite_posts()
    {
        return posts;
    }

}
