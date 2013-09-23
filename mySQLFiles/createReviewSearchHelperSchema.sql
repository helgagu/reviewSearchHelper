SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `reviewSearchResults` ;
CREATE SCHEMA IF NOT EXISTS `reviewSearchResults` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
SHOW WARNINGS;
USE `reviewSearchResults` ;

-- -----------------------------------------------------
-- Table `reviewSearchResults`.`binsearch_results`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `reviewSearchResults`.`binsearch_results` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `reviewSearchResults`.`binsearch_results` (
  `idbinsearch_results` INT NOT NULL AUTO_INCREMENT,
  `Keyword` VARCHAR(30) NOT NULL,
  `TotalResults` BIGINT NOT NULL,
  `TotalPages` BIGINT NOT NULL,
  `AmazonLocale` VARCHAR(5) NOT NULL,
  `SearchParams_BrowseNodeId` VARCHAR(10) NULL,
  `SearchParams_ItemPage` VARCHAR(2) NULL,
  `SearchParams_SearchIndex` VARCHAR(30) NULL,
  `SearchParams_PowerSearch` VARCHAR(60) NULL,
  `SearchParams_Sort` VARCHAR(20) NULL,
  `SearchParams_MerchantId` VARCHAR(10) NULL,
  `SearchParams_Availability` VARCHAR(15) NULL,
  `SearchParams_ResponseGroup` VARCHAR(45) NULL,
  `RequestTimestamp` VARCHAR(20) NULL,
  `OriginalResponse` BLOB NOT NULL,
  `Timestamp` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UpdatedTimestamp` DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`idbinsearch_results`),
  UNIQUE INDEX `idbinsearch_results_UNIQUE` (`idbinsearch_results` ASC))
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `reviewSearchResults`.`browsenodes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `reviewSearchResults`.`browsenodes` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `reviewSearchResults`.`browsenodes` (
  `idbrowsenodes` INT NOT NULL AUTO_INCREMENT,
  `binName` VARCHAR(100) NOT NULL,
  `BinItemCount` BIGINT NOT NULL,
  `BrowseNodeId` VARCHAR(10) NOT NULL,
  `parentBrowseNode` VARCHAR(10) NULL,
  `keyword` VARCHAR(30) NOT NULL,
  `exclusionReason` VARCHAR(100) NULL,
  `Timestamp` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UpdatedTimestamp` DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `idbinsearch_results` INT NOT NULL,
  PRIMARY KEY (`idbrowsenodes`),
  UNIQUE INDEX `idbrowsenodes_UNIQUE` (`idbrowsenodes` ASC),
  INDEX `fk_browsenodes_binsearch_results1_idx` (`idbinsearch_results` ASC),
  CONSTRAINT `fk_browsenodes_binsearch_results1`
    FOREIGN KEY (`idbinsearch_results`)
    REFERENCES `reviewSearchResults`.`binsearch_results` (`idbinsearch_results`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `reviewSearchResults`.`asin`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `reviewSearchResults`.`asin` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `reviewSearchResults`.`asin` (
  `idasin` INT NOT NULL AUTO_INCREMENT,
  `asin` VARCHAR(45) NOT NULL,
  `Timestamp` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UpdatedTimestamp` DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`idasin`),
  UNIQUE INDEX `asin_UNIQUE` (`asin` ASC),
  UNIQUE INDEX `idasin_UNIQUE` (`idasin` ASC))
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `reviewSearchResults`.`childBrowseNodesToSearch`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `reviewSearchResults`.`childBrowseNodesToSearch` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `reviewSearchResults`.`childBrowseNodesToSearch` (
  `idchildBrowseNodesToSearch` INT NOT NULL AUTO_INCREMENT,
  `binName` VARCHAR(100) NULL,
  `binItemCount` BIGINT NULL,
  `browseNodeId` VARCHAR(10) NOT NULL,
  `parentBrowseNode` VARCHAR(10) NULL,
  `keyword` VARCHAR(45) NOT NULL,
  `endpoint` VARCHAR(5) NOT NULL,
  `resultsFetched` INT NOT NULL DEFAULT 0,
  `Timestamp` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `idbrowsenodes` INT NULL,
  INDEX `fk_childBrowseNodesToSearch_browsenodes1_idx` (`idbrowsenodes` ASC),
  PRIMARY KEY (`idchildBrowseNodesToSearch`),
  CONSTRAINT `fk_childBrowseNodesToSearch_browsenodes1`
    FOREIGN KEY (`idbrowsenodes`)
    REFERENCES `reviewSearchResults`.`browsenodes` (`idbrowsenodes`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `reviewSearchResults`.`browsenodes_asin`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `reviewSearchResults`.`browsenodes_asin` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `reviewSearchResults`.`browsenodes_asin` (
  `idbrowsenodes_asin` INT NOT NULL AUTO_INCREMENT,
  `asin` VARCHAR(45) NOT NULL,
  `Timestamp` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UpdatedTimestamp` DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `idasin` INT NOT NULL,
  `binsearch_results_idbinsearch_results` INT NOT NULL,
  `idchildBrowseNodesToSearch` INT NOT NULL,
  UNIQUE INDEX `idbrowsenodes_asin_UNIQUE` (`idbrowsenodes_asin` ASC),
  INDEX `fk_browsenodes_asin_asin1_idx` (`idasin` ASC),
  INDEX `fk_browsenodes_asin_binsearch_results1_idx` (`binsearch_results_idbinsearch_results` ASC),
  INDEX `fk_browsenodes_asin_childBrowseNodesToSearch1_idx` (`idchildBrowseNodesToSearch` ASC),
  PRIMARY KEY (`idbrowsenodes_asin`),
  CONSTRAINT `fk_browsenodes_asin_asin1`
    FOREIGN KEY (`idasin`)
    REFERENCES `reviewSearchResults`.`asin` (`idasin`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_browsenodes_asin_binsearch_results1`
    FOREIGN KEY (`binsearch_results_idbinsearch_results`)
    REFERENCES `reviewSearchResults`.`binsearch_results` (`idbinsearch_results`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_browsenodes_asin_childBrowseNodesToSearch1`
    FOREIGN KEY (`idchildBrowseNodesToSearch`)
    REFERENCES `reviewSearchResults`.`childBrowseNodesToSearch` (`idchildBrowseNodesToSearch`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `reviewSearchResults`.`books`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `reviewSearchResults`.`books` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `reviewSearchResults`.`books` (
  `idbooks` INT NOT NULL AUTO_INCREMENT,
  `asin` VARCHAR(45) NULL,
  `isbn` VARCHAR(15) NULL,
  `title` VARCHAR(400) NULL,
  `authors` VARCHAR(200) NULL,
  `pages` BIGINT NULL,
  `salesrank` BIGINT NULL,
  `edition` VARCHAR(100) NULL,
  `manufacturer` VARCHAR(100) NULL,
  `publisher` VARCHAR(100) NULL,
  `publicationDate` VARCHAR(45) NULL,
  `detailPageUrl` VARCHAR(400) NULL,
  `eisbn` VARCHAR(45) NULL,
  `binding` VARCHAR(45) NULL,
  `AmazonLocale` VARCHAR(5) NULL,
  `originalResponse` BLOB NULL,
  `exclusionReason` VARCHAR(100) NULL,
  `Timestamp` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `UpdatedTimestamp` DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `idasin` INT NOT NULL,
  PRIMARY KEY (`idbooks`),
  INDEX `fk_books_asin1_idx` (`idasin` ASC),
  UNIQUE INDEX `idbooks_UNIQUE` (`idbooks` ASC),
  CONSTRAINT `fk_books_asin1`
    FOREIGN KEY (`idasin`)
    REFERENCES `reviewSearchResults`.`asin` (`idasin`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `reviewSearchResults`.`editorialReviews`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `reviewSearchResults`.`editorialReviews` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `reviewSearchResults`.`editorialReviews` (
  `ideditorialReviews` INT NOT NULL AUTO_INCREMENT,
  `editorialReview` BLOB NULL,
  `source` VARCHAR(60) NULL,
  `originalResponse` BLOB NOT NULL,
  `Timestamp` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UpdatedTimestamp` DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `idbooks` INT NOT NULL,
  PRIMARY KEY (`ideditorialReviews`),
  INDEX `fk_editorialReviews_books1_idx` (`idbooks` ASC),
  CONSTRAINT `fk_editorialReviews_books1`
    FOREIGN KEY (`idbooks`)
    REFERENCES `reviewSearchResults`.`books` (`idbooks`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `reviewSearchResults`.`excludedBrowseNodes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `reviewSearchResults`.`excludedBrowseNodes` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `reviewSearchResults`.`excludedBrowseNodes` (
  `idexcludedBrowseNodes` INT NOT NULL AUTO_INCREMENT,
  `browseNodeId` VARCHAR(45) NOT NULL,
  `exclusionReason` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`idexcludedBrowseNodes`),
  UNIQUE INDEX `idexcludedBrowseNodes_UNIQUE` (`idexcludedBrowseNodes` ASC),
  UNIQUE INDEX `browseNodeId_UNIQUE` (`browseNodeId` ASC))
ENGINE = InnoDB;

SHOW WARNINGS;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
