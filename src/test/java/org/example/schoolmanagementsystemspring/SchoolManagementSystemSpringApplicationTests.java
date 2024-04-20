package org.example.schoolmanagementsystemspring;

import org.example.schoolmanagementsystemspring.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SchoolManagementSystemSpringApplicationTests {

    @Test
    void contextLoads() {
        // create a simple test
        User user1 = new User();
        user1.setId(10);
        User user2 = new User();
        user2.setId(10);
        assertEquals(user2.getId(), user1.getId());
    }

    //method to return biggest integer



}
