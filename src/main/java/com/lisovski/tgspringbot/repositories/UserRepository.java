package com.lisovski.tgspringbot.repositories;

import com.lisovski.tgspringbot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT token FROM users WHERE chat_id=:chatId",nativeQuery = true)
    String getTokenByChatId(@Param(value = "chatId") long chatId);

    @Query(value = "SELECT * FROM users WHERE chat_id=:chatId",nativeQuery = true)
    User getUser(@Param(value = "chatId") long chatId);

    @Query(value = "SELECT EXISTS(SELECT * FROM users WHERE chat_id=:chatId)", nativeQuery = true)
    boolean existsByChatId(@Param(value = "chatId")long chatId);

    void deleteById(int id);

    @Query(value = "INSERT INTO users (chat_id, token, state, aff_id) VALUES (:chatId,:token,:state,:aff_id) RETURNING *", nativeQuery = true)
    int insert(@Param(value = "chatId") long chatId,
               @Param(value = "token") String token,
               @Param(value = "state") String state,
               @Param(value = "aff_id") int aff_id);

    @Query(value = "UPDATE users SET state=:state WHERE chat_id=:chatId RETURNING 1", nativeQuery = true)
    int updateStateByChatId(@Param(value = "state") String state,
                             @Param(value = "chatId") long chatId);

    @Query(value = "UPDATE users SET token=:token WHERE chat_id=:chatId RETURNING 1", nativeQuery = true)
    int updateTokenByChatId(@Param(value = "token") String token,
                            @Param(value = "chatId") long chatId);

    @Query(value = "UPDATE users SET token=:token,state=:state,aff_id=:aff_id WHERE chat_id=:chatId RETURNING 1", nativeQuery = true)
    int updateTokenAndStateAndAffIdByChatId(@Param(value = "token") String token,
                                            @Param(value = "state") String state,
                                            @Param(value = "aff_id") int aff_id,
                                            @Param(value = "chatId") long chatId);

    @Query(value = "UPDATE users SET token='skibidi',state='AUTH',aff_id=0 WHERE chat_id=:chatId RETURNING 1", nativeQuery = true)
    int refreshUserByChatId(@Param(value = "chatId") long chatId);
}
