# 📎 Zipit - A URL Shortner

<!--toc:start-->
- [📎 Zipit - A URL Shortner](#📎-zipit-a-url-shortner)
  - [Components](#components)
    - [Frontend](#frontend)
    - [Backend](#backend)
    - [Redirect](#redirect)
  - [Run Locally](#run-locally)
    - [Running the frontend](#running-the-frontend)
    - [Running the Backend](#running-the-backend)
    - [Running the Redirect](#running-the-redirect)
  - [Expectations from this Project](#expectations-from-this-project)
    - [What can you learn?](#what-can-you-learn)
    - [Why am I opensourcing it?](#why-am-i-opensourcing-it)
    - [What not to do?](#what-not-to-do)
  - [Contribution](#contribution)
<!--toc:end-->

This project was made for **fun** and **learning**. 
The intention for this project was to learn `Java`, `SpringBoot`, `Microservices`, `JWT Auth`, `svelte`, `Docker` etc.
This project is a fully functioning end to end URL Shortner. 

## Components
The Project has follwing 3 major parts:

### Frontend
The frontend is written in [`svelte`](https://svelte.dev/) with `sveltekit`.
It uses [`TypeScript`](https://www.typescriptlang.org/) as a primary language.
For the UI, pure CSS has beed used with some `AnimatedIcons` with as [`lottiefiles`](https://lottiefiles.com/).
The Icons has been animated using [`Figma`](https://www.figma.com/) and [`LottieLab`](https://www.lottielab.com/).

### Backend
The backend is a simple [`Springboot`](https://spring.io/) application with [`MongoDB`](https://www.mongodb.com/).
For the user authentication, [`JWT`](https://jwt.io/) has been implemented.
Backend has features like `Email varification`, `password reset with email`, `user authentication` etc.
Backend spawns a separate thread for emails.

### Redirect
It's a very simple spring boot `microservice`. It just takes the shorten url and redirects to original url.
It does handle URL conficts e.g. URL not found or URL is expired.

## Run Locally
Before you move furture, I presume that you have basic knowledge of programming.
To run this project locally, you need to have followings
- [Java 17](https://jdk.java.net/17/)
- MongoDB Uri (could be local instance or deployed)
- [Nodejs](https://nodejs.org/en) and npm
- Gmail and App Password (for SMTP) [follow this link](https://support.google.com/mail/answer/185833?hl=en)
- IntelliJ IDEA (Optional, Java just works better in IntelliJ)
- Docker (Optional, if you want to use it)

### Running the frontend

use follwing commands
```sh
npm install
npm run dev -- --open
```
> Note that if you change the backend port, then you may need to make few changes in .env file.
If you do not change any port in Backend and Redirect services, then you are good to go.

> Info: Note the URL of frontend from your browser, you may need it later.

### Running the Backend

Add the MongoDB URI, gmail username and password in `application.properties`
```properties
server.port=8090
spring.data.mongodb.uri=mongodb+srv://{username}:{password}@{host}/?retryWrites=true&w=majority
spring.data.mongodb.database=database_name
# Email
spring.mail.host=smtp.gmail.com
spring.mail.username=myemail@gmail.com
spring.mail.password=mypassword
spring.mail.port=587
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
```
> Note: We'll use port 8090 for backend. You can use any port, but all the steps will be described with posrt 8090 in mind for backend

In `org.example.backend.servies.auth.LilyAuthService.java`, look into line 31.
If you have kept the port as 8090 then, do not change anything, otherwise change the port accordingly.

In `org.example.backend.controller.auth.LilyAuthController.java`, look into line 35. Update it with URL of frontend you running locally.

> Warning Keep the usage of `/` in mind, if may lead to errors. Refer to already present url. If you have changed anything in project,
then most probably, you may not need to change anything.

Run the backend using intelliJ IDEA or type in the following command in terminal
```sh
mvnw sprint-boot:run
```

### Running the Redirect

Update the `application.properties`, run it on port `8095` and in `org.example.redirect.controller.LilyRedirectController.java`, line 39, put the `error` url of frond-end.


## Expectations from this Project
This project is a very simple implementation for a URL shortner. We're not using `redis`, `kafka`, `Zoo Keeper` etc,
as it will lead to an over engineering of a simple application. 
This project is for best for learning purpose, If you want to make it scallable then you may need to
"over engineer" it. It is recommended to try to implement the solution by yourself and then take
this project as a reference for better learning. 

### What can you learn?
I made this project for fun and learning. I enjoyed the whole process and the outcome. Some of the
key notes that I learnt are:

- **AnimatedIcons with Lottiefiles** - Lotteifiles are amazing. I decided to make some icons in `Figma` and then
I animated them using `LottieLab`. The implementation of those `Lottifiles` as the fun part.

- **Auth With JWT** - Implementation of JWT in backend and how to utilies it the JWT in frontend.
For example, in this project, I have used `LocalStorage` to store JWT which is used for further 
communication with backend. Can you do a different implementation?

- **Svelte and UI** - I made the frontend in `svelte` because I was very impressed with this framework.
Although, this project does not utilies the full potential of `svelte` but it is a very good staring point and beyond.

- **Spring Boot** - The backend and redirect service uses spring boot. The spring security, project structure and implementations could be an amazing learning.

- **Docker and Deployment** - The backend and redirect contains `Dockerfile`. It is highly recommended to go throught them
and try to come up with your own `Dockerfiles`

### Why am I opensourcing it?
Throughout the journey of this project, I learnt a lot. I gathered many scattered information and compiled them into this project.
The main thought is to provide new learners a project they can get inspired from. The main goal of opensourcing this project is 
to give the new learners another resource.

### What not to do?
It is recommended to fork this project, learn and make changes. Do not just copy this project to
put it in your `resume`. Try to build a different project by learning basics from this project.


## Contribution
The main purpose of this project is to provide the learning or reference resources for new learners. 
It is recommended that you should experiment in your own fork and **`not`** to `open a pull request` 
but if you implemented a feature or made any improvements, then **`pull requests are welcomed`**. 

