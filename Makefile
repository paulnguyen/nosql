
all: clean 

clean: 
	rm -rf build/*
	rm -f *.db

aspects:
	ajc -1.5 -inpath lib/jakarta-poi.jar:lib/junit.jar:lib/log4j.jar -sourceroots src -d build

java:
	ajc -1.5 -inpath lib/jakarta-poi.jar:lib/junit.jar:lib/log4j.jar -d build src/*.java

compile:
	javac -cp lib/jakarta-poi.jar:lib/junit.jar:lib/log4j.jar -d build src/*.java
	
vol:
	java -cp ./build:lib/aspectjrt.jar:lib/jakarta-poi.jar:lib/junit.jar:lib/log4j.jar Volume

run0:
	java -cp ./build:lib/aspectjrt.jar:lib/jakarta-poi.jar:lib/junit.jar:lib/log4j.jar SMImplVersion0

run1:
	java -cp ./build:lib/aspectjrt.jar:lib/jakarta-poi.jar:lib/junit.jar:lib/log4j.jar SMImplVersion1
	
run2:
	java -cp ./build:lib/aspectjrt.jar:lib/jakarta-poi.jar:lib/junit.jar:lib/log4j.jar SMImplVersion2
	
test1:
	java -cp ./build:lib/aspectjrt.jar:lib/jakarta-poi.jar:lib/junit.jar:lib/log4j.jar junit.textui.TestRunner TestAcceptanceBasic
	
test2:
	java -cp ./build:lib/aspectjrt.jar:lib/jakarta-poi.jar:lib/junit.jar:lib/log4j.jar junit.textui.TestRunner TestAcceptanceCrossFunctional
	
test3:
	java -cp ./build:lib/aspectjrt.jar:lib/jakarta-poi.jar:lib/junit.jar:lib/log4j.jar junit.textui.TestRunner TestAcceptanceOID
	
test4:
	java -cp ./build:lib/aspectjrt.jar:lib/jakarta-poi.jar:lib/junit.jar:lib/log4j.jar junit.textui.TestRunner TestStressLargeRecords
	
test5:
	java -cp ./build:lib/aspectjrt.jar:lib/jakarta-poi.jar:lib/junit.jar:lib/log4j.jar junit.textui.TestRunner TestXMLFileLoad
	
	
	
	
	
	