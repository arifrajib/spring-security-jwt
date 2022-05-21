Spring Security JWT token based authentication
=====================

Demonstrating how to implement JWT Authentication in Spring Boot Application using Spring Security  


## Run project

```
$ git clone https://github.com/arifrajib/spring-security-jwt.git
$ cd spring-security-jwt/
$ ./mvnw spring-boot:run
```

## Usages

1. **Register User** : */api/auth/register* 
2. **Login** : */api/auth/login*
3. **User details** : */api/user/details*


## Project dependency

* spring-boot-starter-web 
* spring-boot-starter-security 
* spring-boot-starter-data-jpa 
* h2 database
* spring-boot-devtools
* lombok
* java-jwt


## Behind the Scenes: Theory

### Components:

1. **Authentication Filter** : It is a **Filter** in the **FilterChain** which detects an authentication attempt and forwards it to the **AuthenticationManager**.

2. **Authentication** : This component specifies the type of authentication to be conducted. Its is an interface. Its implementation specifies the type of Authentication. For example, **UsernamePasswordAuthenticationToken** is an implementation of the **Authentication** interface which specifies that the user wants to authenticate using a username and password. Other examples include **OpenIDAuthenticationToken**, **RememberMeAuthenticationToken**.

3. **Authentication Manager** : The main job of this component is to delegate the *authenticate()* call to the correct **AuthenticationProvider**. An application can have multiple **AuthenticationProvider** s, few of which are **DaoAuthenticationProvider**, **LdapAuthenticationProvider**, **OpenIDAuthenticationProvider**, etc. The Authentication Manager decides which Authentication Provider to delegate the call to by calling the *supports()* method on every available **AuthenticationProvider**. If the *supports()* method returns true then that **AuthenticationProvider** supports the **Authentication** type and is used to perform authentication.

4. **Authentication Provider** : It is an interface whose implementation processes a certain type of authentication. An **AuthenticaionProvider** has an *authenticate* method which takes in the *Authentication* type and performs authentication on it. On successful authentication, the **AuthenticationProvider** returns back an **Authentication** object of the same type that was taken as input with the *authenticated* property set to *true*. If authentication fails, then it *throws* an *Authentication Exception*. 
   
   In most apps, we perform username and password authentication and therefore, before performing authentication we have to fetch the **UserDetails** (username / email, password, roles, etc..) from a *“datasource”* such as a database with the help of an **UserService**, and then authenticate the provided data against the actual data. ie. **DaoAuthenticationProvider** (is an implementation of the **AuthenticationProvider** interface which mainly deals with username password authentication)

5. **UserDetailsService** : This is a service which is responsible for fetching the details of the user from a *“datasource”*, most likely a database using the *loadUserByUsername(String username)* methods which takes in the username as a parameter. It then returns a **UserDetails** object populated with the user data fetched from the datasource (database). The three main fields of an UserDetails object are username, password and the roles/authorities.


#### Authentication Flow 

* Step 1 : When the server receives a request for authentication, such as a login request, it is first intercepted by the *Authentication Filter* in the *Filter Chain*.

* Step 2 : A **UsernamePasswordAuthenticationToken** is created using the username and password provided by the user. As discussed above, the **UsernamePasswordAuthenticationToken** is an implementation of the **Authentication** interface and used when a user wants to authenticate using a username and password.

* Step 3 : The **UsernamePasswordAuthenticationToken** is passed to the **AuthenticationManager** so that the token can be authenticated.

* Step 4 : The **AuthenticationManager** delegates the authentication to the appropriate *AuthenticationProvider*. As dicussed before this is accomplished by calling the *supports()* method on the *AuthenticationProvider*.

* Step 5 : The **AuthenticationProvider** calls the *loadUserByUsername(username)* method of the *UserDetailsService* and gets back the **UserDetails** object containing all the data of the user. The most important data is the password becuase it will be used to check whether the provided password is correct. If no user is found with the given user name, a **UsernameNotFoundException** is thrown.

* Step 6 : The **AuthenticationProvider** after receiving the **UserDetails** checks the passwords and authenticates the user. If the passwords do not match it throws a *AuthenticationException*. However, if the authentication is successful, a **UsernamePasswordAuthenticationToken** is created, and the fields *principal*, *credentials*, and *authenticated* are set to appropriate values. Here *principal* refers to your *username* or the *UserDetails*, credentials refers to *password* and the authenticated field is set to *true*. This token is returned back to the **AuthenticationManager**.

* Step 7: On successful authentication, inside the **SecurityContextHolder** the **SecurityContext** is updated with the details of the current authenticated user. *SecurityContext* can be used in several parts of the app to check whether any user is currently authenticated and if so, what are the user’s details.



## License

This repository is open-sourced software licensed under the [MIT license](http://opensource.org/licenses/MIT)
