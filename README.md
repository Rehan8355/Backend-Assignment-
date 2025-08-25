Alumni LinkedIn Profile Searcher
A Spring Boot backend application that enables users to search for the LinkedIn profiles of alumni from a specified educational institution. The application utilizes the PhantomBuster API to scrape data from LinkedIn, processes the information, and serves it to users with an easy REST API.

Table of Contents
Objective

Features
Architecture
API Endpoints
Request & Response Examples
Setup & Configuration
How It Works
Error Handling
Known Limitations
Credits & License

Objective
Develop a Spring Boot web application for searching LinkedIn profiles of alumni from a specific educational institution. The system leverages PhantomBuster's automation agents to fetch and serve alumni data based on user-provided search criteria.

Features
RESTful Alumni Search API

Integration with PhantomBuster: Launches, monitors, and fetches data from PhantomBuster agents.

Flexible Search: Filter by university, current designation, and pass-out year.

Automated Data Retrieval: Parses and delivers LinkedIn alumni profiles.

Centralized Error Handling

Ready for Extension: Clean, modular service structure.

Architecture
Controller Layer: Accepts user search requests and returns results.

Service Layer: Orchestrates PhantomBuster agent launches, monitors status, fetches results.

HTTP Engine: Generic utility to interact with external REST services (PhantomBuster).

POJOs: Map request and response bodies.

Exception Handling: Global exception handler for graceful error responses.

Workflow Diagram
User hits /api/alumni/search with search parameters.

Service launches PhantomBuster agent → gets containerId.

Service checks agent status until finished.

Service fetches agent output → retrieves data file URL.

Service downloads and parses alumni JSON data.

Filtered data is returned in API response.

API Endpoints
Search Alumni Profiles
URL: /api/alumni/search

Method: POST

Request Body:

json
{
  "university": "NAME_OF_THE_UNIVERSITY",
  "designation": "CURRENT_DESIGNATION",
  "passoutYear": "OPTIONAL_PASSOUT_YEAR"
}
Success Response: 200 OK

json
{
  "status": "success",
  "data": [
    {
      "name": "John Doe",
      "currentRole": "Software Engineer",
      "university": "Example University",
      "location": "San Francisco, CA",
      "linkedinHeadline": "Tech Enthusiast | Problem Solver",
      "year": "2015-2019"
    },
    ...
  ]
}
Error Response: e.g., 500 Internal Server Error

json
{
  "status": "error",
  "message": "Error description here"
}
Request & Response Examples
Sample POST request via Postman:

POST /api/alumni/search

Body (JSON):

json
{
  "university": "IIT Bombay",
  "designation": "Software Engineer",
  "passoutYear": "2020"
}
Sample Success Response:

json
{
  "status": "success",
  "data": [
    {
      "name": "Jane Smith",
      "currentRole": "Software Engineer",
      "university": "IIT Bombay",
      "location": "Bengaluru, India",
      "linkedinHeadline": "Cloud Developer at TechCorp",
      "year": "2016-2020"
    }
  ]
}
Setup & Configuration
Prerequisites
Java 17+

Maven

PhantomBuster account (with working agent & API key)

Internet connection

Application Properties
Configure these in your application.properties:

text
# PhantomBuster API Key
phantombuster.apiKey=YOUR_PHANTOMBUSTER_API_KEY

# API Endpoints
phantomBuster.launchAgentUrl=https://api.phantombuster.com/api/v2/agents/launch
phantomBuster.checkAgentStatusUrl=https://api.phantombuster.com/api/v2/containers/fetch?id={containerId}
phantomBuster.fetchDataUrl=https://api.phantombuster.com/api/v2/containers/fetch-output?id={containerId}&mode=json

# Agent Launch
launchAgentRequestBody.id=PASTE-YOUR-AGENT-ID
launchAgentRequestBody.sessionCookie=PASTE-YOUR-AUTH-SESSIONCOOKIE
Build & Run
bash
mvn clean install
java -jar target/alumni-profile-searcher-0.0.1-SNAPSHOT.jar
App will start on localhost:8080 by default.

How It Works
User submits a search query via API.

Spring Boot app launches the PhantomBuster agent using provided criteria.

The app polls agent status (running or finished). If running, thread is put on hold (e.g., 60 seconds sleep) to allow agent to collect data.

Manual intervention needed: For some PhantomBuster agents, results are not available until the agent is forcibly aborted (currently must be done on the PhantomBuster site).

Once the agent is finished, the app fetches the output (a JSON file URL) and downloads it.

Profiles are parsed and returned in a structured JSON response.

Error Handling
All errors/exceptions are caught centrally and returned in a standard format:

json
{
  "status": "error",
  "message": "Description of issue"
}


Known Limitations & Manual Steps :

Agent Abortion Needed:
PhantomBuster allows you for fetching the scrape data only when the agent is aborted and there is no support or endpoint for aborting the agent by code and due to which i hoild the thread for 1min in code in this time we have to go to dashboard where agent is running and abort the agent then when thread back further execution happens and data fetch from phantomBuster by call the container/fetch-output endpoint .

and one more thing because agent takes times for fetching the data so thats why there is also need for sometime holding the thraed so that agent fetch data from linkedin.   
