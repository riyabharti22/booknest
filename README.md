BookNest
A full-stack book tracking web application — search millions of books, track your reading, rate what you've read, and get AI-powered recommendations.
Live Demo: https://booknest-production-a533.up.railway.app
Repository: https://github.com/riyabharti22/booknest

Features

Search 8.5M+ books via Open Library API
Track reading status — Want to Read / Reading / Read
Rate books on a 5-star scale
AI-powered book recommendations using Groq LLM (llama-3.3-70b)
GitHub OAuth2 login — no passwords needed
Personal reading shelf per user
Production deployed with CI/CD via GitHub


Tech Stack
LayerTechnologyBackendJava 21, Spring Boot 3FrontendThymeleaf, HTML, CSSDatabaseApache Cassandra (DataStax Astra DB)AuthSpring Security, GitHub OAuth2AIGroq API (llama-3.3-70b-versatile)HTTP ClientSpring WebClientDeploymentRailway (auto-deploy via GitHub)BuildMaven

Project Structure
src/main/java/com/booknest/booknest/
├── ai/              # Groq AI recommendations
├── author/          # Author data
├── book/            # Book detail and controller
├── config/          # Security and WebClient config
├── home/            # Home page controller
├── search/          # Search controller
└── userbooks/       # User reading list and ratings

Getting Started
Prerequisites

Java 21+
Maven
DataStax Astra DB account
GitHub OAuth App
Groq API key

1. Clone the repo
bashgit clone https://github.com/riyabharti22/booknest.git
cd booknest
2. Set environment variables
propertiesGITHUB_CLIENT_ID=your_github_client_id
GITHUB_CLIENT_SECRET=your_github_client_secret
ASTRA_CLIENT_ID=your_astra_client_id
ASTRA_CLIENT_SECRET=your_astra_client_secret
GROQ_API_KEY=your_groq_api_key
3. Add Astra DB secure bundle
Place secure-connect-booknest.zip in src/main/resources/
4. Run
bashmvn spring-boot:run
Visit http://localhost:8080

Environment Variables
VariableDescriptionGITHUB_CLIENT_IDGitHub OAuth App Client IDGITHUB_CLIENT_SECRETGitHub OAuth App Client SecretASTRA_CLIENT_IDDataStax Astra DB usernameASTRA_CLIENT_SECRETDataStax Astra DB passwordGROQ_API_KEYGroq API key for AI recommendations

Never commit real credentials. All secrets are managed via environment variables.


AI Recommendations
BookNest uses the Groq API with the llama-3.3-70b-versatile model to generate personalized book recommendations. Users describe their mood or interest in natural language and receive 5 curated book suggestions instantly.

Deployment
The app is deployed on Railway with automatic deployments triggered on every push to the main branch. All secrets are stored as Railway environment variables and injected at runtime via Spring Boot's ${VARIABLE_NAME} syntax.

Author
Riya Bharti
https://github.com/riyabharti22
