package twitt;


import twitter4j.FilterQuery;
import twitter4j.GeoLocation;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;


public class TwittGetTask extends Thread
{
	DBController controller;
	String keyword;
	String[] keywordSet;
	int type;
	
	public TwittGetTask(int typeIndex)
	{
		controller = new DBController();
		controller.setConnnection();
		keywordSet = KeywordTable.getKeywordSet(typeIndex);
		keyword = keywordSet[0];
		type = typeIndex;
		System.out.println("task for " + keyword + " initialized");
	}
	
	public void run()
	{
		ConfigurationBuilder cb = new ConfigurationBuilder();
		if(type % 2 == 0)
		{
			cb.setDebugEnabled(true)
	           .setOAuthConsumerKey("")
	           .setOAuthConsumerSecret("")
	           .setOAuthAccessToken("")
	           .setOAuthAccessTokenSecret("");
		}
		else
		{
			cb.setDebugEnabled(true)
	           .setOAuthConsumerKey("")
	           .setOAuthConsumerSecret("")
	           .setOAuthAccessToken("")
	           .setOAuthAccessTokenSecret("");
		}
        
        
        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        StatusListener listener = new StatusListener() 
        {	
            @Override
            public void onStatus(Status status) 
            {
                GeoLocation location = status.getGeoLocation();
                if(location != null)
                	controller.writeToDB(status, keyword);
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
            
            @Override
            public void onScrubGeo(long userId, long upToStatusId) {}

            @Override
            public void onStallWarning(StallWarning warning) {}

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        
        FilterQuery tweetFilter = new FilterQuery();
        tweetFilter.track(keywordSet);
        twitterStream.addListener(listener);
        twitterStream.filter(tweetFilter);
	}
}
