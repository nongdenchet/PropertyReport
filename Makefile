clean:
	- mvn clean

build:
	- mvn clean
	- mvn install -DskipTests
	- docker-compose build

run:
	- mvn install -DskipTests
	- docker-compose up --build

test:
	- mvn test

release:
	- make build
	- heroku container:push web
	- heroku container:release web
