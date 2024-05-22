# Marujo - The Hyper Blog

![Java](https://img.shields.io/badge/Java-F9981D?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/SpringBoot-6DB5D?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/SpringSecurity-6DB33D?style=for-the-badge&logo=springsecurity&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)
![BootStrap](https://img.shields.io/badge/Bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)

## Overview
Blog Central is a platform where visitors can read and search blogs, and registered users can write, edit, and comment on blog posts.

## Technologies Used
- Java: For the back-end logic.
- Spring Boot: For building the application and managing dependencies.
- Thymeleaf: For server-side rendering of HTML templates.
- Bootstrap: For responsive design and styling components.
- Spring Security: For managing authentication and authorization.

## Users
- **Visitors**
  - Read and search blogs.
  - Register and log in.
    
- **Registered Users**
  - Read and search blogs.
  - Log out, edit user details, post blogs, and comment on posts.

## Main Pages
- **Blogcentral.html**
  - **Sort Function**: Sort by newest, oldest, or most popular.
  - **Show Six Results**: Display the six most recent or search results with links to the author’s blog home and detail page.
  - **Show More Results**: Navigate through results (next, previous, last, first six).

- **Bloghome.html**
  - Author’s mini profile (name and avatar).
  - All blog posts by the author with sorting function.
  - Create new blog post (for the author).

- **BlogPostDetail.html**
  - Full blog post, like button, and comments.
  - **For logged-in users**: Submit comment.
  - **For the author**: Edit, delete post, delete comments.
  - **For commentors**: Edit, delete comment.
    
- **Header**
  - Log-in, sign-up, update profile, and search functions.

- **Side Bar**
  - Displays logged-in users’ names and avatars, number of online users, and total visitors.

## Form Pages

- **Log-in Page**
  - Username and password fields.
  - Additional features: Back, keep me logged in, sign-up link.
  - **Security**: Logged-in users cannot access.

- **Sign-up Page**
  - Form fields: First name, last name, username, email, street, house no., city, ZIP, password, retype password.
  - Additional features: Log-in link, optional email validation, password restrictions, avatar upload.
  - **Security**: Logged-in users cannot access.

- **UserDetailsPage.html**
  - Edit profile (except username).
  - Delete profile and log out.

## Features
- **Sorting and Display**
    - Consistent display and sorting (newest, oldest, most popular) of blog posts on Blogcentral.html and Bloghome.html.
      
- **Blog Post Management**
  - Create, edit, and delete blog posts by authors.
  - Commenting system with edit and delete options for authors.

- **Search Function**
  - Searches by username, tags, and title.

- **Authentication**
  - Log-in, sign-up, and profile update functionalities with “keep me logged in” option.

- **Real-time Updates**
  - Sidebar shows real-time data on logged-in users, online users, and total visitors.

## Security
  - Access restrictions on log-in and sign-up pages for logged-in users.
  - Edit and delete operations restricted to respective authors.
    
## Optional Features
  - Email validation, password restrictions, and avatar upload.

## Conclusion
Blog Central offers a streamlined experience for reading, writing, and managing blog posts with robust security and real-time updates.
