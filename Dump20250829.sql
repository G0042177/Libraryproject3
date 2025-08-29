CREATE DATABASE  IF NOT EXISTS `library_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `library_db`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: library_db
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
  `book_id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `author` varchar(50) NOT NULL,
  `type` enum('EBook','PrintedBook') NOT NULL,
  `available` tinyint(1) DEFAULT '1',
  `quantity` int DEFAULT '1',
  PRIMARY KEY (`book_id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES (1,'Introduction to Java','James Gosling','PrintedBook',1,5),(2,'Database Systems','C.J. Date','PrintedBook',1,3),(3,'Learning SQL','Alan Beaulieu','EBook',1,2),(4,'The Bible','Various','PrintedBook',1,9),(5,'Harry Potter and the Philosopher\'s Stone','J.K. Rowling','PrintedBook',1,1),(6,'Harry Potter and the Chamber of Secrets','J.K. Rowling','PrintedBook',1,2),(7,'Harry Potter and the Prisoner of Azkaban','J.K. Rowling','PrintedBook',1,9),(8,'Harry Potter and the Goblet of Fire','J.K. Rowling','PrintedBook',1,4),(9,'Harry Potter and the Order of the Phoenix','J.K. Rowling','PrintedBook',1,3),(10,'Harry Potter and the Half-Blood Prince','J.K. Rowling','PrintedBook',1,7),(11,'Harry Potter and the Deathly Hallows','J.K. Rowling','PrintedBook',1,3),(12,'A Game of Thrones','George R. R. Martin','PrintedBook',1,6),(13,'A Clash of Kings','George R. R. Martin','PrintedBook',1,4),(14,'A Storm of Swords','George R. R. Martin','PrintedBook',1,9),(15,'A Feast for Crows','George R. R. Martin','PrintedBook',1,8),(16,'A Dance with Dragons','George R. R. Martin','PrintedBook',1,7),(17,'The Summer I Turned Pretty','Jenny Han','PrintedBook',1,7),(18,'It\'s Not Summer Without You','Jenny Han','PrintedBook',1,6),(19,'We\'ll Always Have Summer','Jenny Han','PrintedBook',1,2),(20,'Diary of a Wimpy Kid','Jeff Kinney','PrintedBook',1,3),(21,'Diary of a Wimpy Kid: Rodrick Rules','Jeff Kinney','PrintedBook',1,6),(22,'Diary of a Wimpy Kid: The Last Straw','Jeff Kinney','PrintedBook',1,5),(23,'Diary of a Wimpy Kid: Dog Days','Jeff Kinney','PrintedBook',1,2),(24,'The 48 Laws of Power','Robert Greene','EBook',1,9),(25,'Atomic Habits','James Clear','EBook',1,7),(26,'Deep Work','Cal Newport','EBook',1,6),(27,'Thinking, Fast and Slow','Daniel Kahneman','EBook',1,2),(28,'The Lean Startup','Eric Ries','EBook',1,8),(29,'The Pragmatic Programmer','Andrew Hunt; David Thomas','EBook',1,7),(30,'Introduction to Machine Learning','Ethem Alpaydin','EBook',1,5),(31,'Discrete Mathematics and Its Applications','Kenneth H. Rosen','EBook',1,7),(32,'Principles of Economics','N. Gregory Mankiw','EBook',1,3),(33,'Psychology: Themes and Variations','Wayne Weiten','EBook',1,4),(34,'The Bible','Various','PrintedBook',1,4);
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `members`
--

DROP TABLE IF EXISTS `members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `members` (
  `member_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `type` enum('Student','Staff') NOT NULL,
  PRIMARY KEY (`member_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `members`
--

LOCK TABLES `members` WRITE;
/*!40000 ALTER TABLE `members` DISABLE KEYS */;
INSERT INTO `members` VALUES (2,'Aoife Murphy','aoife.murphy01@gmail.com','Student'),(3,'Liam O\'Connor','liam.oconnor99@hotmail.com','Student'),(4,'Katie Byrne','katie_byrne12@yahoo.com','Student'),(5,'Shane McCarthy','shanemccarthy23@outlook.com','Student'),(6,'Niamh Kelly','niamh.kelly@atu.ie','Student'),(7,'Conor Walsh','cwalsh04@gmail.com','Student'),(8,'Dr. Fiona Daly','fiona.daly@atu.ie','Staff'),(9,'Patrick O\'Sullivan','posullivan@atu.ie','Staff'),(10,'Mary Healy','mary.healy@library.ie','Staff'),(11,'John Buckley','johnbuckley@atu.ie','Staff'),(13,'samuel','G00421777@atu.ie','Staff');
/*!40000 ALTER TABLE `members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rentals`
--

DROP TABLE IF EXISTS `rentals`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rentals` (
  `rental_id` int NOT NULL AUTO_INCREMENT,
  `member_id` int NOT NULL,
  `book_id` int NOT NULL,
  `rental_date` date NOT NULL,
  `return_date` date DEFAULT NULL,
  PRIMARY KEY (`rental_id`),
  KEY `fk_member` (`member_id`),
  KEY `fk_book` (`book_id`),
  CONSTRAINT `fk_book` FOREIGN KEY (`book_id`) REFERENCES `books` (`book_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_member` FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rentals`
--

LOCK TABLES `rentals` WRITE;
/*!40000 ALTER TABLE `rentals` DISABLE KEYS */;
/*!40000 ALTER TABLE `rentals` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-29 18:15:00
