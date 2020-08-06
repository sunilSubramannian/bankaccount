-- MySQL dump 10.13  Distrib 8.0.21, for Win64 (x86_64)
--
-- Host: localhost    Database: bankaccount
-- ------------------------------------------------------
-- Server version	5.7.26-log

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
-- Dumping routines for database 'bankaccount'
--
/*!50003 DROP PROCEDURE IF EXISTS `sp_make_transaction` */;
ALTER DATABASE `bankaccount` CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_make_transaction`(
in v_query json
)
BEGIN
DECLARE v_response json;
DECLARE i_from_account_id varchar(45);
DECLARE i_to_account_id varchar(45);
DECLARE i_amount double default 0.0;

start transaction;

set i_from_account_id = JSON_UNQUOTE(JSON_EXTRACT(v_query,'$.fromAccountId'));
set i_to_account_id = JSON_UNQUOTE(JSON_EXTRACT(v_query,'$.toAccountId'));
set i_amount = replace(replace(replace(JSON_UNQUOTE(JSON_EXTRACT(v_query,'$.amount')),'INR',''),'Rs',''),'Rs.','');
set @entry_date = current_timestamp();
set @exe_count = 0;

if((select count(distinct userid) from bankaccount.mapping_user_account
		where account_number in (i_from_account_id,i_to_account_id)) = 1) then
	set v_response = (select json_object('errorCode','412','errorMessage','Transfer between accounts belonging to the same user is not allowed'));
elseif((select count(1) from bankaccount.mapping_user_account where account_number = i_from_account_id) = 0) then
	set v_response = (select json_object('errorCode','412','errorMessage','Sender Account details Invalid!'));
elseif((select count(1) from bankaccount.mapping_user_account where account_number = i_to_account_id) = 0) then
	set v_response = (select json_object('errorCode','412','errorMessage','Receiver Account details Invalid!'));
elseif(i_amount <= 0.0) then
	set v_response = (select json_object('errorCode','412','errorMessage','Amount cannot be zero or negative'));
elseif((select balance_amount - i_amount from mapping_user_account
			where account_number = i_from_account_id) < 0.0 ) then
	set v_response = (select json_object('errorCode','412','errorMessage','Insufficient amount on the account.'));
elseif((select balance_amount + i_amount from bankaccount.mapping_user_account
			where account_number = i_to_account_id and atid = 1) > 50000) then
	set v_response = (select json_object('errorCode','412','errorMessage','Account will cross the maximum balance policy, cannot transfer the requested amount.'));
else 
	UPDATE `bankaccount`.`mapping_user_account`
	SET
	`balance_amount` = balance_amount - i_amount,
	`last_modified` = @entry_date
	WHERE `account_number` = i_from_account_id;
    set @exe_count = @exe_count + row_count();
    
    UPDATE `bankaccount`.`mapping_user_account`
	SET
	`balance_amount` = balance_amount + i_amount,
	`last_modified` = @entry_date
	WHERE `account_number` = i_to_account_id;
    set @exe_count = @exe_count + row_count();
    
    if(@exe_count = 2) then
        set v_response = (select json_object('newSrcBalance',ifnull((select balance_amount from mapping_user_account
																where account_number = i_from_account_id),0.0),
											'totalDestBalance',ifnull((select sum(balance_amount) from mapping_user_account
																where userid = (select userid from mapping_user_account
																		where account_number = i_to_account_id)),0.0),
											'transferedAt',@entry_date
						));
		
        commit;
    else
		rollback;
        set v_response = (select json_object('errorCode','412','errorMessage','Unknown error!'));
    end if;
end if;

	INSERT INTO `bankaccount`.`transaction_details`
	(`from_account_no`,`to_account_no`,`amount`,`created_date`,`response`)
	VALUES
	(i_from_account_id,i_to_account_id,i_amount,@entry_date,v_response);          
	select v_response;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
ALTER DATABASE `bankaccount` CHARACTER SET utf8 COLLATE utf8_general_ci ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-08-06 11:55:09
