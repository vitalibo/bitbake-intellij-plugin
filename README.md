# BitBake IntelliJ Plugin


## Build Using Official Gradle Container

This mounts the current directory into the container, so a `.gradle` cache will be created for quick reruns.

    docker run --rm -it -u gradle -v "$PWD":/home/gradle/project -w /home/gradle/project gradle:latest gradle buildPlugin
