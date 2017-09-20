AHN pointcloud viewer web service
=================================

[![Build Status](https://travis-ci.org/NLeSC/ahn-pointcloud-viewer-ws.svg)](https://travis-ci.org/NLeSC/ahn-pointcloud-viewer-ws)
[![SonarCloud Gate](https://sonarcloud.io/api/badges/gate?key=nl.esciencecenter.ahn:ahn-pointcloud-viewer-ws)](https://sonarcloud.io/dashboard?id=nl.esciencecenter.ahn:ahn-pointcloud-viewer-ws)
[![SonarCloud Coverage](https://sonarcloud.io/api/badges/measure?key=nl.esciencecenter.ahn:ahn-pointcloud-viewer-ws&metric=coverage)](https://sonarcloud.io/component_measures/domain/Coverage?id=nl.esciencecenter.ahn:ahn-pointcloud-viewer-ws)

Backend for the [ahn-pointcloud-viewer](https://github.com/NLeSC/ahn-pointcloud-viewer) web application.

Has the following api endpoints:
1. Compute the number of points in a selected area
2. Create a laz file of the selection and send a mail to end-user with download location of the generated laz file

It uses a PostGIS database to approximate number of points in selected area.

Development
-----------

First create config file `config.yml`, use `config.yml-dist` as an example.
Se `src/main/java/nl/esciencecenter/ahn/pointcloud/db/PointCloudStore.java` for instructions to create an example PostGIS database.

Run web service:
````
./gradlew run 
````

Perform tests with test and coverage reports in `build/reports` directory.
````
./gradlew test jacocoTestReport
````

To open in an IDE like Eclipse or Intellij IDEA, create project files with `./gradlew eclipse` or `./gradlew idea` respectively.

### Manual testing

1. Create a database.

1.1 Start db

````
docker run -e POSTGRES_USER=ahn -e POSTGRES_PASSWORD=mysecret -p 5432:5432 -d mdillon/postgis
````

1.2 Fill it with test dataset

````
psql -h localhost -p 5432 -U ahn ahn < src/test/resources/test.sql
````

2. Create a executable to run to create laz files. For example:

````
#!/bin/bash
echo `date -Iseconds`: $@ >> ahn-slicer.log
````

3. Edit `config.yml` to set database and executable
4. Start web service `./gradlew run`
5. Test with a http client

````
virtualenv env
. env/bin/activate
pip install httpie
http -pHBhb http://localhost:8080/size left:=125932.60 bottom:=483568.840 right:=125942.60 top:=483588.840
http -pHBhb http://localhost:8080/laz left:=125932.60 bottom:=483568.840 right:=125942.60 top:=483588.840 email=someone@example.com
````

Build
-----

````
git clone git@github.com:NLeSC/ahn-pointcloud-viewer-ws.git
./gradlew build
````

The distribution is in the `build/distributions` directory.

Deployment
----------

1. Unpack distribution and cd to it.
2. Create config file, use `config.yml-dist` as an example.
3. Run it

````
bin/ahn-pointcloud-viewer-ws server config.yml
````

A web service will be started on http://localhost:8080

Generate web api documentation
------------------------------

API documentation is written in https://apiblueprint.org/ format.

API documentation can be preview with:
````
sudo npm install -g aglio
aglio -i apiary.apib -s
````

Database and create_user_file
-----------------------------

This web service relies on a Database which contains 
a table with the extents of the files in the AHN2,
a table with the extents of the files in the octree structure and
a third table that contans, for each level of the octree, the ratio of 
points in the level diviede by the total number of points .

To fill in a PostgreSQL database with the required information use the scripts
`fill_db_raw.py` and `fill_db_potree.py` from the 
[Massive-PotreeConverter repository](https://github.com/NLeSC/Massive-PotreeConverter).

The file `create_user_file.py` in `main/python` is used to create a user file
from a selected region and it uses the described PostgreSQL database. The scripts uses LAStools.
