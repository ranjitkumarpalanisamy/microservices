package msmail.msmail.repository;

import org.springframework.data.repository.CrudRepository;

import msmail.msmail.entity.Mail;

public interface MailRepository extends CrudRepository<Mail, Long> {

}
