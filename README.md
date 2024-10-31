<div align="center">
  <img src="https://github.com/user-attachments/assets/358808cb-2882-41ce-9025-91dcd6a2e898"/>
</div>

___
# 📋Description
This is a web application that allows you to create matches, update their score and view the history of finished matches.

# 🛠️Libraries and technologies
- JakartaEE
- H2 database
- Hibernate
- HikariCP
- Lombok
- JUnit5
- JSP, CSS, JSTL
- Maven
- Tomcat

# 🎾Features and interface

## 🗏Home page
<div align="center">
  <img src="https://github.com/user-attachments/assets/0b3dcb99-d0ed-442b-85ab-16fc349234d7" style="border: 2 solid red" width="500px"/>
</div>

## 🗏New match page

Page where user can enter the names of the players and start the match

<div align="center">
  <img src="https://github.com/user-attachments/assets/c23acec1-4ad2-4e6b-9394-db95542e6c04" width="500px"/>
</div>

## 🗏Match score page

Page where user clicks on the buttons to win points for a specific player. After which the match score is updated

<div align="center">
  <img src="https://github.com/user-attachments/assets/211c9296-b403-492a-99af-4afcfda65b66" width="500px"/>
</div>

After one of the players wins, the results of the match are displayed

<div align="center">
  <img src="https://github.com/user-attachments/assets/5eca0a4f-22b3-459e-8dc2-2a9542efbbdd" width="500px"/>
</div>

## 🗏Matches page

A page where user can view the history of all matches and find matches in which a certain player participated

<div align="center">
  <img src="https://github.com/user-attachments/assets/90962c35-715e-4f7a-95ba-6f50fc3dd105" width="500px"/>
</div>

# 🖥️How to run project locally
1. Install Java Development Kit 22+ ver.
2. Install **Tomcat 10**
3. Clone repository.
```
git clone https://github.com/red-eyed-99/tennis-scoreboard.git
```
4. In the cmd go to the folder with the cloned repository and run
```
mvnw.cmd install
```
5. Take **tennis-scoreboard.war** from **target** folder and put it in Tomcat webapps folder.
6. Run tomcat, web application should be available at **http://localhost:8080/tennis-scoreboard**
