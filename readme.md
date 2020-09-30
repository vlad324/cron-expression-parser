### Cron Expression Parser

Requisition: Java 11+

To generate artifact execute in root project directory:

`./gradlew clean jar`

Jar file will be placed in `./build/libs` folder.

To parse cron expression: 

``java -jar ./build/libs/cron-expression-parser.jar "45-50 0-2,21-23 1-10/2 */5 1,2,3,4 /usr/bin/find"``