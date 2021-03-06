package com.teamrogerio.openskullrework.usecase;

import com.teamrogerio.openskullrework.controller.exception.EmailFormatIsNotValidException;
import com.teamrogerio.openskullrework.controller.exception.PersonAlreadyExistsException;
import com.teamrogerio.openskullrework.controller.model.PersonResponse;
import com.teamrogerio.openskullrework.controller.translator.Translator;
import com.teamrogerio.openskullrework.entities.Person;
import com.teamrogerio.openskullrework.gateway.mongodb.repository.CreateNewPersonGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateNewPersonUseCase {

    private final CreateNewPersonGateway createNewPersonGateway;
    private final VerifyIfEmailIsValidUseCase verifyIfEmailIsValidUseCase;
    private final VerifyIfEmailAlreadyExistsUseCase verifyIfEmailAlreadyExistsUseCase;

    public PersonResponse execute(Person person) throws EmailFormatIsNotValidException, PersonAlreadyExistsException {
        verifyIfEmailIsValidUseCase.execute(person.getEmail());
        verifyIfEmailAlreadyExistsUseCase.execute(person.getEmail());
        return Translator.translate(createNewPersonGateway.execute(person), PersonResponse.class);
    }
}
