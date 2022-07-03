-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema parkyongman
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `parkyongman` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `parkyongman` ;

-- -----------------------------------------------------
-- Table `parkyongman`.`tb_board`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `parkyongman`.`tb_board` (
  `idx` INT NOT NULL AUTO_INCREMENT COMMENT '게시판번호(pk)',
  `upper_board_idx` INT NULL DEFAULT null COMMENT '상위게시판번호',
  `name` VARCHAR(50) NOT NULL COMMENT '게시판이름',
  `def` VARCHAR(2048) NOT NULL COMMENT '정의(설명)',
  `show_yn` ENUM('Y', 'N') NOT NULL DEFAULT 'Y' COMMENT '화면에 보일지여부',
  `creator` VARCHAR(20) NOT NULL COMMENT '생성자',
  `modifier` VARCHAR(20) NOT NULL COMMENT '수정자',
  `create_time` DATETIME NULL COMMENT '생정일',
  `update_time` DATETIME NULL COMMENT '수정일',
  PRIMARY KEY (`idx`),
  INDEX `fk_tb_board_tb_board1_idx` (`upper_board_idx` ASC) VISIBLE,
  UNIQUE INDEX `idx_UNIQUE` (`idx` ASC) VISIBLE,
  CONSTRAINT `fk_tb_board_tb_board1`
    FOREIGN KEY (`upper_board_idx`)
    REFERENCES `parkyongman`.`tb_board` (`idx`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '게시판';


-- -----------------------------------------------------
-- Table `parkyongman`.`tb_grade`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `parkyongman`.`tb_grade` (
  `id` INT NOT NULL COMMENT '등급id',
  `order` INT null COMMENT '등급순위',
  `name` VARCHAR(45) NOT NULL COMMENT '등급명',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `parkyongman`.`tb_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `parkyongman`.`tb_user` (
  `id` VARCHAR(40) NOT null COMMENT '유저아이디',
  `pw` VARCHAR(300) NOT null COMMENT '유저비번',
  `name` VARCHAR(30) NOT null COMMENT '유저이름',
  `email` VARCHAR(45) NULL COMMENT 'email',
  `phone` VARCHAR(45) null COMMENT '폰번호',
  `join_date` DATETIME NULL COMMENT '가입일',
  `last_login` DATETIME null COMMENT '마지막로그인',
  `exit_yn` ENUM('Y', 'N') NULL DEFAULT 'N' COMMENT '탈퇴여부',
  `grade_id` INT NOT null COMMENT '유저등급',
  PRIMARY KEY (`id`),
  INDEX `fk_tb_user_grade1_idx` (`grade_id` ASC) VISIBLE,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  CONSTRAINT `fk_tb_user_grade1`
    FOREIGN KEY (`grade_id`)
    REFERENCES `parkyongman`.`tb_grade` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `parkyongman`.`tb_post`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `parkyongman`.`tb_post` (
  `idx` INT NOT NULL AUTO_INCREMENT COMMENT '글번호(pk)',
  `board_idx` INT NOT null COMMENT '게시판번호(pk)',
  `title` VARCHAR(50) NOT NULL COMMENT '글제목',
  `content` VARCHAR(2048) NOT NULL COMMENT '글내용',
  `view_cnt` INT NOT NULL DEFAULT 0 COMMENT '조회수',
  `notice_yn` ENUM('Y', 'N') NOT NULL DEFAULT 'N' COMMENT '공지글 여부',
  `secret_yn` ENUM('Y', 'N') NOT NULL DEFAULT 'N' COMMENT '비밀글 여부',
  `delete_yn` ENUM('Y', 'N') NOT NULL DEFAULT 'N' COMMENT '삭제 여부',
  `writer` VARCHAR(40) NOT NULL COMMENT '글쓴이',
  `insert_time` DATETIME NULL DEFAULT now() COMMENT '등록일',
  `update_time` DATETIME NULL DEFAULT now() COMMENT '수정일',
  `delete_time` DATETIME NULL COMMENT '삭제일',
  PRIMARY KEY (`idx`),
  INDEX `fk_tb_post_tb_board1_idx` (`board_idx` ASC) VISIBLE,
  INDEX `fk_tb_post_tb_user1_idx` (`writer` ASC) VISIBLE,
  UNIQUE INDEX `idx_UNIQUE` (`idx` ASC) VISIBLE,
  CONSTRAINT `fk_tb_post_tb_board1`
    FOREIGN KEY (`board_idx`)
    REFERENCES `parkyongman`.`tb_board` (`idx`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tb_post_tb_user1`
    FOREIGN KEY (`writer`)
    REFERENCES `parkyongman`.`tb_user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '게시글\n';


-- -----------------------------------------------------
-- Table `parkyongman`.`tb_comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `parkyongman`.`tb_comment` (
  `idx` INT NOT NULL AUTO_INCREMENT COMMENT '댓글번호(pk)',
  `post_idx` INT NOT NULL COMMENT '게시글번호(fk)',
  `content` VARCHAR(2048) NOT NULL COMMENT '댓글내용',
  `notice_yn` ENUM('Y', 'N') NOT NULL DEFAULT 'N' COMMENT '공지글 여부',
  `delete_yn` ENUM('Y', 'N') NOT NULL DEFAULT 'N' COMMENT '삭제 여부',
  `writer` VARCHAR(20) NOT NULL COMMENT '작성자',
  `insert_time` DATETIME NULL DEFAULT now() COMMENT '등록일',
  `update_time` DATETIME NULL COMMENT '수정일',
  `delete_time` DATETIME NULL COMMENT '삭제일',
  PRIMARY KEY (`idx`),
  INDEX `fk_tb_comment_tb_board_idx` (`post_idx` ASC) VISIBLE,
  UNIQUE INDEX `idx_UNIQUE` (`idx` ASC) VISIBLE,
  CONSTRAINT `fk_tb_comment_tb_board`
    FOREIGN KEY (`post_idx`)
    REFERENCES `parkyongman`.`tb_post` (`idx`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '댓글';

-- -----------------------------------------------------
-- Table `parkyongman`.`tb_attach`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `parkyongman`.`tb_attach` (
  `idx` INT NOT NULL AUTO_INCREMENT COMMENT '파일 번호 (PK)',
  `post_idx` INT NOT NULL COMMENT '글번호(FK)',
  `original_name` VARCHAR(260) NOT NULL COMMENT '원본 파일명',
  `save_name` VARCHAR(40) NOT NULL COMMENT '저장 파일명',
  `size` INT NOT NULL COMMENT '파일 크기',
  `delete_yn` ENUM('Y', 'N') NOT NULL DEFAULT 'N' COMMENT '삭제 여부',
  `insert_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일',
  `delete_time` DATETIME NULL DEFAULT NULL COMMENT '삭제일',
  PRIMARY KEY (`idx`),
  UNIQUE INDEX `idx_UNIQUE` (`idx` ASC) VISIBLE,
  INDEX `fk_tb_attach_tb_post1_idx` (`post_idx` ASC) VISIBLE,
  CONSTRAINT `fk_tb_attach_tb_post1`
    FOREIGN KEY (`post_idx`)
    REFERENCES `parkyongman`.`tb_post` (`idx`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci
COMMENT = '첨부 파일';


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
