// import stuff

class Server {
	public Server () {
		// some sort of db instance
		private AmazonDynamoDBSample db;
	}
	
	// User doing this
	public boolean writeToDatabase(User user, Post post) {
		db.writePostToDB(user, post);
	}
	
	// Map doing this
	public boolean loadPosts(/*get lat and long range from Map*/) {
		db.searchPostsWithLocation(lowLat, highLat, lowLong, highLong);
	}
	
	// User doing this
	public boolean updateFavorite(User user, Post post) {
		db.favoritePost(user, post);
	}
	
	// User doing this
	public boolean addReply(Post post, Reply reply) {
		db.addReplyToPost(post, reply);
	}
	
	// User doing this, changing Map
	public boolean searchDatabase(/* search query*/) {
		db.searchPosts(/*search query*/);		
	}
}