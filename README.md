# storytel-rest-api

Design and build a RESTful API to work as a basic message store.

It should include the following features:
  - a client can create a message in the service
  - the same client can modify their message
  - the same client can delete their message
  - a client can view any message available

Use an in-memory solution for storing any data for this service. Consider the appropriate HTTP verbs, headers and responses to use.

You should also include tests to assert the correctness of your solution.

The complete source code should be easy to build and run, provide instructions for how to do so in a README file.
Send us the source code either in a zip file, or via a public git repo.
Get back to us when you've finished and we'll arrange the next steps; and if you have any questions or need clarification don't hesitate to contact us

# Build instructions

N.B.! You need Java 11, git, maven to use this.

1. Clone the github repository
> git clone https://github.com/kizouker/storytel-rest-api.git
2. Build the project using maven
> mvn package
(A jar war will be placed in
<./target/RestServiceAPI-1.0-SNAPSHOT-spring-boot.war>)
3. Start the server/Rest api ( the junit tests will also be run)
>  java -jar ./target/RestServiceAPI-1.0-SNAPSHOT-spring-boot.war

4. You can find the documentation browsing to:
http://localhost:8080/restservice/swagger-ui.html#/
