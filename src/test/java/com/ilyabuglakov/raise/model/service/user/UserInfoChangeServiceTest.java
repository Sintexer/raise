package com.ilyabuglakov.raise.model.service.user;

import com.ilyabuglakov.raise.domain.User;
import com.ilyabuglakov.raise.domain.type.Status;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.testng.Assert.*;

public class UserInfoChangeServiceTest {

    User user;
    UserInfoChangeService userInfoChangeService;

    @BeforeMethod
    public void setUp() {
        user = User.builder()
                .id(1)
                .name("Name")
                .email("Email@gmail.com")
                .registrationDate(LocalDate.now())
                .surname("Surname")
                .status(Status.ACTIVE)
                .password("121212")
                .build();
        userInfoChangeService = new UserInfoChangeService();
    }

    @Test
    public void testChangeName() {
        String newName = "newName";
        boolean expected = true;

        boolean actual = userInfoChangeService.changeName(user, newName);
        Assert.assertEquals(actual, expected);
        Assert.assertEquals(user.getName(), newName);
    }

    @Test
    public void testChangeSurname() {
        String newSurname = "newSurname";
        boolean expected = true;

        boolean actual = userInfoChangeService.changeSurname(user, newSurname);
        Assert.assertEquals(actual, expected);
        Assert.assertEquals(user.getSurname(), newSurname);
    }

    @Test
    public void testChangePassword() {
        String newPassword = "131313";
        boolean expected = true;

        boolean actual = userInfoChangeService.changePassword(user, newPassword, newPassword);
        Assert.assertEquals(actual, expected);
        Assert.assertEquals(user.getPassword(), new Sha256Hash(newPassword).toHex());
    }

    @Test
    public void test_fail_ChangePassword() {
        String newPassword = "131313";
        boolean expected = false;

        boolean actual = userInfoChangeService.changePassword(user, newPassword, newPassword+"1");
        Assert.assertEquals(actual, expected);
        Assert.assertNotEquals(user.getPassword(), newPassword);
    }
}