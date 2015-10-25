# TwittMap

This project is the assignment of the course Cloud Computing at Columbia University

# Front-End:

Our web-page provides TWO selection dropdown lists for users:
(1) Category: 
There are five items in the category dropdown list: All, sports, music, tech and food. 
You could select any one of them and the map would show corresponding markers.

(2) Period:
Similar to category, you could select markers in the past 1/5/30/60 min period. 
What’s more, our map will automatically add/remove points which are in/out of current period every 3 seconds.

In addition, you could refer to # Records to know how many markers are currently showed in our TwittMap. 
When starting the program, it shows “Initializing” instead.

# Back-End

The back-end side is implement by Java Web Development and is running on Tomcat server.
It uses JDBC to access to the RDS database and is implemented with RESTful APIs by Java Servlet.
The back-end server is designed with two main functions:

(1) getTwitt

TwittGet.java and TwittGetTask.java is responsible for getting new twitt by Twitter API and store these data into 
a MySQL database running on Amazon RDS.

(2)back-end server

BackendServlet.java is the back-end server program deployed by AWS elastic Beanstalk. The server program is designed
for receiving HTTP GET request and reading geo information from database and then sending back to the front-end.

# Deployment

1. We create an Ubuntu 64-bit ec2 instance
2. We create an elastic beanstalk application
3. With load balancing, we upload the war file of the project and deployed it using elastic beanstalk




