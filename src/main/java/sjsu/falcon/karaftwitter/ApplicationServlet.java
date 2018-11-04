package sjsu.falcon.karaftwitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/****
 * TWITTER KARAF IMPLEMENTATION - Eight various twitter application methods
 * 
 * Sudha Amarnath 
 * Chaitrali Deshmukh
 * Saylee Vyawahare 
 * Anvitha Jain 
 *
 */


public class ApplicationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * servlet doPost method to implement all 8 Twitter APIs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			TwitterUtilityAPIs twitterHelper = new TwitterUtilityAPIs();
			
			String twitteroptions = request.getParameter("twitteroptions");
			String searchStr = request.getParameter("searchStr");
			List<String> twitterResponseList = new ArrayList<String>();

			switch (twitteroptions) {
			case "Follow User":
				twitterResponseList = twitterHelper.followAnotherUser(searchStr);
				break;
			case "Post Tweet":
				twitterResponseList = twitterHelper.postTweet(searchStr);
				break;
			case "Trends Available":
				twitterResponseList = twitterHelper.trendsAvailable();
				break;
			case "Query String":
				twitterResponseList = twitterHelper.queryString(searchStr);
				break;
			case "Home Timeline":
				twitterResponseList = twitterHelper.homeTimeline();
				break;
			case "Language Support":
				twitterResponseList = twitterHelper.languageSupport();
				break;
			case "Trends Closest":
				twitterResponseList = twitterHelper.trendsClosest();
				break;
			case "Followers List":
				twitterResponseList = twitterHelper.followersList(searchStr);
				break;
			case "Unfollow User":
				twitterResponseList = twitterHelper.unfollowUser(searchStr);
				break;
			default:
				break;
		}
			request.getSession().setAttribute("twitterResponse", twitterResponseList);
			request.getSession().setAttribute("option", twitteroptions);
		} catch (Exception e) {
			e.printStackTrace();
		}

		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/index.jsp");
		requestDispatcher.forward(request, response);
	}

	/**
	 * Servlet doGet method
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/index.jsp");
		requestDispatcher.forward(request, response);
	}

}
