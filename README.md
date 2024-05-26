# LOMS-ITEC3506
LAN Office Messaging System for ITEC3506 Summer

## Pre Requisites
- Java 17
- MySQL ([How to install MySQL?](https://dev.mysql.com/doc/mysql-installer/en/))

## MySQL Setup (Needed for the first time only)
Open a terminal (command prompt in Microsoft Windows) and open a MySQL client as a user who can create new users.

For example, on a Linux system, use the following command;
```
sudo mysql
```

To create a new database, run the following commands at the mysql prompt:

```
-- Creates a new Database
create database itec3506_summer24_loms;

-- Creates a dev user
create user 'loms_admin'@'%' identified by 'ProjectDev#2024';

-- Gives all privileges to the new user on the newly created database
grant all on itec3506_summer24_loms.* to 'loms_admin'@'%';
```

### Some security changes
When you are on a production environment, you may be exposed to SQL injection attacks. A hacker may inject DROP TABLE or any other destructive SQL commands. So, as a security practice, you should make some changes to your database before you expose the application to your users.

The following command revokes all the privileges from the user associated with the Spring application:
```
revoke all on itec3506_summer24_loms.* from 'loms_admin'@'%';
```

Now the Spring application cannot do anything in the database.

The application must have some privileges, so use the following command to grant the minimum privileges the application needs:
```
grant select, insert, delete, update on itec3506_summer24_loms.* to 'loms_admin'@'%';
```
Removing all privileges and granting some privileges gives your Spring application the privileges necessary to make changes to only the data of the database and not the structure (schema).

### Important

When you want to make changes to the database:

Regrant permissions.

Change the spring.jpa.hibernate.ddl-auto to update.

Re-run your applications.

Then repeat the two commands shown here to make your application safe for production use again.