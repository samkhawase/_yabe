import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

import models.*;

public class BasicTest extends UnitTest {

//    @Test
//    public void aVeryImportantThingToTest() {
//        assertEquals(2, 1 + 1);
//    }

	// before the tests
	
	
	@Before
	public void setup(){
		Fixtures.deleteDatabase();
	}
	
	// the BIG test covering all
	@Test
	public void fullTest() {
	    Fixtures.loadModels("data.yml");
	 
	    // Count things
	    assertEquals(2, User.count());
	    assertEquals(3, Post.count());
	    assertEquals(3, Comment.count());
	 
	    // Try to connect as users
	    assertNotNull(User.connect("bob@gmail.com", "secret"));
	    assertNotNull(User.connect("jeff@gmail.com", "secret"));
	    assertNull(User.connect("jeff@gmail.com", "badpassword"));
	    assertNull(User.connect("tom@gmail.com", "secret"));
	 
	    // Find all of Bob's posts
	    List<Post> bobPosts = Post.find("author.email", "bob@gmail.com").fetch();
	    assertEquals(2, bobPosts.size());
	 
	    // Find all comments related to Bob's posts
	    List<Comment> bobComments = Comment.find("post.author.email", "bob@gmail.com").fetch();
	    assertEquals(3, bobComments.size());
	 
	    // Find the most recent post
	    Post frontPost = Post.find("order by postedAt desc").first();
	    assertNotNull(frontPost);
	    assertEquals("About the model layer", frontPost.title);
	 
	    // Check that this post has two comments
	    assertEquals(2, frontPost.comments.size());
	 
	    // Post a new comment
	    frontPost.addComment("Jim", "Hello guys");
	    assertEquals(3, frontPost.comments.size());
	    assertEquals(4, Comment.count());
	}
	
	
	// last indiv. test for comments class
	@Test
	public void useTheCommentsRelation() {
	    // Create a new user and save it
	    User bob = new User("bob@gmail.com", "secret", "Bob").save();
	 
	    // Create a new post
	    Post bobPost = new Post(bob, "My first post", "Hello world").save();
	 
	    // Post a first comment
	    bobPost.addComment("Jeff", "Nice post");
	    bobPost.addComment("Tom", "I knew that !");
	 
	    // Count things
	    assertEquals(1, User.count());
	    assertEquals(1, Post.count());
	    assertEquals(2, Comment.count());
	 
	    // Retrieve Bob's post
	    bobPost = Post.find("byAuthor", bob).first();
	    assertNotNull(bobPost);
	 
	    // Navigate to comments
	    assertEquals(2, bobPost.comments.size());
	    assertEquals("Jeff", bobPost.comments.get(0).author);
	    
	    // Delete the post
	    bobPost.delete();
	    
	    // Check that all comments have been deleted
	    assertEquals(1, User.count());
	    assertEquals(0, Post.count());
	    assertEquals(0, Comment.count());
	}
	
	// Comments test
	@Test
	public void postComments() {
	    // Create a new user and save it
	    User bob = new User("bob@gmail.com", "secret", "Bob").save();
	 
	    // Create a new post
	    Post bobPost = new Post(bob, "My first post", "Hello world").save();
	 
	    // Post a first comment
	    new Comment(bobPost, "Jeff", "Nice post").save();
	    new Comment(bobPost, "Tom", "I knew that !").save();
	 
	    // Retrieve all comments
	    List<Comment> bobPostComments = Comment.find("byPost", bobPost).fetch();
	 
	    // Tests
	    assertEquals(2, bobPostComments.size());
	 
	    Comment firstComment = bobPostComments.get(0);
	    assertNotNull(firstComment);
	    assertEquals("Jeff", firstComment.author);
	    assertEquals("Nice post", firstComment.content);
	    assertNotNull(firstComment.postedAt);
	 
	    Comment secondComment = bobPostComments.get(1);
	    assertNotNull(secondComment);
	    assertEquals("Tom", secondComment.author);
	    assertEquals("I knew that !", secondComment.content);
	    assertNotNull(secondComment.postedAt);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testPost(){
		// create a new user
		User bob = new User("bob@gmail.com", "passwd", "Bob").save();

		// create a new post
		Post newPost = new Post(bob, "my 1st blog","Hello world").save();
		
		// test that post has been created
		assertEquals(1,Post.count());

		// all posts by bob
		List<Post> bobPosts = Post.find("byAuthor", bob).fetch();
		
		// Tests
	    assertEquals(1, bobPosts.size());
	    Post firstPost = bobPosts.get(0);
	    assertNotNull(firstPost);
	    assertEquals(bob, firstPost.author);
	    assertEquals("my 1st blog", firstPost.title);
	    assertEquals("Hello world", firstPost.content);
	    assertNotNull(firstPost.postedAt);	
		
	}
	
	//user test cases
	@Test
	public void testUser(){
		
		// create a user
		User testUser = new User("Bink@booboo.com", "password", "Baby Bink");
		testUser.save();
		
		// fetch the user
		User bink = User.find("byEmail", "Bink@booboo.com").first();
		
		assertNotNull(bink);
		assertEquals("Baby Bink", bink.fullName);
		
	}
	@Test
	public void tryConnectAsUser() {
	    // Create a new user and save it
	    new User("bob@gmail.com", "secret", "Bob").save();
	    
	    // Test 
	    assertNotNull(User.connect("bob@gmail.com", "secret"));
	    assertNull(User.connect("bob@gmail.com", "badpassword"));
	    assertNull(User.connect("tom@gmail.com", "secret"));
	}

}
