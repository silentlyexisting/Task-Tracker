start:
	APP_ENV=dev gradle bootRun

clean:
	./gradlew clean

build:
	./gradlew clean build

install:
	 ./gradlew installDist