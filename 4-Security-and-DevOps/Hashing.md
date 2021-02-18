# Hashing
## What is Hashing
**Hashing**  is the process of generating a unique value (hash) for a given  _text_,  _string_, or  _numeric_  input (key). The generated value (called a  _hash_) itself could be either a  _text_,  _string_, or  _numeric_, which depends upon the underlying  **Hash**  function.

> A  **Hash**  function is a one-way mathematical function which is used to generate a unique value for a given input.

The two essential properties of hashing are:
-   Hashing must be irreversible
-   Each input should have a unique output (or, practically, as close to unique as possible)

A scenario, when a hash function gives the same output for different inputs, is called a  **collision**.

### Irreversible vs. Reversible functions
Let there be a function  _f_, which can generate unique hash values for a given set of input. Another function  _g_  can get the original value back if the hash value is given as input. In such a case, the function  _f_  would be called  **reversible**, as we can get the original value back. Hence,  _f_  cannot be used as a Hash function.

> The hashes should be irreversible, so one cannot compute the input given the output.

### Theoretical Example
Let's look at a simple example of hashing. Suppose that a user has the following password:
-   `passw0rd!`  - If we directly store this text in the database, we will have a major security vulnerability.

So instead, we can first run the password through a one-way function that produces a jumbled up piece of text (which has no obvious discernible relationship to the original password). That jumbled up piece of text is the  _hash_, and it might look something like this:
-   `passw0rd!`  → hash function →  `@kdF3lkAWoLA`

So when the client interacts with the server, rather than directly sending the password, the client can instead send the hash:
-   `passw0rd!`  → hashing function →  `@kdF3lkAWoLA`  → stored in database

This way, if someone gains access to the database, they will still not have access to the plain-text password.

### How to Pick a Good Hash Function
A good hash function needs to be efficiently computable, so it needs to be reasonably fast. It needs to be uniform, which means given an input the output needs to be as unique as possible. In other words, a low number of collisions exists. A given output should give absolutely no indication of its input. Inputs should be effectively random and uniformly distributed. Changing "cat" to "bat" should yield unpredictable results (this is known as the avalanche property)

### Some famous Hashing Algorithms
There are many hashing algorithms prevalent in the industry.

-   **MD5**: The MD5 Message-Digest Algorithm is a hash function that accepts an input message of any length, and correspondingly produces a 128-bit (16-byte) hash value. Mostly, MD5 is used to verify data integrity. It was proposed by Ronal Rivest in 1992, as specified in  [RFC 1321](https://tools.ietf.org/html/rfc1321). **MD5 is comparatively unsafe, as it might get reversed by using brute-force-attack.** Also, the **chances of collision are very high in MD5**. For non-critical applications, MD5 can be a good choice as it is computationally faster than other algorithms.  
      
    
-   **SHA**: The SHA (Secure Hash Algorithm) is a set (SHA-0, SHA-1, SHA-2, and SHA-3) of cryptographic hash functions developed by the National Institute of Standards and Technology (NIST). In comparison to MD5, SHA generates secure hashes. SHA-1 is a 160-bit hash function. SHA-2 is further of two types: SHA-256 and SHA-512. SHA-256 is a 256-bit hash function that provides 128 bits of security in the case of collision attacks, while SHA-512 is a 512-bit hash function is designed for 256 bits of security. SHA-3 supports the same hash lengths as SHA-2. Chances of collision are high in SHA as well, but lesser than MD5. Thus, SHA-2 could be a good choice for general purpose application with a limited set of inputs, such as a University portal.  
      
    
-   **bCrypt**: It is generally used to generate the hash for user-passwords. bCrypt is based on the Blowfish cipher algorithm. It has a crucial phase for key setup. This phase starts with encrypting a sub-key and uses the output of this encryption to change another sub-key. This way, the bCrypt involves iterative steps for generating the hash, making it a preferred choice of developers for critical applications.  
      
    
-   **sCrypt**: It is a computationally intensive password-based key derivation function, proposed in 2016, as specified in  [RFC 7914](https://tools.ietf.org/html/rfc7914). As part of the algorithm, it generates a large vector of pseudorandom bit strings. Thus, it requires a large amount of memory for computation. It isn't easy for a brute-force-attacker to reverse the hash, as it would involve a significantly high amount of time, memory, and a high number (billion) of attempts. Other password-based key derivation functions such as PBKDF1 and PBKDF2 have relatively low resource demands.

## Explore Further - Collision
In several scenarios, two different keys can generate the same hash. Such a scenario is called  **Collision**. If we use a simple hash function, such as input length or sum of ASCII code of all characters, then it might lead to a collision. The diagram below lists the approaches used for collision resolution.

A collision can be resolved by using any of the following techniques:

1.  **Separate Chaining**  - It is a type of Open Hashing technique. The idea is to store the keys corresponding to collision (same) hash outputs in a Linked List. There would be a separate Linked List for each unique hash output.  
    
2.  **Open Addressing**  - It is also called Closed Hashing. In this approach, for a given set of $n$ input keys, we take a data structure that can accommodate more than $n$ keys. The idea is to store the keys corresponding to collision (same) hash outputs in the next available slot in the data structure.        
    -   Linear or quadratic probing - Keep  _probing_  until an empty slot is found.          
    -   **Double Hashing**  - We use two hash functions - one for hashing, and another for calculating the  _offset_. Then, this offset is appended to the output of the first hash function. This way, the final output is expected to be a collision-free value.

You may find it useful to read further about Collision Resolution Techniques  [here](https://en.wikipedia.org/wiki/Hash_table#Collision_resolution).

## Implement Hashing (SHA-256)
### MessageDigest class
The  `java.security.MessageDigest`  class provides us an easy implementation of the MD2, MD5, SHA-1, SHA-256, SHA-384, and SHA-512 algorithms. The following example shows the SHA-256 implementation by creating an instance of the MessageDigest class. For more details of the MessageDigest class, refer  [here](https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#MessageDigest).

### SecureRandom class
Salting is done by using an instance of the  `java.security.SecureRandom`  class. Let's look at the steps:

**Step 1 - Create a main method to call the custom** `createSalt()` **and** `get_SecurePassword()` **methods**

```java
//Necessary imports
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.MessageDigest;

public class SaltExample {

public static void main(String[] args)throws NoSuchAlgorithmException, NoSuchProviderException {
  // A static String for the example
  String passwordToHash = "myPassword";

  // Create a salt
  byte[] salt = createSalt();

  // Create a hash
  String securePassword = get_SecurePassword(passwordToHash, salt); 
  System.out.println(securePassword); 
 }
}

```

**Step 2 - Create the  `createSalt()`  to return a byte array**

```java
// Method to generate a Salt
private static byte[] createSalt() {
  SecureRandom random = new SecureRandom();
  byte[] salt = new byte[16];
  random.nextBytes(salt);
  return salt;
 }

```

**Step 3 - Create the  `get_SecurePassword()`  to return a String**

```java
// Method to generate the hash. 
//It takes a password and the Salt as input arguments
private static String get_SecurePassword(String passwordToHash, byte[] salt){
  String generatedPassword = null;
  try {
   MessageDigest md = MessageDigest.getInstance("SHA-256");
   md.update(salt);
   byte[] bytes = md.digest(passwordToHash.getBytes());
   StringBuilder sb = new StringBuilder();
   for(int i=0; i< bytes.length ;i++)
   {
    sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
   }
   generatedPassword = sb.toString();
  } 
  catch (NoSuchAlgorithmException e) {
   e.printStackTrace();
  }
  return generatedPassword;
 } 
```

## Implement Hashing (bCrypt)
### BCryptPasswordEncoder class
In our upcoming demo, we will demonstrate the use of  **bCrypt** technique for hashing in our Spring Boot project. Fortunately, you do not have to implement it from scratch, instead, you can use a library class  `org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder`  , which is a part of the Spring framework.

**Step 1 - Add the dependency**  
Add the  [Spring Boot Starter Security](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-security)  Maven dependency to the  `pom.xml`  
  
**Step 2 - Create an instance of the BCryptPasswordEncoder class**  
You can create an instance, and call any of the built-in methods.

```
  BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

  // Call a built-in method
  String securePassword = bCryptPasswordEncoder.encode("mySaltedPassword");

```

In the code snippet above,  `securePassword`  is the generated hash, and the  `mySaltedPassword`  is the String representing the raw password.

**Step 3 - Call either of the following built-in methods of the BCryptPasswordEncoder class**    

1.  `encode()`  method - Encodes the raw password. Generally, a good encoding algorithm applies an SHA-1 or greater hash combined with an 8-byte or greater randomly generated salt.
    
    ```
    // To encode a given rawPassword
    public String encode(CharSequence rawPassword) {
         if (rawPassword == null) {
             throw new IllegalArgumentException("rawPassword cannot be null");
         } else {
             String salt;
             if (this.random != null) {
                 salt = BCrypt.gensalt(this.version.getVersion(), this.strength, this.random);
             } else {
                 salt = BCrypt.gensalt(this.version.getVersion(), this.strength);
             }
    
             return BCrypt.hashpw(rawPassword.toString(), salt);
         }
     }
    
    ```

2.  `matches()`  method - It matches and verifies the encoded password obtained from the storage, and the submitted raw password (after encoding). Returns true if the passwords match, false if they do not. The stored password itself is never decoded.
    
    ```java
     public boolean matches(CharSequence rawPassword, String encodedPassword) {
         if (rawPassword == null) {
             throw new IllegalArgumentException("rawPassword cannot be null");
         } else if (encodedPassword != null && encodedPassword.length() != 0) {
             if (!this.BCRYPT_PATTERN.matcher(encodedPassword).matches()) {
                 this.logger.warn("Encoded password does not look like BCrypt");
                 return false;
             } else {
                 return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
             }
         } else {
             this.logger.warn("Empty encoded password");
             return false;
         }
     }
    
    ```
    
3.  `upgradeEncoding()`  method - It returns true if the encoded password should be encoded again for better security, else false. The default implementation always returns false.
    
    ```java
     public boolean upgradeEncoding(String encodedPassword) {
         if (encodedPassword != null && encodedPassword.length() != 0) {
             Matcher matcher = this.BCRYPT_PATTERN.matcher(encodedPassword);
             if (!matcher.matches()) {
                 throw new IllegalArgumentException("Encoded password does not look like BCrypt: " + encodedPassword);
             } else {
                 int strength = Integer.parseInt(matcher.group(2));
                 return strength < this.strength;
             }
         } else {
             this.logger.warn("Empty encoded password");
             return false;
         }
     }
    ```