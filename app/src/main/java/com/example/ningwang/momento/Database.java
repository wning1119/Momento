package com.example.ningwang.momento;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;
import com.amazonaws.services.dynamodbv2.model.*;


public class Database {
    private AmazonDynamoDBClient ddbClient;
    private DynamoMapper mapper;

    int postCount;
    int replyCount;

    public Database() {
        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-west-2:e95e6c82-d2d5-4e4f-be55-cced122f7451", // Identity Pool ID
                Regions.US_WEST_2 // Region
        );

        ddbClient = new AmazonDynamoDBClient(credentialsProvider);

        mapper = new DynamoDBMapper(ddbClient);

        postCount = 0;
        replyCount = 0;
    }

    /**
     * write post into database
     * <p>
     *
     * @param post
     */
    public void writePostToDB(Post post) {
        postCount++; //add post count by 1 and use it as post ID

        // set default values
        post.setFavorite(0);
        post.setReplyIds();
        post.setReplies();
        post.setPostId(postCount);
        post.setTimestamp();

        mapper.save(post);
    }

    /**
     * add reply to post
     * <p>
     *
     * @param post
     * @param reply
     */
    public void addReplyToPost(Post post, Reply reply) {
        replyCount++; //add reply count by 1 and use it as reply ID

        // set reply fields
        reply.setId(replyCount);
        reply.setTimestamp();

        mapper.save(reply);

        // update post fields
        Post selectedPost = mapper.load(Post.class, post.getId());
        selectedPost.addReplyId(reply.getId());
        selectedPost.addToReplies(reply);

        mapper.save(selectedPost);
    }

    /**
     * get the current replies of this post
     * <p>
     *
     * @param post
     */
    public List<Reply> getRepliesForPost(Post post){
        List<int> replyIds = post.getReplyIds();
        List<Reply> replies = new ArrayList<>();
        for (int replyId : replyIds) {
            replies.add(mapper.load(Reply.class, replyId));
        }

        return replies;
    }

    /**
     * add the number of favorites of that post by 1
     * <p>
     *
     * @param postId
     */
    public void favoritePost(int postId){
        Post selectedPost = mapper.load(Post.class, postId);
        selectedPost.increaseFavorite();
        mapper.save(selectedPost);
    }

    /**
     * search posts with given category
     * <p>
     *
     * @param category
     * @return List of Post
     */

    public List<Post> searchPostsWithCategory(String category){

    }

    /**
     * search posts with given key word
     * <p>
     *
     * @param keyword
     * @return List of Post
     */

    public List<Post> searchPostsWithKeyword(String keyword){

    }


    /**
     * search posts with given the location range
     * <p>
     *
     * @param lowlatitude
     * @param highlatitude
     * @param lowlongitude
     * @param highlongitude
     * @return List of Post
     */

    public List<Post> searchPostsWithLocation(Double lowlatitude,
                                                     Double highlatitude, Double lowlongitude, Double highlongitude){

    }
}