# CovIDetect

CovIDetect is a dashboard application for universities that aims to facilitate the process of managing covid cases and provide statistical insight on attandance rates as well as covid cases.



![alt text](https://i.postimg.cc/cHGQYgGt/main-Screen.png)


## Functionalities
The app provides 3 main functionalities:
* #### Room Visualization
* #### Update Covid Status (Manually)
* #### Statistical Analysis

## Room Visualization

This functionality provides the user with a visualization of a room and its covid/possible cases.

![alt text](https://i.postimg.cc/6QfQjyGv/room-Visualization.png)

The user can select a room, the date at which they wish to see the state of the room and the hours that a lecture occured (HourSpan).

They can also click on the Student ID List button and see all the students that were sitting as well as their health status (covid case, possible case, healthy).

![alt text](https://i.postimg.cc/HWhkhGjB/student-List.png)

## Update Covid Status (Manually)

This functionality provides the user with the ability to manually update a student's health status to a covid case.

![alt text](https://i.postimg.cc/mkvR2sH4/update-Covid-Status.png)

The user can enter a student's ID and pick the date at which the student was tested and turned out to positive.

The system will automatically update the status of the student and set the possible cases appropriately, two days before the date, for all rooms at which the student may have sat.

## Statistical Analysis

This functionality provides the user with the ability to visualize statistical data of attendance rates and covid cases.

### This data can be shown:

#### ‣ Yearly

#### ‣ Monthly

#### ‣ Daily

#### ‣ By Hour

#### ‣ By Professor


![alt text](https://i.postimg.cc/fWpMvxgP/statistical-Analysis.png)


The user can select a specific range of dates (From, To) for which they wish to see statistical data for.

They can also select the data category (Attendance Rates, Covid Case Stats) as well as an additional statistical method (in this version, only standard deviation is available).

# Setup
This version of the software is available only for Windows platforms that have Java installed. Linux versions will be released in the future.

## Requirements
This project requires:
* Java SE Development Kit 18 which can be found here: https://www.oracle.com/java/technologies/javase/jdk18-archive-downloads.html

* JavaFX which can be found here: https://openjfx.io/

#### No additional files are needed to run the app. 
#### However, an academic email is required to log in (@uom.edu.gr, @uom.gr in this case).
#### In case there are problems with the academic email showing up as non existing (problem with the free email verification api (https://mailboxlayer.com/) or the user does not have an academic email, they can use this email: ics21056@uom.edu.gr

## Running the app
In the executable file folder, there is a CovIDetect.jar file which is all what's needed.

* Note that the folders and files of the app during execution are created at the same path that the .jar file is, so it is advised that the .jar be put in a folder.

Running:
```java
java -jar -Xmx220M CovIDetect.jar
```
will execute the app and is all that is needed. 

The extra argument ```-Xmx220M``` is needed due to the memory increasing over every Service that is executed.

The JVM, while considering some objects eligible for garbage collection, doesn't give back the memory to the OS as it does not deem it necessary (probably because they are Services and it thinks that they might be used again). 
No "OutOfMemoryError" occured using the extra max heap size argument

## Importing project on IDE
This project is an IntelliJ IDEA JavaFX project.