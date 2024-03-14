package nl.top.spring6reactiveexamples.repositories;

import nl.top.spring6reactiveexamples.domain.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PersonRepositoryImplTest {

    PersonRepository personRepository = new PersonRepositoryImpl();

    @Test
    void testMonoByIdBlock() {
        Mono<Person> personMono = personRepository.getById(1);

        Person person = personMono.block();
        out.println(person.toString());
    }

    @Test
    void testGetByIdSubscriber() {
        Mono<Person> personMono = personRepository.getById(1);

        personMono.subscribe(person -> {
            out.println(person.toString());
        });
    }

    @Test
    void testMapOperation() {
        Mono<Person> personMono = personRepository.getById(1);

        personMono.map(person -> {
            return person.getFirstName();
        }).subscribe(firstName ->
                out.println(firstName));
    }

    @Test
    void testMapOperation2() {
        Mono<Person> personMono = personRepository.getById(1);

        personMono.map(Person::getFirstName)
                .subscribe(firstName ->
                        out.println(firstName));
    }

    @Test
    void testFluxBlock() {
        Flux<Person> personFlux = personRepository.findAll();

        Person person = personFlux.blockFirst();//you only get first person
        out.println(person.toString());
    }

    @Test
    void testFluxSubscriber() {
        Flux<Person> personFlux = personRepository.findAll();
        personFlux.subscribe(out::println);
    }

    @Test
    void testFluxMap() {
        Flux<Person> personFlux = personRepository.findAll();

        personFlux.map(Person::getFirstName)
                .subscribe(out::println);

        personFlux.map(Person::getLastName)
                .subscribe(out::println);

    }

    @Test
    void testFluxToList() {
        Flux<Person> personFlux = personRepository.findAll();
        Mono<List<Person>> listMono = personFlux.collectList();
        listMono.subscribe(out::print);
        out.println();
        personFlux.subscribe(person -> out.println(person.getLastName()));
    }

    @Test
    void testFilterOnName() {
        Flux<Person> personFlux = personRepository.findAll();

        personRepository.findAll().filter(person -> person.getLastName().equals("Kraak"))
                .subscribe(person -> out.println("name is " + person.getFirstName()));
    }

    @Test
    void testGetById() {
        Mono<Person> yoMono = personRepository.findAll()
                .filter(person -> person.getFirstName().equals("Yo"))
                .next();

        yoMono.subscribe(person -> out.println(person.getLastName()));
    }

    @Test
    void testFindPersonByIdNotFound() {
        Flux<Person> personFlux = personRepository.findAll();

        final Integer id = 8;
//next() will return an empty mono and no error will be thrown. therefore use single()
        Mono<Person> personMono = personFlux.filter(person -> person.getId().equals(id)).single()
                .doOnError(throwable -> {
                    out.println("error occured in the flux");
                    out.println(throwable.toString());
                });

        personMono.subscribe(person -> {
            out.println(person.toString());
        }, throwable -> {
            out.println("error occured in the mono");
            out.println(throwable.toString());
        });
    }

    @Test
    void testFindPersonById() {

        final Integer id = 2;
        Mono<Person> personMono = personRepository.getById(id);

        assertTrue(personMono.hasElement().block());

        personMono.subscribe(person -> {
            Assertions.assertEquals("Yo", person.getFirstName() );
            out.println("her name is " + person.getFirstName());
        });

    }

    @Test
    void testFindPersonByIdNotFoundMono() {
        final Integer id = 5;
        Mono<Person> personMono = personRepository.getById(id);

        assertFalse(personMono.hasElement().block());

    }

    @Test
    void testFindPersonByIdStepVerifier() {
        final Integer id = 4;
        Mono<Person> personMono = personRepository.getById(id);

        StepVerifier.create(personMono).expectNextCount(1).verifyComplete();
        personMono.subscribe(person -> out.println(person));

    }

    @Test
    void testFindPersonByIdNotFoundMonoStepVerifier() {
        final Integer id = 5;
        Mono<Person> personMono = personRepository.getById(id);

        StepVerifier.create(personMono).expectNextCount(0).verifyComplete();

    }


}