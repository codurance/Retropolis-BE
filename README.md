# Retropolis-BE [![codurance](https://circleci.com/gh/codurance/Retropolis-BE.svg?style=shield)](https://github.com/codurance/Retropolis-BE) [![Known Vulnerabilities](https://snyk.io/test/github/codurance/Retropolis-BE/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/codurance/Retropolis-BE?targetFile=pom.xml) [![codecov](https://codecov.io/gh/codurance/Retropolis-BE/branch/master/graph/badge.svg)](https://codecov.io/gh/codurance/Retropolis-BE)

The back end of the Retropolis application. This is the final project for the apprentices Mar-May-2020.
The project is a remote tool to facilitate remote retrospectives.

## Teck Stack

The project is build with [Spring](https://reactjs.org/) and Java 11. It uses [JUnit](https://junit.org/junit5/) and [REST Assured](http://rest-assured.io/) for testing. 
The project follows the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).

For continuous integration it uses [CircleCI](https://circleci.com/) and for the deployment [AWS Elastic Beanstalk](https://aws.amazon.com/elasticbeanstalk/).

### Run it locally
 1) Clone the project on your local machine.  <br/>
                 `$ git clone https://github.com/codurance/Retropolis-BE.git`

 2) Navigate to the project folder and install the dependencies with the following command.  <br/>
                 `$ mvn install`
                 
 3) Run the application locally (the application can be accessed from [localhost:5000](http://localhost:3000/)) <br/>
                  `$ mvn spring-boot:run`

#### Run tests
You can run the tests by using `$ mvn test`.

#### Generate coverage report
You can produce code coverage report using the jacoco plugin `$ mvn jacoco:report`.


### Documentation
Retropolis-BE documentation is available [here](https://github.com/codurance/Retropolis-BE/wiki)
