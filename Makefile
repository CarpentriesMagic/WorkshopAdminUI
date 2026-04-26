MVN=mvn
PWD=$(shell pwd)

release:
ifndef rel
	@echo "Please specify a release as rel=releasename"
else
	@echo $(rel) > release.txt
	@sed -i "18s|#####|$(rel)|" src/main/java/uk/ac/ncl/dwa/view/MainFrame.java
	@rm -rf target/
	@$(MVN) package
	@zip assets.zip workshopadmin.properties.bu assets/sqlite-jdbc-3.50.3.0.jar logging.properties HackMD_Templates/* schedules/* emptydatabase.sqlite
	@mv target/WorkshopAdmin-jar-with-dependencies.jar target/WorkshopAdmin.jar
	@gh release create $(rel) target/WorkshopAdmin.jar assets.zip --generate-notes -F assets/Release_Notes.md
	@rm assets.zip
	@sed -i "18s|${rel}|#####|" src/main/java/uk/ac/ncl/dwa/view/MainFrame.java
	@rm release.txt
endif