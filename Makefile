build:
	- mvn clean
	- mvn install -DskipTests
	- docker-compose build

run:
	- mvn install -DskipTests
	- docker-compose up --build

test:
	- mvn test
