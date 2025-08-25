# 🎓 Alumni LinkedIn Profile Searcher

A production-ready **Spring Boot** backend that searches and returns **LinkedIn alumni profiles** for a given educational institution.  
It integrates with **PhantomBuster – LinkedIn Search Export Agent** to launch a scrape, monitor it, fetch results, and return a clean JSON response.

> ⚠️ **Note**: Scraping LinkedIn content may violate LinkedIn’s Terms of Service. Use responsibly and only with accounts and data you’re authorized to access.

---

## ✨ Features

- 🔎 **RESTful search API** for alumni profiles
- ⚡ **PhantomBuster integration** (launch → poll → fetch output)
- 🎯 **Flexible filters**: university, designation, pass-out year
- 🧰 **Robust error handling** with consistent JSON error shape
- 🧩 **Modular architecture** (Controller / Service / HTTP client / DTOs)
- 📝 **Config via properties or environment variables**

---

## 🧱 Architecture

- **Controller** — Validates input and returns DTO responses  
- **Service** — Orchestrates PhantomBuster agent lifecycle  
- **HTTP Client** — Generic REST client (e.g., `RestTemplate`/`WebClient`)  
- **DTOs/POJOs** — Request & Response models  
- **GlobalExceptionHandler** — Centralized error responses

### 🔄 High-level workflow

1. Client calls `POST /api/alumni/search` with filters.  
2. Service launches PhantomBuster agent → receives `containerId`.  
3. Service **polls** container status until finished (or times out).  
4. Service fetches **output JSON file URL**.  
5. Service downloads & parses alumni data → returns structured JSON.

---

## 🚀 API

### Search Alumni Profiles

**URL**: `/api/alumni/search`  
**Method**: `POST`  
**Content-Type**: `application/json`

#### Request Body
```json
{
  "university": "IIT Bombay",
  "designation": "Software Engineer",
  "passoutYear": "2020"
}
✅ Success Response (200)
json
Copy
Edit
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
❌ Error Response (500)
json
Copy
Edit
{
  "status": "error",
  "message": "Unable to fetch alumni data"
}
Example curl
bash
Copy
Edit
curl -X POST http://localhost:8080/api/alumni/search \
  -H "Content-Type: application/json" \
  -d '{
    "university": "IIT Bombay",
    "designation": "Software Engineer",
    "passoutYear": "2020"
  }'
⚙️ Setup & Configuration
✅ Prerequisites
Java 17+

Maven

PhantomBuster account

A configured LinkedIn Search Export Agent

API key

Session cookie (if required by your agent)

Internet connectivity

🔑 Application Properties (application.properties)
You can keep secrets in env vars and reference them here.

properties
Copy
Edit
# === PhantomBuster Auth ===
phantombuster.apiKey=${PHANTOMBUSTER_API_KEY}

# === PhantomBuster Endpoints ===
phantomBuster.launchAgentUrl=https://api.phantombuster.com/api/v2/agents/launch
phantomBuster.checkAgentStatusUrl=https://api.phantombuster.com/api/v2/containers/fetch?id={containerId}
phantomBuster.fetchDataUrl=https://api.phantombuster.com/api/v2/containers/fetch-output?id={containerId}&mode=json

# === Agent Settings ===
launchAgentRequestBody.id=${PHANTOMBUSTER_AGENT_ID}
launchAgentRequestBody.sessionCookie=${PHANTOMBUSTER_SESSION_COOKIE}

# === Optional Tuning ===
alumni.poll.maxAttempts=20         # how many times to poll the container
alumni.poll.delayMillis=5000       # delay between polls
alumni.download.timeoutMillis=30000
🌱 Environment Variables (recommended)
bash
Copy
Edit
export PHANTOMBUSTER_API_KEY=your_api_key_here
export PHANTOMBUSTER_AGENT_ID=your_agent_id_here
export PHANTOMBUSTER_SESSION_COOKIE=your_session_cookie_here
You can also use a .env file + a loader if you prefer.

▶️ Build & Run
Using Maven:

bash
Copy
Edit
mvn clean install
java -jar target/alumni-profile-searcher-0.0.1-SNAPSHOT.jar
The service will start at:

arduino
Copy
Edit
http://localhost:8080
⚡ How It Works (Operational Notes)
The service launches your LinkedIn agent with input parameters.

It polls PhantomBuster for container status until it is finished (or times out).

Some agents only expose outputs after aborting:

If so, you must manually abort the agent in your PhantomBuster dashboard.

Alternatively, adjust your agent settings to produce output on completion.

After output is available, the service downloads the JSON and maps it into response DTOs.

⚠️ Known Limitations & Manual Steps
Agent abortion requirement: Certain PhantomBuster agents expose data only when aborted.

There is no public API to abort programmatically.

Workaround: The service polls and you may need to abort manually in the PhantomBuster dashboard if your agent behaves this way.

Scrape latency: Expect ~1–2 minutes for the agent to gather data before it’s available.

LinkedIn ToS: Ensure your use complies with LinkedIn’s Terms of Service.

Quick checklist before first request
Log in to PhantomBuster.

Create/configure LinkedIn Search Export agent.

Copy Agent ID & Session Cookie → set them in env/properties.

Start the Spring Boot app.

Call POST /api/alumni/search.

🛑 Error Handling
All errors return a consistent JSON shape:

json
Copy
Edit
{
  "status": "error",
  "message": "Description of the issue"
}
Common cases:

Missing/invalid config → 500 with message

PhantomBuster API errors → 502/500 with upstream message

Timeout waiting for agent output → 504 Gateway Timeout (recommended)

