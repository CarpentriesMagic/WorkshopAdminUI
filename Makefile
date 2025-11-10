MVN=mvn
PWD=$(shell pwd)

release:
ifndef rel
	echo "Please specify a release as rel=releasename"
else
	rm -r target/
	mvn package
	zip assets.zip workshopadmin.properties.bu assets\sqlite-jdbc-3.50.3.0.jar logging.properties HackMD_Templates/* schedules/* emptydatabase.sqlite
	mv target/dwa-jar-with-dependencies.jar target/dwa-${rel}.jar
	gh release create ${rel} target/dwa-${rel}.jar assets.zip --generate-notes --fail-on-no-commits
	rm assets.zip
endif
