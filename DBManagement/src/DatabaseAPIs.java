import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
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
        	User u = new User();
        	u.setUserInfo(12, "Ye");
        	Post p = new Post();
        	p.setSubject("Welcome to CS130");
        	p.setDetail("hello");
        	p.setTimestamp();
        	p.setLocation(11.10, 11.23);
        	p.setOwner(12);
        	ArrayList<String> c = new ArrayList<>();
        	c.add("study");
        	c.add("nerd");
        	p.setCategory(c);
        	
        	writePostToDB(u,p);
        	
            //test reply api
        	p.setId(1);//need to set id here because post is not returned by db
            Reply r = new Reply(9,"that's awesome!");
            r.setTimestamp();
            
            addReplyToPost(p,r);
            
            favoritePost(u,p);
            favoritePost(u,p);//favorite twice, the num of favorite should be 2
            
            //test getReplyForPost API
            List<Reply> l = getReplyForPost(p);
            System.out.print("result for get reply"+"\n");
            for(Reply re:l){
            	System.out.println(re.getId()+"\n");
            	System.out.println(re.getOwner()+"\n");
            	System.out.println(re.getTimestamp()+"\n");
            	System.out.println(re.getContent()+"\n");
            }
            
            //test search with gps function
            System.out.println("Result for search function:"+"\n");
            List<Post> list = searchPostsWithLocation(10.00,15.00,5.00,20.00);
            for(Post po:list){
            	System.out.println(po.getId()+"\n");
            	System.out.println(po.getOwner()+"\n");
            	System.out.println(po.getTimestamp()+"\n");
            	System.out.println(po.getDetail()+"\n");
            	System.out.println(po.getReplies().get(0).getContent()+"\n");
            }
            
            
            //test search with category function
            System.out.println("Result for search category function:"+"\n");
            List<Post> list2 = searchPostsWithCategory("study");
            for(Post po:list2){
            	System.out.println(po.getId()+"\n");
            	System.out.println(po.getOwner()+"\n");
            	System.out.println(po.getTimestamp()+"\n");
            	System.out.println(po.getDetail()+"\n");
            	System.out.println(po.getReplies().get(0).getContent()+"\n");
            }
            
          //test search with category function
            System.out.println("Result for search keyword function:"+"\n");
            List<Post> list3 = searchPostsWithKeyword("CS130");
            for(Post po:list3){
            	System.out.println(po.getId()+"\n");
            	System.out.println(po.getOwner()+"\n");
            	System.out.println(po.getTimestamp()+"\n");
            	System.out.println(po.getDetail()+"\n");
            	System.out.println(po.getReplies().get(0).getContent()+"\n");
            }
            
            System.out.println("Success!");
            
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
    	postTable.putItem(new Item()
    			.withPrimaryKey("postId",postCount)
    			.withString("subject", post.getSubject())
    			.withString("detail", post.getDetail())
    			.withInt("favorite", post.getFavorite())
    			.withInt("timeout", post.getTimeout())
    			.withString("Timestamp",post.getTimestamp().toString())
    			.withDouble("longitude", post.getLongitude())
    			.withDouble("latitude", post.getLatitude())
    			.withList("category", post.getCategory())
    			.withList("replyIds", new ArrayList<>())
    			.withInt("ownerId", post.getOwner())
    			);
        
    	/* 
    	//Removed, no need to store my posts, it will be stored locally 
    	//add to user-mypost table
    	String myPostTableName = "MyPost";
    	Table myPostTable = dynamoDB.getTable(myPostTableName);
    	//firstly query table to get current mypost
    	QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("userId = :id")
                .withValueMap(new ValueMap()
                		.withInt(":id", user.getUser_id()));
        ItemCollection<QueryOutcome> items = myPostTable.query(spec);
        Iterator<Item> iterator = items.iterator();
        List<Integer> posts = null;
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
        myPostTable.updateItem(updateItemSpec);
    	*/
    	
    	//add to category-post table
    	String postInCategoryTableName = "PostInCategory";
    	Table postInCategoryTable = dynamoDB.getTable(postInCategoryTableName);
    	ArrayList<String> categories = post.getCategory();
    	for(String category:categories){
    		//firstly query table to get current category
        	QuerySpec spec = new QuerySpec()
                    .withKeyConditionExpression("category = :c")
                    .withValueMap(new ValueMap()
                    		.withString(":c", category));
            ItemCollection<QueryOutcome> items = postInCategoryTable.query(spec);
            Iterator<Item> iterator = items.iterator();
            List<Integer> posts = new ArrayList<>();
            while (iterator.hasNext()) {
            	posts = iterator.next().getList("postIds"); 
            }
            posts.add(postCount); //add it to result
            //update table
            UpdateItemSpec updateItemSpec = new UpdateItemSpec()
        			.withPrimaryKey("category",category)
        			.withUpdateExpression("set postIds = :l")
        			.withValueMap(new ValueMap()
        					.withList(":l",posts));
            postInCategoryTable.updateItem(updateItemSpec);
    	}
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
    	replyTable.putItem(new Item()
    			.withPrimaryKey("replyId",replyCount)
    			.withInt("ownerId", reply.getOwner())
    			.withString("content", reply.getContent())
    			.withString("timestamp", reply.getTimestamp().toString())
    			);
    	
    	//add the reply id to the Reply field in this post
    	String postTableName = "Post";
    	Table postTable = dynamoDB.getTable(postTableName);
    	//firstly query table to get current replies
    	QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("postId = :id")
                .withValueMap(new ValueMap()
                		.withInt(":id", post.getId()));
        ItemCollection<QueryOutcome> items = postTable.query(spec);
        Iterator<Item> iterator = items.iterator();
        List<Integer> replies = null;
        while (iterator.hasNext()) {
        	replies = iterator.next().getList("replyIds"); 
        }
        if(replies==null)
        	replies = new ArrayList<>();
        replies.add(replyCount); //add it to result
        //update table
        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
        		.withPrimaryKey("postId",post.getId())
    			.withUpdateExpression("set replyIds = :l")
    			.withValueMap(new ValueMap()
    					.withList(":l",replies));
        postTable.updateItem(updateItemSpec);
    }
    
    /**
     * get the current reply of this post
     * <p>
     * 
     * @param post
     */
    public static List<Reply> getReplyForPost(Post post){
    	String postTableName = "Post";
    	Table postTable = dynamoDB.getTable(postTableName);
    	//firstly query table to get current replies
    	QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("postId = :id")
                .withValueMap(new ValueMap()
                		.withInt(":id", post.getId()));
        ItemCollection<QueryOutcome> items = postTable.query(spec);
        Iterator<Item> iterator = items.iterator();
        List<Integer> replyIds = new ArrayList<>();
        while (iterator.hasNext()) {
        	replyIds = iterator.next().getList("replyIds"); 
        }
        List<Reply> replies = new ArrayList<>();
        Iterator it = replyIds.iterator();
        while(it.hasNext()){        	
        	BigDecimal b= (BigDecimal) it.next();//Integer.parseInt(s);
        	String s = b.toString();
        	int id = Integer.parseInt(s);
        	//query the Reply table, get corresponding reply, add to list
        	String replyTableName = "Reply";
        	Table replyTable = dynamoDB.getTable(replyTableName);
        	Reply r = null;
        	//query Reply table
        	QuerySpec spec1 = new QuerySpec()
                    .withKeyConditionExpression("replyId = :id")
                    .withValueMap(new ValueMap()
                    		.withInt(":id", id));
            ItemCollection<QueryOutcome> items1 = replyTable.query(spec1);
            Iterator<Item> iterator1 = items1.iterator();
            while (iterator1.hasNext()) {
            	Item entry = iterator1.next();
            	r = new Reply(entry.getInt("ownerId"),entry.getString("content"));
            	r.setId(id);
            	Timestamp t=java.sql.Timestamp.valueOf(entry.getString("timestamp"));
            	r.setTimestamp(t);
            }
        	replies.add(r);
        }
        return replies;
    }
    

    /**
     * add the number of favorites of that post by 1
     * <p>
     * 
     * @param post
     */
    public static void favoritePost(User user, Post post){
    	//add the number of favorties in this post by 1
    	String postTableName = "Post";
    	Table postTable = dynamoDB.getTable(postTableName);
    	//firstly query table to get current num of favorite
    	QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("postId = :id")
                .withValueMap(new ValueMap()
                		.withInt(":id", post.getId()));
        ItemCollection<QueryOutcome> items = postTable.query(spec);
        Iterator<Item> iterator = items.iterator();
        int favorite = 0;
        while (iterator.hasNext()) {
        	favorite = iterator.next().getInt("favorite"); 
        }
        favorite++; //add it to result
        //update table
        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
        		.withPrimaryKey("postId",post.getId())
    			.withUpdateExpression("set favorite = :i")
    			.withValueMap(new ValueMap()
    					.withInt(":i",favorite));
        postTable.updateItem(updateItemSpec);
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
    public static String storeImageToS3(File image) throws InterruptedException{
    	 String existingBucketName = "cs130";
         String keyName            = (new Timestamp(new Date().getTime())).toString()+(new Random().nextInt());  
         
         TransferManager tm = new TransferManager(new ProfileCredentialsProvider());        
         // TransferManager processes all transfers asynchronously, 
         // so this call will return immediately.
         Upload upload = tm.upload(
         		existingBucketName, keyName, image);
         
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
    public static File getImageFromS3(String url) throws FileNotFoundException, IOException{    	
    	//http://www.javaroots.com/2013/05/how-to-upload-and-download-images-in.html
    	AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());   
    	String existingBucketName = "cs130";
        String keyName            = url;
    	S3Object object = s3Client.getObject(
    	                  new GetObjectRequest(existingBucketName, keyName));
    	
    	InputStream objectData = object.getObjectContent();
    	File file = null;
    	OutputStream outputStream = new FileOutputStream(file);
    	IOUtils.copy(objectData, outputStream);
    	outputStream.close();
        return file;    	
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
    	String tableName = "PostInCategory";
    	Table categoryTable = dynamoDB.getTable(tableName);
    	//firstly query table to get current mypost
    	QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("category = :c")
                .withValueMap(new ValueMap()
                		.withString(":c", category));
        ItemCollection<QueryOutcome> items = categoryTable.query(spec);
        Iterator<Item> iterator = items.iterator();
        List<Integer> postIds = new ArrayList<>();
        while (iterator.hasNext()) {
        	postIds = iterator.next().getList("postIds"); 
        }
        List<Post> posts = new ArrayList<>();
        Iterator it = postIds.iterator();
        while(it.hasNext()){        	
        	BigDecimal b= (BigDecimal) it.next();//Integer.parseInt(s);
        	String s = b.toString();
        	int id = Integer.parseInt(s);
        	//query the Reply table, get corresponding reply, add to list
        	String postTableName = "Post";
        	Table postTable = dynamoDB.getTable(postTableName);
        	Post p = null;
        	//query Reply table
        	QuerySpec spec1 = new QuerySpec()
                    .withKeyConditionExpression("postId = :id")
                    .withValueMap(new ValueMap()
                    		.withInt(":id", id));
            ItemCollection<QueryOutcome> items1 = postTable.query(spec1);
            Iterator<Item> iterator1 = items1.iterator();
            while (iterator1.hasNext()) {
            	Item entry = iterator1.next();
            	List<String> categories = entry.getList("category");
            	Post temp = new Post();
            	temp.setId(entry.getInt("postId"));
            	List<Reply> replies=getReplyForPost(temp);
            	p=new Post(
            			entry.getInt("favorite"),entry.getInt("timeout"),
            			entry.getString("subject"),entry.getString("detail"),
            			Timestamp.valueOf(entry.getString("Timestamp")),
            			entry.getDouble("longitude"),entry.getDouble("latitude"),
            			(ArrayList<String>)categories,(ArrayList<Reply>)replies,
            			entry.getInt("ownerId"),entry.getInt("postId"));
            	posts.add(p);
            	}  
            }        
        return posts;    	 
        }
    
    /**
     * search posts with given key word
     * <p>
     * 
     * @param keyword
     * @return List of Post
     */
    
    public static List<Post> searchPostsWithKeyword(String keyword){
    	//query post table to find all post with title or content containing keyword
    	List<Post> posts = new ArrayList<>();
    	String postTableName = "Post";
    	Table postTable = dynamoDB.getTable(postTableName);
    	ItemCollection<ScanOutcome> items = postTable.scan(); //get all posts
    	Iterator<Item> iterator = items.iterator();
    	while (iterator.hasNext()) {
        	Item entry = iterator.next();
        	String subject = entry.getString("subject");
        	String detail = entry.getString("detail");
        	if((!subject.contains(keyword)) && (!detail.contains(keyword)) )//not contain keyword
        		continue;
        	//now this post is valid, add to post list        	
        	//get the list of reply and query reply table to get
        	List<String> categories = entry.getList("category");
        	Post temp = new Post();
        	temp.setId(entry.getInt("postId"));
        	List<Reply> replies=getReplyForPost(temp);
        	Post p=new Post(
        			entry.getInt("favorite"),entry.getInt("timeout"),
        			entry.getString("subject"),entry.getString("detail"),
        			Timestamp.valueOf(entry.getString("Timestamp")),
        			entry.getDouble("longitude"),entry.getDouble("latitude"),
        			(ArrayList<String>)categories,(ArrayList<Reply>)replies,
        			entry.getInt("ownerId"),entry.getInt("postId"));
        	posts.add(p);
    	}
    	return posts;
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
    
    public static List<Post> searchPostsWithLocation(Double lowlatitude,
    		Double highlatitude, Double lowlongitude, Double highlongitude){
    	//when searching, check the remaining time and only return valid posts
    	String postTableName = "Post";
    	Table postTable = dynamoDB.getTable(postTableName);
        
        ScanSpec scanSpec = new ScanSpec()
    		    .withFilterExpression("latitude > :lowla and latitude< :highla and longitude > :lowlo and longitude < :highlo")
    		    .withValueMap(new ValueMap()
    		    		.with(":lowla", lowlatitude)
    		    		.with(":highla", highlatitude)
    		    		.with(":lowlo", lowlongitude)
    		    		.with(":highlo", highlongitude));
        
        ItemCollection<ScanOutcome> items = postTable.scan(scanSpec);

        Timestamp currentTime = new Timestamp(new Date().getTime());
        
        Iterator<Item> iterator = items.iterator();
        List<Post> posts = new ArrayList<>();
        while (iterator.hasNext()) {
        	Item entry = iterator.next();
        	Timestamp generateTime = Timestamp.valueOf(entry.getString("Timestamp"));
        	int favorite = entry.getInt("favorite");  //each favorite is 1 minutes
        	int totalLifeTimeinSeconds = favorite*60 +1*60*60; //original life time is 1 hour
        	Timestamp newTime = new Timestamp(generateTime.getTime() + totalLifeTimeinSeconds * 1000);
        	if(currentTime.compareTo(newTime)==1) //current time larger than newTime, not valid
        		continue;
        	//now this post is valid, add to post list        	
        	//get the list of reply and query reply table to get
        	List<String> categories = entry.getList("category");
        	Post temp = new Post();
        	temp.setId(entry.getInt("postId"));
        	List<Reply> replies=getReplyForPost(temp);
        	Post p=new Post(
        			entry.getInt("favorite"),entry.getInt("timeout"),
        			entry.getString("subject"),entry.getString("detail"),
        			generateTime,entry.getDouble("longitude"),entry.getDouble("latitude"),
        			(ArrayList<String>)categories,(ArrayList<Reply>)replies,
        			entry.getInt("ownerId"),entry.getInt("postId"));
        	posts.add(p);
        }        
        return posts;
    }  
}
