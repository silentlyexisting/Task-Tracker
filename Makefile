start:
	APP_ENV=dev gradle bootRun

clean:
	./gradlew clean

build:
	./gradlew clean build

install:
	 ./gradlew installDist

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

check-updates:
	./gradlew dependencyUpdates

make api-doc:
	gradle clean generateOpenApiDocs