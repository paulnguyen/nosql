all: clean

clean: 
	find . -name "*.class" -exec rm -rf {} \;
	rm -rf build/*
	rm -f *.db


# Gradle: 		https://guides.gradle.org/creating-new-gradle-builds/
# CircleCI:		https://circleci.com/docs/2.0/language-java/

gradle-init:
	gradle init

gradle-build:
	gradle build -x test 

gradle-test:
	gradle test

jar: gradle-build
	gradle shadowJar

run: 
	java -cp build/libs/nojava-all.jar nojava.SMImplVersion2


###
### Old School Unit Testing & Debugging (Not Using Gradle)
###

aspects:
	ajc -1.5 -inpath libs/jakarta-poi.jar:libs/junit.jar:libs/log4j.jar -sourceroots src/main/java -d build

java:
	ajc -1.5 -inpath libs/jakarta-poi.jar:libs/junit.jar:libs/log4j.jar -d build src/main/java/nojava/*.java

compile-tests:
	javac -cp ./build:libs/jakarta-poi.jar:libs/junit.jar:libs/log4j.jar -d build src/test/java/nojava/*.java
	
vol:
	java -cp ./build:libs/aspectjrt.jar:libs/jakarta-poi.jar:libs/junit.jar:libs/log4j.jar nojava.Volume

run0:
	java -cp ./build:libs/aspectjrt.jar:libs/jakarta-poi.jar:libs/junit.jar:libs/log4j.jar nojava.SMImplVersion0

run1:
	java -cp ./build:libs/aspectjrt.jar:libs/jakarta-poi.jar:libs/junit.jar:libs/log4j.jar nojava.SMImplVersion1
	
run2:
	java -cp ./build:libs/aspectjrt.jar:libs/jakarta-poi.jar:libs/junit.jar:libs/log4j.jar nojava.SMImplVersion2
	
test1:
	java -cp ./build:libs/aspectjrt.jar:libs/jakarta-poi.jar:libs/junit.jar:libs/log4j.jar junit.textui.TestRunner nojava.TestAcceptanceBasic
	
test2:
	java -cp ./build:libs/aspectjrt.jar:libs/jakarta-poi.jar:libs/junit.jar:libs/log4j.jar junit.textui.TestRunner nojava.TestAcceptanceCrossFunctional
	
test3:
	java -cp ./build:libs/aspectjrt.jar:libs/jakarta-poi.jar:libs/junit.jar:libs/log4j.jar junit.textui.TestRunner nojava.TestAcceptanceOID
	
test4:
	java -cp ./build:libs/aspectjrt.jar:libs/jakarta-poi.jar:libs/junit.jar:libs/log4j.jar junit.textui.TestRunner nojava.TestStressLargeRecords
	
test5:
	java -cp ./build:libs/aspectjrt.jar:libs/jakarta-poi.jar:libs/junit.jar:libs/log4j.jar junit.textui.TestRunner nojava.TestXMLFileLoad
	
