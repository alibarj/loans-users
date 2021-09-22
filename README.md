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


