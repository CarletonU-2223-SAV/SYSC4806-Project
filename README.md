# SYSC 4806 Project - Group 7


![CI Status](https://github.com/CarletonU-2223-SAV/SYSC4806-Project/actions/workflows/maven.yaml/badge.svg)

Chosen topic: Amazin Bookstore & FaaS, Microservices, Amazon Lambda

> Bookstore Owner can upload and edit Book information (ISBN, picture, description, author, publisher,...) and inventory. 
> User can search for, and browse through, the books in the bookstore, sort/filter them based on the above information. 
> User can then decide to purchase one or many books by putting them in the Shopping Cart and proceeding to Checkout. 
> The purchase itself will obviously be simulated, but purchases cannot exceed the inventory. 
> User can also view Book Recommendations based on past purchases. 
> This is done by looking for users whose purchases are most similar (using Jaccard distance: Google it!), 
> and then recommending books purchased by those similar users but that the current User hasn't yet purchased.

### Deadlines

- Milestone 1: March 8 2023
- Milestone 2: March 22 2023
- Group Presentation: March 26 2023
- Milestone 3: April 5 2023

### Team members
- Phuc La (101107588)
- Nicolas Tuttle (101105699)
- Robell Gabriel (101108508)

#### How to run the project
1. Package Maven
2. Run Sysc4806ProjectApplication.class

#### Milestone 1

In milestone 1, our team built the framework of the Amazin BookStore with many features implemented. Features created in this milestone: 
- Create new book for the store (upload new book to the store):
  - Bookstore owner or admin can add new book into the store.
- Edit book (update book information such as description and inventory):
  - Bookstore owner or admin can edit the information of the pre-existing book in the store by changing the description or update inventory manually.
- Delete book from the store page:
  - Bookstore owner or admin can delete a book from the store page, which completely removes it from the system.
- Adding books into shopping cart
  - Users can add book into their respective online shopping cart, and the book's inventory will decrease accordingly based on the number of books that the user added into their cart.
  - Naturally, the number of books that the user add to cart cannot exceed the inventory.
- Searching for book(s) based on ISBN, Publisher, Author and Title
  - All the searching is done with case-insensitive contains logic.
  - The only exception is the ISBN since it is unique for each book.

The class diagram for this milestone is as follows:

![Class Diagram](doc/class_diagram.png)

The database schema used for the Amazin Bookstore system is as follows:

![Database Schema](doc/database_schema.png)

The team's plan for the next milestone is:
- Implement the Checkout feature
- Adding recommendation purchase feature based on what the user purchased in the past
- Implement the features in the bookstore using the AWS Lambda function
- Create and change active users
