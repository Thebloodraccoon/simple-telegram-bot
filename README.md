# Simple Telegram Bot

A Spring-based Telegram bot implementation with a two-menu system and customizable keyboard interface.

## Requirements

- Java 11 or higher
- Spring Boot 2.x
- Maven or Gradle
- Telegram Bot API credentials

## Setup

1. Register a new bot with [@BotFather](https://t.me/botfather) on Telegram and get your bot token
2. Configure your application properties:

```properties
bot.name=YourBotName
bot.token=YourBotToken
```
or configure .env file
```properties
BOT_NAME=YourBotName
BOT_TOKEN=YourBotToken
```
3. Build the project using Maven or Gradle
4. Run the application

## Docker Deployment
You can also run this application using Docker:

```properties
docker-compose up --build
```