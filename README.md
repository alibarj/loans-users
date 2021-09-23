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
![Conceptual model diagram](/database/design-MCD.drawio.svg) 
### Logic model
![Logic model diagram](/database/design-MLD.drawio.svg) 

Translation:
- Utilisateur : User
- Profil : Profile
- Evenement : Authorized action or privilege
- Contenir : Contain
- Effectuer : perform
- Appartenir : Belong

##Backend
The backend code is written in Java EE 8 and ran on apache tomcat 9. The app is intended to be a REST API, it's therefor composed of one ***servlet*** (/backend/UtilisateursServlet.java) whose job is to define the app's behavior when it recieves a request. The requests that the servelet deals with are of type HTTP, and use the methods GET, POST, PUT and DELETE methods to respectively retrive, add, update and delete data from the database. The behavior of the app is defined in 4 methods, doGet(), doPost(), doPut() and doDelete(), one of which will be called at each HTTP request, depending on the request's method.

