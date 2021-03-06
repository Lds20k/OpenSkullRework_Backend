package com.teamrogerio.openskullrework.controller;

import com.teamrogerio.openskullrework.controller.exception.*;
import com.teamrogerio.openskullrework.controller.model.ImageResponse;
import com.teamrogerio.openskullrework.controller.model.PersonRequest;
import com.teamrogerio.openskullrework.controller.model.PersonResponse;
import com.teamrogerio.openskullrework.controller.translator.Translator;
import com.teamrogerio.openskullrework.entities.Person;
import com.teamrogerio.openskullrework.usecase.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Component
@RequestMapping("/person")
public class PersonController {

    private final CreateNewPersonUseCase createNewPersonUseCase;
    private final GetAllPersonUseCase getAllPersonUseCase;
    private final GetPersonByIdUseCase getPersonByIdUseCase;
    private final SavePersonImageUseCase savePersonImageUseCase;
    private final GetPersonImageUseCase getPersonImageUseCase;
    private final UpdatePersonUseCase updatePersonUseCase;

    //Insere
    @PostMapping
    public ResponseEntity<PersonResponse> postPerson(@RequestBody PersonRequest request) throws EmailFormatIsNotValidException, PersonAlreadyExistsException {
        return new ResponseEntity<>(createNewPersonUseCase.execute(Translator.translate(request, Person.class)), HttpStatus.OK);
    }

    //Get todos
    @GetMapping
    public ResponseEntity<List<PersonResponse>> getAllPerson() {
        return new ResponseEntity<>(getAllPersonUseCase.execute(), HttpStatus.OK);
    }

    //Get um
    @GetMapping("/{personId}")
    public ResponseEntity<PersonResponse> getPersonById(@PathVariable("personId") String personId) throws PersonDoesNotExistsException {
        return new ResponseEntity<>(getPersonByIdUseCase.execute(personId), HttpStatus.OK);
    }

    //retorna imagem
    @GetMapping("/image/{personId}")
    public ResponseEntity<ImageResponse> getUserImage(@PathVariable("personId") String personId) throws PersonDoesNotExistsException, ProblemsToLoadImageException {
        return new ResponseEntity<>(getPersonImageUseCase.execute(personId), HttpStatus.OK);
    }

    //TODO: Deleta um

    //TODO: Retorna um jwt para login

    //atualiza dados
    @PutMapping("/{personId}")
    public ResponseEntity<PersonResponse> putPerson(@RequestBody PersonRequest personRequest, @PathVariable("personId") String personId) throws PersonDoesNotExistsException, EmailFormatIsNotValidException, PersonAlreadyExistsException {
        return new ResponseEntity<>(updatePersonUseCase.execute(Translator.translate(personRequest, Person.class), personId), HttpStatus.OK);
    }

    //faz upload da foto de perfil
    @PostMapping("/upload/{personId}")
    public ResponseEntity<PersonResponse> postPersonImage(@PathVariable("personId") String personId, @RequestParam MultipartFile imageFile) throws ProblemsToUploadImageException, PersonDoesNotExistsException, FileIsNotCompatibleException {
        return new ResponseEntity<>(savePersonImageUseCase.execute(personId, imageFile), HttpStatus.OK);
    }
}
