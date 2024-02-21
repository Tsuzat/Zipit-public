# Use an official OpenJDK 17 runtime as a parent image
FROM openjdk
FROM maven

# Set the working directory in the container
WORKDIR /usr/src/app

# Copy the application JAR file into the container at the specified working directory
COPY . .

RUN mvn clean install

# Expose the port the app runs on
EXPOSE 8095

# Specify the command to run on container start
CMD ["java", "-jar","target/redirect-0.0.1-SNAPSHOT.jar"]
