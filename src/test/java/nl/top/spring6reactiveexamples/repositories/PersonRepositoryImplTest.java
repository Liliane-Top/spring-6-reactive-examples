package nl.top.spring6reactiveexamples.repositories;

import nl.top.spring6reactiveexamples.domain.Person;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.lang.System.*;


class PersonRepositoryImplTest {

    PersonRepository personRepository = new PersonRepositoryImpl();

    @Test
    void testMonoByIdBlock(){
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
    void testFluxBlock(){
        Flux<Person> personFlux = personRepository.findAll();

        Person person = personFlux.blockFirst();//you only get first person
        out.println(person.toString());
    }

    @Test
    void testFluxSubscriber(){
        Flux<Person> personFlux = personRepository.findAll();
        personFlux.subscribe(out::println);
    }

    @Test
    void testFluxMap(){
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
    void testFilterOnName(){
        Flux<Person> personFlux = personRepository.findAll();

        personRepository.findAll().filter(person -> person.getLastName().equals("Kraak"))
                .subscribe(person -> out.println("name is " + person.getFirstName()));
    }

    @Test
    void testGetById(){
        Mono<Person> yoMono = personRepository.findAll()
                .filter(person -> person.getFirstName().equals("Yo"))
                .next();

        yoMono.subscribe(person -> out.println(person.getLastName()));
    }





}