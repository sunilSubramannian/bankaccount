package com.linus.bankaccount.model.repo;

import com.fasterxml.jackson.databind.JsonNode;
import com.linus.bankaccount.model.dao.Mapping_User_Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface TransactionRepository extends JpaRepository<Mapping_User_Account,Integer> {

    @Query(value = "select count(distinct userid) from bankaccount.mapping_user_account " +
            " where account_number in (:i_from_account_id,:i_to_account_id)", nativeQuery = true)
    int checksameuser(@Param("i_from_account_id") String i_from_account_id,@Param("i_to_account_id") String i_to_account_id);

    @Query(value = "select count(1) from bankaccount.mapping_user_account where account_number = :i_from_account_id", nativeQuery = true)
    int validSender(@Param("i_from_account_id") String i_from_account_id);

    @Query(value = "select count(1) from bankaccount.mapping_user_account where account_number = :i_to_account_id", nativeQuery = true)
    int validReciever(@Param("i_to_account_id") String i_to_account_id);

    @Query(value = "select balance_amount - :i_amount from mapping_user_account " +
            " where account_number = :i_from_account_id", nativeQuery = true)
    double checkBalance(@Param("i_from_account_id") String i_from_account_id, @Param("i_amount") String i_amount);

    @Query(value = "select ifnull((select balance_amount + :i_amount from bankaccount.mapping_user_account " +
            " where account_number = :i_to_account_id and atid = 1),0)", nativeQuery = true)
    double maxBalancePolicy(@Param("i_to_account_id") String i_to_account_id,@Param("i_amount") String i_amount);

    @Modifying
    @Query(value = "UPDATE `bankaccount`.`mapping_user_account` " +
            " SET" +
            " `balance_amount` = balance_amount - :i_amount," +
            " `last_modified` = :entry_date" +
            " WHERE `account_number` = :i_from_account_id",nativeQuery = true)
    int updateSenderBalance(@Param("i_from_account_id") String i_from_account_id,
                                @Param("i_amount") String i_amount, @Param("entry_date") Timestamp entry_date);

    @Modifying
    @Query(value = "UPDATE `bankaccount`.`mapping_user_account` " +
            " SET" +
            " `balance_amount` = balance_amount + :i_amount," +
            " `last_modified` = :entry_date" +
            " WHERE `account_number` = :i_to_account_id",nativeQuery = true)
    int updateReceiverBalance(@Param("i_to_account_id") String i_to_account_id,
                                @Param("i_amount") String i_amount, @Param("entry_date") Timestamp entry_date);


    @Query(value = "select balance_amount from mapping_user_account " +
            " where account_number = :i_from_account_id",nativeQuery = true)
    double newSenderBalance(@Param("i_from_account_id") String i_from_account_id);

    @Query(value = "select sum(balance_amount) from mapping_user_account " +
            " where userid = (select userid from mapping_user_account " +
            " where account_number = :i_to_account_id)",nativeQuery = true)
    double totReceiverBalance(@Param("i_to_account_id") String i_to_account_id);

    @Modifying
    @Query(value = "INSERT INTO `bankaccount`.`transaction_details`" +
            " (`from_account_no`,`to_account_no`,`amount`,`created_date`,`response`) " +
            " VALUES" +
            " (:i_from_account_id,:i_to_account_id,:i_amount,:entry_date,:iresponse );",nativeQuery = true)
    void insertTransaction(@Param("i_from_account_id") String i_from_account_id, @Param("i_to_account_id") String i_to_account_id,
            @Param("i_amount") String i_amount, @Param("entry_date") Timestamp entry_date, @Param("iresponse") String iresponse);





}
