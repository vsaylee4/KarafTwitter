package sjsu.falcon.karaftwitter.test;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import sjsu.falcon.karaftwitter.TwitterUtilityAPIs;

/****
 * TWITTER KARAF IMPLEMENTATION - Eight or more various twitter API implementation - Test Cases
 * 
 * Sudha Amarnath 
 * Chaitrali Deshmukh
 * Saylee Vyawahare 
 * Anvitha Jain 
 *
 */

public class Tests {

	TwitterUtilityAPIs twitterUtility = new TwitterUtilityAPIs();

	/****
	 * Sudha Amarnath 
	 */	
	@Test
	public void testQueryString() {
		List<String> response = twitterUtility.queryString("taylorswift");
		assertNotNull(response);
		response = twitterUtility.queryString("katyperry filter:media");
		assertNotNull(response);
		response = twitterUtility.queryString("geocode:40.712776,-74.005974");
		assertNotNull(response);		
	}

	/****
	 * Chaitrali Deshmukh
	 *
	 */
	@Test
	public void testPostTweet() {
		List<String> response = twitterUtility.postTweet("hello there!");
		assertNotNull(response);
	}
	/****
	 * Saylee Vyawahare
	 *
	 */	
	@Test
	public void testTrendsAvailable() {
		List<String> response = twitterUtility.trendsAvailable();
		assertNotNull(response);
	}	

	
	/****
	 * Anvitha Jain
	 *
	 */
	@Test
	public void testFollowAnotherUser() {
		List<String> response = twitterUtility.followAnotherUser("Caltrain");
		assertNotNull(response);
	}
	@Test
	public void testUnfollowUser() {
		List<String> response = twitterUtility.unfollowUser("Caltrain");
		assertNotNull(response);
	}
	
	@Test
	public void testHomeTimeline() {
		List<String> response = twitterUtility.homeTimeline();
		assertNotNull(response);
	}
	@Test
	public void testLanguageSupport() {
		List<String> response = twitterUtility.languageSupport();
		assertNotNull(response);
	}
	@Test
	public void testTrendsClosest() {
		List<String> response = twitterUtility.trendsClosest();
		assertNotNull(response);
	}
	@Test
	public void testFollowersList() {
		List<String> response = twitterUtility.followersList("taylorswift");
		assertNotNull(response);
	}
}
