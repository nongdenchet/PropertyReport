clean:
	- ./mvnw clean

build:
	- ./mvnw clean install -DskipTests && docker-compose build

run:
	- ./mvnw install -DskipTests && docker-compose up --build

test:
	- ./mvnw test
