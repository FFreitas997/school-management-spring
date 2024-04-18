package org.example.schoolmanagementsystemspring.attachment.repository;

import org.example.schoolmanagementsystemspring.attachment.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
    Optional<Attachment> findByProfilePictureTrue();
    @Query("select att from Attachment att inner join User u on u.id = att.user.id where u.email = :email")
    List<Attachment> findAttachmentByUserEmail(String email);

    @Query("select att from Attachment att inner join User u on u.id = att.user.id where u.email = :email and att.profilePicture = true")
    List<Attachment> findAttachmentByUserEmailAndByProfile(String email);
}