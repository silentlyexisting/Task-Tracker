clean:
	./gradlew clean

build:
	./gradlew clean build

start-dev:
	APP_ENV=dev ./gradlew bootRun

start-prod:
	APP_ENV=prod ./gradlew bootRun

install:
	 ./gradlew installDist

lint:
	./gradlew checkstyleMain checkstyleTest

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

check-updates:
	./gradlew dependencyUpdates

migrations:
	./gradlew diffChangeLog

make api-doc:
	gradle clean generateOpenApiDocs