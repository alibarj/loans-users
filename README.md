# loans-users
## Context
Loans servicing is a banking software that manages loans issued by the bank. It controls everything related to the loan; the clients info, the bank accounts transactionns, etc.
For a summer internship, I was asked to create a web service that deals with the Loans servicing software ***users***. The web service creates, adds, updates and deletes users.

## The users
Every user must have a ***profile***. A profile defines a set of authorized actions on the Loans servicing software, these actions are called ***privileges***. A user may have more privileges than its profile defines. A user's profile has an expiration date, and privileges have expiration dates and level of power (how much control does the user have on that specific action).
In addition, a user also have basic info, such as id (username), password, name, departement, etc.

## Database design
For the database modeling, I used the [Merise](https://en.wikipedia.org/wiki/Merise) methodology.
### Conceptual model
![Conceptual model diagram](/assets/images/design-MCD.drawio.svg) 
### Logic model
![Logic model diagram](/assets/images/design-MLD.drawio.svg) 

Translation:
- Utilisateur : User
- Profil : Profile
- Evenement : Authorized action or privilege
- Contenir : Contain
- Effectuer : perform
- Appartenir : Belong
- Service : Department

The database is implemented in MySQL, implementation script is in /database/script.sql.

## Backend
The backend code is written in Java EE 8 and ran on apache tomcat 9. The app is intended to be a REST API, it's therefor composed of one ***servlet*** (/backend/UtilisateursServlet.java) whose job is to define the app's behavior when it recieves a request. The requests that the servelet deals with are of type HTTP, and use the methods GET, POST, PUT and DELETE methods to respectively retrive, add, update and delete data from the database. The behavior of the app is defined in 4 methods, doGet(), doPost(), doPut() and doDelete(), one of which will be called at each HTTP request, depending on the request's method.

![Servelet methods](/assets/images/servlet.PNG) 

### Database connectivity
Connection with the database is carried out by a JDBC driver. The connection to the database, and sql statements and executions are encapsulated using the DAO (Data Access Object) design pattern (/backend/dao). Data persistence is ensured with java beans (/backend/beans).

### Data exchange format
All data transactions via HTTP are in JSON format, this means that requests bodies must be JSON strings, and responses returned by the servelt are also JSON strings. For this purpose, I introduced the /backend/Serializable.java interface whose job is to serialize a Java object to a string format and deserialize a string format to a java object, and an implementation (/database/JsonSerializer.java) that serializes and deserializes to JSON format.
I adopted this approach to anticipate possible changes in data exchange formats.

### Architecture summary
![Servelet methods](/assets/images/design-Architecture.drawio.svg)

## Frontend
After successful testing with the Postman software, I built a frontend client for the API using HTML5, Bootstrap, and JavaScript. The client is a single-page interface that gives the ability to display all users in the database, add a new user, and delete an existing user (updating a user is not yet implemented).
The interface is not hosted in a server, it uses the Ajax technology to send requests and recieve responses from the api.
![Interface screenshot](/assets/images/interface_main.PNG)
![Interface screenshot](/assets/images/interface_consulter1.PNG)
![Interface screenshot](/assets/images/interface_consulter2.PNG)
![Interface screenshot](/assets/images/interface_ajouter1.PNG)
![Interface screenshot](/assets/images/interface_ajouter2.PNG)
![Interface screenshot](/assets/images/interface_ajouter3.PNG)

## Conclusion
This project was an opportunity for me to practice my software engineering skills such as Java, JavaScript, design patterns, etc. and to explore previously unknown technologies like RESTful apis, Json and Ajax. It evidently still needs a lot of work to be ready for production, but it was an enriching experience.
