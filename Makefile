clean:
	./gradlew clean

build:
	./gradlew clean build

start-dev:
	./gradlew bootRun --args='--spring.profiles.active=dev'

start-prod:
	./gradlew bootRun --args='--spring.profiles.active=prod'

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