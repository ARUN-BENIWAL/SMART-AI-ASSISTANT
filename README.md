# 🤖 Smart AI Assistant - Full Stack

A modern, feature-rich AI assistant web application with real-time web search, database integration, and persistent knowledge storage. Built with vanilla JavaScript, HTML5, and CSS3 for the frontend, designed to integrate with a Java Servlet backend and MySQL database.

## ✨ Features

### 🎯 Core Features
- **Intelligent Chat Interface** - Natural conversation with AI assistant
- **Real Web Search** - Integrated Wikipedia and DuckDuckGo search
- **Knowledge Base** - MySQL database for persistent storage
- **Smart Caching** - Local storage cache for faster responses
- **Reminders System** - Set and manage time-based reminders
- **Statistics Dashboard** - Track usage and system information

### 🌐 Web Search
- Automatic web search when knowledge base doesn't have answers
- Wikipedia REST API integration
- DuckDuckGo Instant Answer API
- Search results caching (7-day expiry)
- Toggle web search on/off
- Force search button for immediate web queries

### 💾 Database Integration
- MySQL backend for persistent storage
- Knowledge base management
- Reminder storage and retrieval
- CRUD operations via REST API
- Fallback to browser localStorage when offline

### 🎨 User Interface
- Modern, gradient design
- Responsive layout (mobile-friendly)
- Tab-based navigation (Chat, Knowledge, Reminders, Stats)
- Real-time connection status indicator
- Toast notifications
- Loading animations
- Message source badges (Database/Web)

## 🚀 Quick Start

### Prerequisites
- Web server (Apache Tomcat, etc.)
- MySQL Database
- Java Servlet container
- Modern web browser

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/smart-ai-assistant.git
cd smart-ai-assistant
```

2. **Set up MySQL Database**
```sql
CREATE DATABASE ai_assistant;

CREATE TABLE knowledge (
    id INT PRIMARY KEY AUTO_INCREMENT,
    topic VARCHAR(255) NOT NULL,
    information TEXT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE reminders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    message VARCHAR(255) NOT NULL,
    reminderTime BIGINT NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

3. **Configure Backend API**
- Update the `API_URL` in `index.html` (line 771):
```javascript
const API_URL = '/ai-assistant/api/assistant'; // Change to your servlet URL
```

4. **Deploy Backend Servlet** (Example structure)
```
/ai-assistant
  /api
    /assistant (POST/GET endpoint)
```

5. **Open in Browser**
```
http://localhost:8080/ai-assistant/index.html
```

## 📖 Usage

### Chat Commands

**Basic Queries**
```
User: What is artificial intelligence?
Bot: [Searches web and returns answer]

User: What time is it?
Bot: Current time is 10:30:45 AM

User: help
Bot: [Shows all available commands]
```

**Learning Mode**
```
User: learn Python is a high-level programming language
Bot: I've learned that Python is a high-level programming language 💾
```

**Ask Specific Questions**
```
User: ask who is Albert Einstein?
Bot: [Returns answer from knowledge base or web]
```

### Quick Commands
- **⏰ Time** - Get current time
- **📅 Date** - Get current date
- **🔍 Search AI** - Example web search
- **❓ Help** - Show all commands

### Knowledge Management
1. Navigate to **Knowledge** tab
2. Enter topic and information
3. Click "💾 Save to Database"
4. View, edit, or delete stored knowledge

### Reminders
1. Navigate to **Reminders** tab
2. Enter reminder message and date/time
3. Click "⏰ Save Reminder"
4. Receive browser notifications when due

## 🛠️ API Endpoints

### Required Backend Endpoints

**Get Knowledge**
```
GET /api/assistant?action=getKnowledge
Response: {
  "success": true,
  "data": [
    {
      "id": 1,
      "topic": "Python",
      "information": "Programming language",
      "timestamp": "2024-01-01T10:00:00"
    }
  ]
}
```

**Add Knowledge**
```
POST /api/assistant
Body: {
  "action": "addKnowledge",
  "topic": "Java",
  "information": "Object-oriented programming language"
}
Response: {
  "success": true
}
```

**Query Assistant**
```
POST /api/assistant
Body: {
  "action": "queryAssistant",
  "query": "What is AI?"
}
Response: {
  "success": true,
  "response": "Artificial Intelligence is..."
}
```

**Get Reminders**
```
GET /api/assistant?action=getReminders
Response: {
  "success": true,
  "data": [
    {
      "id": 1,
      "message": "Doctor appointment",
      "reminderTime": 1704110400000
    }
  ]
}
```

**Add/Delete Reminder**
```
POST /api/assistant
Body: {
  "action": "addReminder",
  "message": "Call dentist",
  "reminderTime": 1704110400000
}
```

## 🔧 Configuration

### Web Search APIs

**Wikipedia REST API**
- Endpoint: `https://en.wikipedia.org/api/rest_v1/page/summary/{query}`
- No API key required
- Rate limit: Respect User-Agent guidelines

**DuckDuckGo Instant Answer**
- Endpoint: `https://api.duckduckgo.com/?q={query}&format=json`
- No API key required
- Rate limit: Reasonable use

### Browser Storage

**localStorage Keys**
- `knowledgeBase` - Local knowledge items
- `webSearchCache` - Cached web search results (7-day TTL)

### Customization

**Change Colors**
```css
/* Primary gradient */
background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

/* Change to your brand colors */
background: linear-gradient(135deg, #your-color-1 0%, #your-color-2 100%);
```

**Adjust Cache Duration**
```javascript
// In checkLocalSearchCache() function (line ~952)
if (ageInDays < 7) { // Change 7 to your preferred days
```

## 📊 Features Breakdown

| Feature | Status | Description |
|---------|--------|-------------|
| Chat Interface | ✅ | Real-time messaging |
| Web Search | ✅ | Wikipedia + DuckDuckGo |
| Knowledge Base | ✅ | MySQL + localStorage |
| Reminders | ✅ | Time-based notifications |
| Statistics | ✅ | Usage tracking |
| Responsive Design | ✅ | Mobile-friendly |
| Offline Mode | ✅ | localStorage fallback |
| Toast Notifications | ✅ | User feedback |
| Connection Status | ✅ | Real-time monitoring |


### Chat Interface
Modern gradient design with message bubbles and source badges.

### Knowledge Base
Manage persistent knowledge with database integration.

### Reminders
Set time-based reminders with browser notifications.

### Statistics
Track usage metrics and system information.

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🐛 Known Issues

- Web search APIs may have rate limits
- Browser notifications require user permission
- localStorage has ~5-10MB limit per domain
- CORS may need configuration for external APIs

## 🔮 Future Enhancements

- [ ] Voice input/output
- [ ] Multi-language support
- [ ] Advanced NLP processing
- [ ] User authentication
- [ ] Cloud synchronization
- [ ] Mobile app version
- [ ] Plugin system
- [ ] Conversation export
- [ ] Theme customization
- [ ] Advanced analytics

## 🙏 Acknowledgments

- Wikipedia REST API
- DuckDuckGo Instant Answer API
- Icons from Unicode Emoji
- Gradient inspiration from [uiGradients](https://uigradients.com)

---

**Made with ❤️ by [ARUN BENIWAL]**

*If you find this project helpful, please give it a ⭐️!*# SMART-AI-ASSISTANT
