package twitt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import java.util.Timer;
import java.util.TimerTask;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class BackendServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private DBController controller;

    public BackendServlet() 
    {
        super();
        TwittGet.startCollect();
        controller = new DBController();
        controller.setConnnection();
        setUpdateTimer();
    }
    
    private void setUpdateTimer()
    {
    	TimerTask task = new TimerTask() 
    	{
    		@Override
    		public void run() {controller.updateTable();}
    	};
    	Timer timer = new Timer();
    	timer.scheduleAtFixedRate(task, 3000, 1000 * 60);
    }

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	{
    	try
    	{ 
    		String message = request.getParameter("message");
    		//System.out.println("received message: " + message);
    		if(message == null)
    			message = "200 " + "all"; 
    		List<GeoInfor> result = controller.getGeoInfor(message);
    		PrintWriter out = response.getWriter();
    		StringBuilder sb = new StringBuilder();
    		if(result.size() == 0)
    		{
    			System.out.println("no matching record");
				out.write("no matching record");
    		}
    		else
    		{
    			//System.out.println("find " + result.size() + " records");
        		
        		for(GeoInfor ele : result)
            	{
            		JSONObject obj = new JSONObject();
    				obj.put("lng", ele.longitude).put("lat", ele.latitude).put("id", ele.id);
    				sb.append(obj);
    				sb.append("\n");
            	}
        		sb.deleteCharAt(sb.length() - 1);
        		out.write(sb.toString());
    		}
    		out.flush();
    	}
    	catch(IOException e){ e.printStackTrace(); }
    	catch(JSONException e){ e.printStackTrace(); }
	}

    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException 
	{
		
	}

}
