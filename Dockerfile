From openjdk:8
copy ./target/bank.jar bank.jar
CMD ["java", "-Dspring.profiles.active=docker", "-jar","bank.jar"]