import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.util.IOUtils;

/**
 * @author Yeziyun
 *
 */

// For the database part, there are 5 tables:
// (1) Post table: postId(key), subject, detail, ..., Image, List<Reply>
// (2) User-mypost relationship table: UserId(key), List<postId>
// (3) User-favoritePost relationship table: UserId(key), List<postId>
// (4) Reply table: replyId(key), ownerId, detail, timestamp
// (5) category-post table: category(key), List<postId>
public class DatabaseAPIs {

    static AmazonDynamoDBClient client;
    static DynamoDB dynamoDB;
    static int postCount=0;
    static int replyCount=0;
    
    private static void init() throws Exception {
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(e);
        }
        client = new AmazonDynamoDBClient(credentials);
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        client.setRegion(usWest2);
        dynamoDB = new DynamoDB(client);
    }

    public static void main(String[] args) throws Exception {
        init();

        try {
            //TSET ONLY!!!
        	String id = "123@ucla.edu";
            String password = "123456";
            writePasswordToDB(id, password);
            String result = getPasswordForUser(id);
            System.out.println("Password: " + result);
            
            
            
        } catch (AmazonServiceException ase) {
            System.out.println(ase.toString());
        } catch (AmazonClientException ace) {
            System.out.println(ace.toString());
        }
    }
        
    /**
     * write post into database
     * <p>
     * 
     * @param post
     */
    public static void writePostToDB(User user, Post post){
    	postCount++; //add post count by 1 and use it as post ID
    	//decode every field in post

    	//write to database with postid
    	
    	//add to post table
    	String postTableName = "Post";
    	Table postTable = dynamoDB.getTable(postTableName);
    	PutItemOutcome outcome = postTable.putItem(new Item()
    			.withPrimaryKey("postId",postCount)
    			.withString("subject", post.getSubject())
    			.withString("detail", post.getDetail())
    			);
        
    	//add to user-mypost table
    	String myPostTableName = "MyPost";
    	Table myPostTable = dynamoDB.getTable(myPostTableName);
    	//firstly query table to get current mypost
    	QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("userId = :id")
                .withValueMap(new ValueMap()
                		.withString(":id", user.getUser_id()));
        ItemCollection<QueryOutcome> items = myPostTable.query(spec);
        Iterator<Item> iterator = items.iterator();
        List<Integer> posts;
        while (iterator.hasNext()) {
        	posts = iterator.next().getList("postIds"); 
        }
        posts.add(postCount); //add it to result
        //update table
        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
    			.withPrimaryKey("userId",user.getUser_id())
    			.withUpdateExpression("set postIds = :l")
    			.withValueMap(new ValueMap()
    					.withList(":l",posts));
        UpdateItemOutcome updateoutcome = myPostTable.updateItem(updateItemSpec);
    	
    	//add to category-post table
    	String postInCategoryTableName = "PostInCategory";
    	Table postInCategoryTable = dynamoDB.getTable(postInCategoryTableName);
    	ArrayList<String> categories = post.getCategory();
    	for(String category:categories){
    		//firstly query table to get current category
        	QuerySpec spec2 = new QuerySpec()
                    .withKeyConditionExpression("category = :c")
                    .withValueMap(new ValueMap()
                    		.withString(":c", category));
            ItemCollection<QueryOutcome> items2 = postInCategoryTable.query(spec2);
            Iterator<Item> iterator2 = items2.iterator();
            List<Integer> posts2;
            while (iterator.hasNext()) {
            	posts2 = iterator.next().getList("postIds"); 
            }
            posts.add(postCount); //add it to result
            //update table
            UpdateItemSpec updateItemSpec2 = new UpdateItemSpec()
        			.withPrimaryKey("category",category)
        			.withUpdateExpression("set postIds = :l")
        			.withValueMap(new ValueMap()
        					.withList(":l",posts2));
            UpdateItemOutcome updateoutcome2 = postInCategoryTable.updateItem(updateItemSpec2);
    }
    
    
    
    /**
     * add reply to post
     * <p>
     * 
     * @param post
     * @param reply
     */
    public static void addReplyToPost(Post post, Reply reply){
    	replyCount++; //add reply count by 1 and use it as reply ID
    	
    	//write reply into Reply table with reply id
    	String replyTableName = "Reply";
    	Table replyTable = dynamoDB.getTable(replyTableName);
    	PutItemOutcome outcome = replyTable.putItem(new Item()
    			.withPrimaryKey("replyId",replyCount)
    			.withString("onwer", reply.getOwner().getUser_id())
    			.withString("content", reply.getContent())
    			);
    	
    	//add the reply id to the Reply field in this post
    	String postTableName = "Post";
    	Table postTable = dynamoDB.getTable(postTableName);
    	//firstly query table to get current mypost
    	QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("postId = :id")
                .withValueMap(new ValueMap()
                		.withString(":id", post.getid????));
        ItemCollection<QueryOutcome> items = myPostTable.query(spec);
        Iterator<Item> iterator = items.iterator();
        List<Integer> posts;
        while (iterator.hasNext()) {
        	posts = iterator.next().getList("postIds"); 
        }
        posts.add(postCount); //add it to result
        //update table
        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
    			.withPrimaryKey("userId",user.getUser_id())
    			.withUpdateExpression("set postIds = :l")
    			.withValueMap(new ValueMap()
    					.withList(":l",posts));
        UpdateItemOutcome updateoutcome = myPostTable.updateItem(updateItemSpec);
    }
    

    /**
     * add the number of favorites of that post by 1
     * <p>
     * 
     * @param post
     */
    public static void favoritePost(User user, Post post){
    	//add the number of favorties in this post by 1
    	
    	//add the relationship to user-favoritePost table
    }
    

    /**
     * store the image to S3 and return URL as string
     * <p>
     * 
     * @param image
     * @return URL
     * @throws InterruptedException 
     */
    public static String storeImageToS3(BufferedImage image) throws InterruptedException{
    	 String existingBucketName = "*** Provide existing bucket name ***";
         String keyName            = "*** Provide object key ***";
         String filePath           = "*** Path to and name of the file to upload ***";  
         
         TransferManager tm = new TransferManager(new ProfileCredentialsProvider());        
         // TransferManager processes all transfers asynchronously, 
         // so this call will return immediately.
         Upload upload = tm.upload(
         		existingBucketName, keyName, new File(filePath));
         
         try {
         	// Or you can block and wait for the upload to finish
         	upload.waitForCompletion();      	
         } catch (AmazonClientException amazonClientException) {
         	amazonClientException.printStackTrace();
         }
         return keyName;
    }
    
    /**
     * get image from S3 using given url
     * <p>
     * 
     * @param url
     * @return image
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public static BufferedImage getImageFromS3(String url) throws FileNotFoundException, IOException{    	
    	//http://www.javaroots.com/2013/05/how-to-upload-and-download-images-in.html
    	AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());   
    	String existingBucketName = "*** Provide existing bucket name ***";
        String keyName            = "*** Provide object key ***";
    	S3Object object = s3Client.getObject(
    	                  new GetObjectRequest(existingBucketName, keyName));
    	
    	InputStream objectData = object.getObjectContent();
    	//transform to BufferedImage
    	BufferedImage image = null;
        if (objectData != null) {
            image = ImageIO.read(objectData);
        }
        return image;    	
    }
    
    
    
    
    
    
    
    
    /**
     * search posts with given category
     * <p>
     * 
     * @param category
     * @return List of Post
     */
    public static List<Post> searchPostsWithCategory(String category){
    	//search the category-post table and get the list of posts
    }
    
    
    /**
     * search posts with given key word
     * <p>
     * 
     * @param keyword
     * @return List of Post
     */
    public static List<Post> searchPostsWithKeyword(String keyword){
    	
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
    public static List<Post> searchPostsWithLocation(String lowlatitude,
    		String highlatitude, String lowlongitude, String highlongitude){
    	//when searching, check the remaining time and only return valid posts
    }
}
