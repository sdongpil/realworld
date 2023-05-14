package com.dss.realworld.user.app;

import com.dss.realworld.user.api.ProfileResponseDto;
import com.dss.realworld.user.domain.User;
import com.dss.realworld.user.domain.repository.FollowRelationRepository;
import com.dss.realworld.user.domain.repository.UserRepository;
import com.dss.realworld.util.UserFixtures;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ProfileServiceTest {

    @Autowired
    private ProfileService profileService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FollowRelationRepository followRelationRepository;

    @BeforeEach
    void setUp() {
        clearTable();

        User newUser = UserFixtures.create();
        userRepository.persist(newUser);
    }

    @AfterEach
    void teatDown() {
        clearTable();
    }

    private void clearTable() {
        userRepository.deleteAll();
        userRepository.resetAutoIncrement();

        followRelationRepository.deleteAll();
        followRelationRepository.resetAutoIncrement();
    }
    @DisplayName("username이 유효하면 GetProfileDto 가져오기 성공")
    @Test
    void t1() {
        ProfileResponseDto profileDto = profileService.getProfileDto("Jacob000");

        assertThat(profileDto.getUsername()).isEqualTo("Jacob000");
    }

    @DisplayName("followeeUsername, followerId 유효하면 팔로우 성공 ")
    @Test
    void t2() {
        //given
        String followeeUsername = "Jacob000";
        User user2 = User.builder()
                .username("son")
                .email("@naver")
                .password("1234")
                .build();
        userRepository.persist(user2);
        Long followerId = user2.getId();

        //when
        ProfileResponseDto profileResponseDto = profileService.followUser(followeeUsername, followerId);

        //then
        assertThat(profileResponseDto.getUsername()).isEqualTo("Jacob000");
        assertThat(profileResponseDto.isFollowing()).isEqualTo(true);
    }

    @DisplayName("followeeUsername, followerId 유효하면 팔로우 취소")
    @Test
    void t3() {
        //given
        String followeeUsername = "Jacob000";
        User user2 = User.builder()
                .username("son")
                .email("@naver")
                .password("1234")
                .build();
        userRepository.persist(user2);

        Long followerId = user2.getId();

        //when
        profileService.followUser(followeeUsername, followerId);
        ProfileResponseDto profileResponseDto = profileService.unFollowUser(followeeUsername, followerId);

        //then
        assertThat(profileResponseDto.getUsername()).isEqualTo(followeeUsername);
        assertThat(profileResponseDto.isFollowing()).isEqualTo(false);
    }
}