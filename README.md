# To be done

**Prerequisites**
- Docker
- Java - 8
- Maven

**To run the tests**
- Pull juice shop using docker: ```docker pull bkimminich/juice-shop```
- Run juice shop using ```docker run --rm -p 3000:3000 bkimminich/juice-shop```
- Go to Zap folder
- Run the Zap  ```./zap.sh -daemon api-key=disabled```
- Run the tests using ```mvn clean test```