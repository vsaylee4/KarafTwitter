package sjsu.falcon.karaftwitter;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

/****
 * TWITTER KARAF IMPLEMENTATION - Eight or more various twitter API implementation
 * 
 * Sudha Amarnath 
 * Chaitrali Deshmukh 
 * Saylee Vyawahare 
 * Anvitha Jain
 *
 */

@SuppressWarnings("deprecation")
public class TwitterUtilityAPIs {

	private Properties getProperties() {
		InputStream input = this.getClass().getResourceAsStream("/properties/application.properties");
		Properties properties = new Properties();
		try {
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	public OAuthConsumer fetchOAuthConsumerCredentials() {
		Properties applicationProperties = getProperties();
		String userKey = applicationProperties.getProperty("user.key");
		String userSecret = applicationProperties.getProperty("user.secret");
		String userAccessToken = applicationProperties.getProperty("user.access.token");
		String userAccessSecret = applicationProperties.getProperty("user.access.secret");
		OAuthConsumer httpOAuthConsumer = new CommonsHttpOAuthConsumer(userKey, userSecret);
		httpOAuthConsumer.setTokenWithSecret(userAccessToken, userAccessSecret);
		return httpOAuthConsumer;
	}

	@SuppressWarnings("resource")
	public HttpResponse performHttpGet(String url) {

		HttpResponse httpResponse = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			fetchOAuthConsumerCredentials().sign(httpGet);
			HttpClient httpClient = new DefaultHttpClient();
			httpResponse = httpClient.execute(httpGet);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OAuthMessageSignerException e) {
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			e.printStackTrace();
		}
		return httpResponse;

	}

	@SuppressWarnings("resource")
	public HttpResponse performHttpPost(String url) {

		HttpResponse httpResponse = null;
		try {
			HttpPost httpPost = new HttpPost(url);
			fetchOAuthConsumerCredentials().sign(httpPost);
			HttpClient httpClient = new DefaultHttpClient();
			httpResponse = httpClient.execute(httpPost);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OAuthMessageSignerException e) {
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			e.printStackTrace();
		}
		return httpResponse;

	}

	/****
	 * Sudha Amarnath
	 * HTTP post and get method
	 * Usage examples
	 *   a. <taylorswift>                   - Searches "#taylorswift" and dumps tweets containing that hashtag
	 *   b. <katyperry filter:media>        - Searches keyword kayperry in filter media
	 *   c. <geocode:40.712776,-74.005974>  - Prints all tweets originated only from geocode New York
	 */ 
	public List<String> queryString(String searchString) {
		List<String> twitterResponseList = new ArrayList<String>();
		try {

			Properties properties = getProperties();

			int maxLineNumbers = Integer.valueOf(properties.getProperty("max.line.numbers"));
			String twitterUrl = properties.getProperty("api.twitter.query.string");

			String patternFilter = "(.*) filter:(\\w*)";
			Pattern rFilter = Pattern.compile(patternFilter);
			Matcher mFilter = rFilter.matcher(searchString);

			String patternGeocode = "(.*)geocode:(.*)";
			Pattern rGeocode = Pattern.compile(patternGeocode);
			Matcher mGeocode = rGeocode.matcher(searchString);

			if (mFilter.find()) {
				twitterUrl = twitterUrl + "?count=" + maxLineNumbers + "&q=" + mFilter.group(1) + "%20filter%3A"
						+ mFilter.group(2) + "&src=typd&lang=en&result_type=recent";

				// building this
				// twitterUrl =
				// "https://api.twitter.com/1.1/search/tweets.json?f=tweets&q=titanic%20from%3Aimdb&src=typd&lang=en";
				// twitterUrl =
				// "https://api.twitter.com/1.1/search/tweets.json?q=from%3ACmdr_Hadfield%20%23nasa&result_type=popular";
				// twitterUrl =
				// "https://api.twitter.com/1.1/search/tweets.json?q=&geocode=37.338207,-121.886330,1km&lang=en&result_type=recent";
				// twitterUrl =
				// "https://api.twitter.com/1.1/search/tweets.json?q=Avengers%20from%3AIMDb";
				// twitterUrl =
				// "https://api.twitter.com/1.1/search/tweets.json?src=typd&q=puppy%20filter%3Amedia";

			} else if (mGeocode.find()) {

				twitterUrl = twitterUrl + "?count=" + maxLineNumbers + "&q=&geocode=" + mGeocode.group(2)
						+ ",1km&lang=en&result_type=recent";
			} else {
				twitterUrl = twitterUrl + "?count=" + maxLineNumbers + "&q=%23" + searchString;
			}
			System.out.println(twitterUrl);

			HttpResponse httpResponse = performHttpGet(twitterUrl);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				JSONObject jsonobject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));

				System.out.println("jsonObject" + jsonobject);

				JSONArray jsonArray = (JSONArray) jsonobject.get("statuses");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = (JSONObject) jsonArray.get(i);
					System.out.println("jsonObject" + jsonObject);
					JSONObject userObject = (JSONObject) jsonObject.get("user");

					String displayText = (String) userObject.get("screen_name") + " : "
							+ (String) jsonObject.get("text");
					twitterResponseList.add(displayText);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return twitterResponseList;
	}

	/****
	 * Sudha Amarnath
	 * HTTP post method
	 * Usage examples
	 *   a. <Caltrain>   - Follow user Caltrain
	 *   	 
	 */
	public List<String> followAnotherUser(String screen_name) {
		List<String> twitterResponseList = new ArrayList<String>();
		try {

			Properties properties = getProperties();

			String twitterUrl = properties.getProperty("api.twitter.followanotheruser");

			twitterUrl = twitterUrl + "?screen_name=" + screen_name;

			HttpResponse httpResponse = performHttpPost(twitterUrl);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				twitterResponseList.add("Twitter API Request Success. Now following: " + screen_name);
			} else {
				twitterResponseList.add("Twitter API Request Failure");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return twitterResponseList;
	}

	/****
	 * Sudha Amarnath
	 * HTTP post method
	 * Usage examples
	 *   a. <Caltrain>   - Unfollow user Caltrain
	 *   	 
	 */
	public List<String> unfollowUser(String screen_name) {
		List<String> twitterResponseList = new ArrayList<String>();
		try {

			Properties properties = getProperties();

			String twitterUrl = properties.getProperty("api.twitter.unfollowuser");

			twitterUrl = twitterUrl + "?screen_name=" + screen_name;

			HttpResponse httpResponse = performHttpPost(twitterUrl);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				twitterResponseList.add("Twitter API Request Success. Now Unfollowing: " + screen_name);
			} else {
				twitterResponseList.add("Twitter API Request Failure");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return twitterResponseList;
	}

	
	/****
	 * Saylee Vyawahare
	 * HTTP post method
	 * Usage examples
	 *   a. <Hello there! Sample Tweet!>   - To post tweet for the test user homepage
	 *   
	 */
	public List<String> postTweet(String status) {
		List<String> twitterResponseList = new ArrayList<String>();
		try {

			Properties properties = getProperties();

			String twitterUrl = properties.getProperty("api.twitter.status.update");

			twitterUrl = twitterUrl + "?status=" + status.replace(" ", "%20");
			HttpResponse httpResponse = performHttpPost(twitterUrl);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				twitterResponseList.add("Tweet Successful Posted: " + status);
			} else {
				twitterResponseList.add("Tweet Failure");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return twitterResponseList;
	}

	/****
	 * Chaitrali Deshmukh
	 * HTTP get method
	 * Usage examples
	 *    Prints all recent tweets from the timeline - default is limit to 5 tweets. Can be changed as required.
	 */
	public List<String> homeTimeline() {
		List<String> twitterResponseList = new ArrayList<String>();
		try {

			Properties properties = getProperties();

			int maxLineNumbers = Integer.valueOf(properties.getProperty("max.line.numbers"));
			String twitterUrl = properties.getProperty("api.twitter.home.timeline") + "?count=" + maxLineNumbers;

			HttpResponse httpResponse = performHttpGet(twitterUrl);
			if (200 == httpResponse.getStatusLine().getStatusCode()) {
				JSONArray jsonArray = new JSONArray(EntityUtils.toString(httpResponse.getEntity()));
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = (JSONObject) jsonArray.get(i);
					JSONObject userObject = (JSONObject) jsonObject.get("user");
					String displayText = (String) userObject.get("screen_name") + " : "
							+ (String) jsonObject.get("text");
					twitterResponseList.add(displayText);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return twitterResponseList;

	}

	/****
	 * Chaitrali Deshmukh
	 * HTTP get method
	 * Usage examples
	 *    Fetches the trends available for a country mentioned and prints default as 5 links.
	 */
	public List<String> trendsAvailable() {
		List<String> twitterResponseList = new ArrayList<String>();
		try {

			Properties properties = getProperties();

			int maxLineNumbers = Integer.valueOf(properties.getProperty("max.line.numbers"));
			String twitterUrl = properties.getProperty("api.twitter.trends.available");

			String country = "United States";
			
			int j = 0;
			HttpResponse httpResponse = performHttpGet(twitterUrl);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				JSONArray jsonArray = new JSONArray(EntityUtils.toString(httpResponse.getEntity()));
				for (int i = 1; i < jsonArray.length(); i++) {

					JSONObject object = (JSONObject) jsonArray.get(i);
					String getCountry = (String) object.get("country");
					
					if (j < maxLineNumbers && getCountry.contains(country) == true) {
						j++;
						String displayText = country + " : " + (String) object.get("url");
						twitterResponseList.add(displayText);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return twitterResponseList;
	}

	/****
	 * Chaitrali Deshmukh
	 * HTTP get method
	 * Usage examples
	 *    Fetches the supported languages in twitter - default set to 5 prints.
	 */
	public List<String> languageSupport() {
		List<String> twitterResponseList = new ArrayList<String>();
		try {

			Properties properties = getProperties();

			int maxLineNumbers = Integer.valueOf(properties.getProperty("max.line.numbers"));
			String twitterUrl = properties.getProperty("api.twitter.language.support");

			HttpResponse httpResponse = performHttpGet(twitterUrl);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				JSONArray jsonArray = new JSONArray(EntityUtils.toString(httpResponse.getEntity()));

				System.out.println(jsonArray);

				for (int i = 0; i < jsonArray.length() && i < maxLineNumbers; i++) {
					JSONObject object = (JSONObject) jsonArray.get(i);
					String displayText = (String) object.get("name");
					twitterResponseList.add(displayText);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return twitterResponseList;
	}

	/****
	 * Saylee Vyawahare
	 * HTTP get method
	 * Usage examples
	 *    Gets the trends closest to a geocode.
	 */
	public List<String> trendsClosest() {
		List<String> twitterResponseList = new ArrayList<String>();
		try {

			Properties properties = getProperties();

			int maxLineNumbers = Integer.valueOf(properties.getProperty("max.line.numbers"));
			String twitterUrl = properties.getProperty("api.twitter.trends.closest") + "?lat=40.712776&long=-74.005974";

			System.out.println(twitterUrl);

			HttpResponse httpResponse = performHttpGet(twitterUrl);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				JSONArray jsonArray = new JSONArray(EntityUtils.toString(httpResponse.getEntity()));
				for (int i = 0; i < jsonArray.length() && i < maxLineNumbers; i++) {
					JSONObject object = (JSONObject) jsonArray.get(i);
					String displayText = (String) object.get("country") + " : " + (String) object.get("url");
					twitterResponseList.add(displayText);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return twitterResponseList;
	}

	/****
	 * Saylee Vyawahare
	 * HTTP get method
	 * Usage examples
	 *    Dumps the list of users who are following the test user.
	 */
	public List<String> followersList(String searchStr) {
		List<String> twitterResponseList = new ArrayList<String>();
		try {

			Properties properties = getProperties();

			int maxLineNumbers = Integer.valueOf(properties.getProperty("max.line.numbers"));
			String twitterUrl = properties.getProperty("api.twitter.follower.list") + "?screen_name=" + searchStr;

			HttpResponse httpResponse = performHttpGet(twitterUrl);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				JSONObject jsonobject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
				JSONArray jsonArray = (JSONArray) jsonobject.get("users");
				for (int i = 0; i < jsonArray.length() && i < maxLineNumbers; i++) {
					JSONObject object = (JSONObject) jsonArray.get(i);
					String displayText = (String) object.get("name") + " : " + (String) object.get("screen_name");
					twitterResponseList.add(displayText);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return twitterResponseList;
	}

}
